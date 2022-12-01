package oops

object InheritanceInScala extends App {

  class Animal {
    val creatureType = "wild-animal"

    def eat = "nom-nom-nom"
  }

  /**
   * def in super class can be overridden as val
   * eat is defined in super class as def
   */
  class Cat(override val eat: String) extends Animal

  val cat = new Cat("nom-mon")
  cat.eat

  // constructors
  class Person(name: String, age: Int) {
    //auxillary constructor
    def this(name: String) = this(name, 0)

    def breath: String = "breath"

  }

  val person = new Person("prem", 20)

  // Constructor Chaining
  class Adult(name: String, age: Int, idcard: String) extends Person(name, age)

  //-----------------------------------------------------------------------------------------------------------------------------
  // overriding class fields different signatures
  class Dog extends Animal {
    override val creatureType = "domestic"

    override val eat = "crunch--crunch"
  }

  //or
  class Monkey(monkeyType: String) extends Animal {
    override val creatureType: String = monkeyType

  }

  // or we can override class fields in this way
  //here it is clear that def can be over ridden as val
  class Bird(override val creatureType: String) extends Animal {
    override val eat: String = {
      super.eat
    }
  }

  val dog = new Dog
  dog.eat

  // def can be overridden as val overide eyword not necesaary
  abstract class Pet(name: String) {
    def greeting: String
  }

  class Dog1(name: String) extends Pet(name) {
    val greeting = "Woof"
  }

  //dynamic Binding
  val unknownanimal: Animal = new Bird("k9")
  unknownanimal.eat

  // super keyword
  //preventing override use final keyword to prevent overriding in scala
  // seal the class-this will allow extends this class in this file but will not allow in other file

  class Dinasour(animal: Animal) extends Animal {
    override val creatureType = "Dina"

  }


}