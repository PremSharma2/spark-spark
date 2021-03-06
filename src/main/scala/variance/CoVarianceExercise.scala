package variance

object CoVarianceExercise extends App {
//TODO defining ADTs
  sealed trait Food {

    def name: String

  }

   class Meat(override val name: String) extends Food

   class Vegetable(override val name: String) extends Food

   class WhiteMeat(override val name: String) extends Meat(name)

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

  /*
  Lets proof our inheritance analogy in terms of Recipe
  Recipe, is a covariant type i.e Recipes can also have variance relationship and because
  Recipe is collection of Food and its subtypes
Let's define the covariant type Recipe. It takes a component type that expresses the base food for the recipe -
that is, a recipe based on meat, vegetable, etc.
   */
  // Here is the contract for the Recipe will look like
  //TODO If your Generic class creates or contains elements of type T it should be +T
  //TODO here recipe contains the elements of type T
  trait Recipe[+A] {

    def name: String

    def ingredients: List[A]

  }

  /*
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

  // TODO it is also type class instance for type Food
  case class GenericRecipe(override val ingredients: List[Food]) extends Recipe[Food] {

    def name: String = s"Generic recipe based on ${ingredients.map(_.name)}"

  }

  // TODO it is also type class instance for type Meat
  case class MeatRecipe(override val ingredients: List[Meat]) extends Recipe[Meat] {

    def name: String = s"Meat recipe based on ${ingredients.map(_.name)}"

  }

  // TODO it is also type class instance for type White Food
  case class WhiteMeatRecipe(override val ingredients: List[WhiteMeat]) extends Recipe[WhiteMeat] {

    def name: String = s"Meat recipe based on ${ingredients.map(_.name)}"

  }

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
    recipe.ingredients.foreach(println(_))
  }

  processRecipe(whiteMeatRecipe)
}
