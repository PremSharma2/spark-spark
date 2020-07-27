package oops

object InheritanceInScala extends App {

  class Animal {
    val creatureType = "wildanimal"
    def eat = println("nom-nom-nom")
  }
  class Cat extends Animal
  val cat = new Cat
  cat.eat

  // constructors
  class Person(name: String, age: Int) {
    //auxillary constructor
    def this(name: String) = this(name, 0)

  }
  // Constructor Chaining
  class Adult(name: String, age: Int, idcard: String) extends Person(name, age)
  //-----------------------------------------------------------------------------------------------------------------------------
  // overriding class fields different signatures
  class Dog extends Animal {
    override val creatureType = "domestic"
    override def eat = println("crunch--crunch")
  }
  //or
  class Monkey(monkeyType: String) extends Animal {
    override val creatureType = monkeyType

  }
  // or we can override class feilds in this way

  class Bird(override val creatureType: String) extends Animal {
    override def eat = {
      super.eat
      println("quaw--quaw")
    }
  }
  val dog = new Dog
  dog.eat

  //dynamic Binding
  val unknownanimal: Animal = new Bird("k9")
  unknownanimal.eat
  // super keyword
  //preventing override use final keyword to prevent overriding in scala
  // seal the class-this will allow extends this class in this file but will not allow in other file
  
    class Dinasour(animal:Animal) extends Animal {
    override val creatureType ="Dina"

  }

  
}