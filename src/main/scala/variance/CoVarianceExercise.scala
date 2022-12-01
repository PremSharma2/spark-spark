package variance

object CoVarianceExercise extends App {



  /*

TODO
    Design approach: ->
    The Recipe has a name and a list of ingredients.
   The type of list of ingredients has the same type of Recipe.
   To express that the Recipe is covariant in its type A, we write it as Recipe[+A].
    The generic recipe is based on every kind of food ingredients,
    the meat recipe is based on meat ingredients ,
    and a white meat recipe has just white meat in its list of ingredients.
    Hence we solved the problem Inheritance by introducing the different variant types
    Recipe[Food] <-  Recipe[Meat] <- Recipe[WhiteMeat]
    i.e Recipe[Meat] can extends to Recipe[Food]
    recipe: Recipe[Food] = new GenericRecipe[Meat]
   */

  //TODO defining ADTs
   trait Food {

    def name: String

  }

  /*
TODO
    Lets proof our inheritance analogy in terms of Recipe
    Recipe, is a covariant type i.e Recipes can also have variance relationship and because
    Recipe is collection of Food and its subtypes
    Let's define the covariant type Recipe.
     It takes a component type that expresses the base food for the recipe -
     that is, a recipe based on meat, vegetable, etc.
   */
  // Here is the contract for the Recipe will look like
  //TODO If your Generic class creates or contains elements of type T it should be +T
  //TODO here recipe contains the elements of type T
  //TODO its a classic case of Producer so it will be covariant
  trait Recipe[+A] {

    def name: String

    def recipeIngredients: List[A]

  }

   class Meat(override val name: String) extends Food

   class Vegetable(override val name: String) extends Food

   class WhiteMeat(override val name: String) extends Meat(name)


//Producer
  abstract class MyRecipe[+A](val ingredients: List[A]){
    def name: String

    def recipeIngredients: List[A] = ingredients
  }

  class WorldRecipe extends MyRecipe[Food]( ingredients = List(carrot,pumpkin)) {
    override def name: String = s"Generic recipe based on ${recipeIngredients.map(_.name)}"
  }
  // TODO it is also type class instance for type Food
  case class GenericRecipe(override val recipeIngredients: List[Food]) extends Recipe[Food] {

    def name: String = s"Generic recipe based on ${recipeIngredients.map(_.name)}"

  }

  // TODO it is also type class instance for type Meat
  case class MeatRecipe(override val recipeIngredients: List[Meat]) extends Recipe[Meat] {

    def name: String = s"Meat recipe based on ${recipeIngredients.map(_.name)}"

  }

  // TODO it is also type class instance for type White Food
  case class WhiteMeatRecipe(override val recipeIngredients: List[WhiteMeat]) extends Recipe[WhiteMeat] {

    def name: String = s"Meat recipe based on ${recipeIngredients.map(_.name)}"

  }


  /*
 We can create some food instances of various type.
 They will be the ingredients of the recipes we are going to serve in our restaurants.
  */
  // Food <- Meat
  val beef = new Meat("beef")
  // Food <- Meat <- WhiteMeat
  val chicken = new WhiteMeat("chicken")
  val turkey = new WhiteMeat("turkey")
  // Food <- Vegetable
  val carrot = new Vegetable("carrot")
  val pumpkin = new Vegetable("pumpkin")
  //Recipe[Food] <-  Recipe[Meat]
  val recipe: Recipe[Food] = MeatRecipe(List(beef, turkey))
  // Recipe[Food]: Based on Meat or Vegetable
  val mixRecipe: Recipe[Food] =
    GenericRecipe(List(chicken, carrot, beef, pumpkin))
  // Recipe[Food] <- Recipe[Meat]: Based on any kind of Meat
  val meatRecipe: Recipe[Food] = MeatRecipe(List(beef, turkey))
  // Recipe[Food] <- Recipe[Meat] <- Recipe[WhiteMeat]: Based only on WhiteMeat
  val whiteMeatRecipe: Recipe[Food] = WhiteMeatRecipe(List(chicken, turkey))

  //TODO : Design an API for recipe for Food and i will pass
  //TODO we wil pass all recipes
  def processRecipe(recipe: Recipe[Food]) = {
    recipe.recipeIngredients.foreach(println(_))
  }
  processRecipe(whiteMeatRecipe)


  /*
TODO
     One more use case
     In general, covariant type parameter can be used as immutable field type,
     method return type and also as method argument type
      if the method argument type has a lower bound.
       Because of those restrictions, covariance is most commonly used in producers (types that return something)
        and immutable types. Those rules are applied in the following implementation of vending machine.
   */

 case  class VendingMachine[+A](val currentItem: Option[A], val items: List[A]) {

    def this(items: List[A]) = this(None, items)

    def dispenseNext(): VendingMachine[A] =
      items match {
        case Nil => {
          if (currentItem.isDefined)
             VendingMachine(None, Nil)
          else
            this
        }
        case t :: ts => {
           VendingMachine(Some(t), ts)
        }
      }

    def addAll[B >: A](newItems: List[B]): VendingMachine[B] =
      new VendingMachine(items ++ newItems)

  }


  /*
TODO
     Type parameter with variance annotation (covariant + or contravariant -)
     can be used as mutable field type only if the field has object private scope (private[this]).
     This is explained in Programming In Scala
     Object private members can be accessed only
     from within the object in which they are defined.
     It turns out that accesses to variables from the same object
     in which they are defined do not cause problems with variance.
     The intuitive explanation is that,
     in order to construct a case where variance would lead to type errors,
     you need to have a reference to a containing object
     that has a statically weaker type than the type the object was defined with.
     For accesses to object private values, however, this is impossible.
 */

  trait Bullet
  case object NormalBullet extends Bullet
  case object ExplosiveBullet extends Bullet
  /*
TODO
  Bullets are contained in the the ammo magazine as seen in the next code listing.
  Notice that class AmmoMagazine is covariant in its type parameter A.
  It also contains mutable field bullets which compiles
  because of object private scope.
  Everytime the method giveNextBullet is invoked,
  bullet from the bullets list is removed.
  AmmoMagazine can't be refilled with bullets
  and there is no way of introducing this feature into this class because that would have led to potential runtime errors.
 */

  final  class AmmoMagazine[+A <: Bullet]( private[this] var bullets: List[A])
  {
    def hasBullets: Boolean = !bullets.isEmpty

    def giveNextBullet(): Option[A] =
      bullets match {
        case Nil => {
          None
        }
        case t :: ts => {
          bullets = ts
          Some(t)
        }
      }

  }

  final class Gun(private var ammoMag: AmmoMagazine[Bullet]) {

    def reload(ammoMag: AmmoMagazine[Bullet]): Unit =
      this.ammoMag = ammoMag

    def hasAmmo: Boolean = ammoMag.hasBullets

    /** Returns Bullet that was shoot or None if there is ammo left */
    def shoot(): Option[Bullet] = ammoMag.giveNextBullet()

  }
  // val gun = new Gun(AmmoMagazine)
  // compiles because of covariant subtyping
  //gun.reload(AmmoMagazine.newExplosiveBulletsMag)
}
