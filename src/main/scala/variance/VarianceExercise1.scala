package variance

object VarianceExercise1 extends App {

  trait Food {

    def name: String

  }

  class Meat(val name: String) extends Food

  class Vegetable(val name: String) extends Food

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
  case class GenericRecipe(ingredients: List[Food]) extends Recipe[Food] {

    def name: String = s"Generic recipe based on ${ingredients.map(_.name)}"

  }
  // TODO it is also type class instance for type Meat
  case class MeatRecipe(ingredients: List[Meat]) extends Recipe[Meat] {

    def name: String = s"Meat recipe based on ${ingredients.map(_.name)}"

  }

  // TODO it is also type class instance for type White Food
  case class WhiteMeatRecipe(ingredients: List[WhiteMeat]) extends Recipe[WhiteMeat] {

    def name: String = s"Meat recipe based on ${ingredients.map(_.name)}"

  }
  //Recipe[Food] <-  Recipe[Meat]
  val recipe: Recipe[Food] = MeatRecipe(List(beef, turkey))
  // Recipe[Food]: Based on Meat or Vegetable
  val mixRecipe: GenericRecipe =
     GenericRecipe(List(chicken, carrot, beef, pumpkin))
  // Recipe[Food] <- Recipe[Meat]: Based on any kind of Meat
  val meatRecipe =  MeatRecipe(List(beef, turkey))
  // Recipe[Food] <- Recipe[Meat] <- Recipe[WhiteMeat]: Based only on WhiteMeat
  val whiteMeatRecipe =  WhiteMeatRecipe(List(chicken, turkey))



}
