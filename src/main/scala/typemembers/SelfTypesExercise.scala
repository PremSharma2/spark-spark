package typemembers

object SelfTypesExercise  extends App {

trait Edible
// person hierarchy
  trait Person {
    def hasAllergiesFrom(thing: Edible): Boolean
  }
  trait Child extends Person
  trait Adult extends Person

  // diet hierarchy
  trait Diet {
    def eat(thing: Edible): Boolean
  }
  trait Carnivore extends Diet
  trait Vegetarian extends Diet
/*
TODO
     The problem is that you want the diet to be applicable to Persons only.
     Not only that, but your functionality actually relies on logic from the Person class.
      For example, you need a person’s age or weight while you implement your Diet API.
      This is often an issue when you design library APIs. There are various options to do it.
      // option 1
     trait Diet extends Person
     trait Diet[P <: Person]
    This option adds a degree of separation by adding a type argument.
    A bit better, but with its own problems.
    You have access to the Person type,
    but not to its methods - you would need an instance of Person to access the logic,
    but then you’d need to pass a Person as a constructor argument.
   Until Scala 3 comes along, there’s no way for you to do that,
   or enforce any Diet subclasses to pass a person as an argument.
   Not to mention variance problems. Is a vegetarian diet for an adult also applicable to teenagers? Which implementations can I reuse for which types?
 */
  // option 3
  trait Diet1 { self: Person =>
    def eat(thing: Edible): Boolean ={
        if (self.hasAllergiesFrom(thing)) false
        else true
    }
  }
  trait Carnivore1 extends Diet1 with Person
  trait Vegetarian1 extends Diet1  with Person

  class VegAthlete1 extends Vegetarian1 with Adult {
    override def hasAllergiesFrom(thing: Edible): Boolean = false
  }

  val athlete = new VegAthlete1
}
