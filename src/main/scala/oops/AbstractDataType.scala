package oops

object AbstractDataType extends App {

  abstract class Animal {

    val creatureType: String
    def eat: Unit
  }

  class Dog extends Animal {

    override val creatureType: String = "Cannie"
    override def eat = println("crunch crunch")
  }
  //trait an Abstract data type represents only behaviour
  trait Carnivore {

    def eat(animal: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override val creatureType: String = "Croc"
    override def eat: Unit = println("nom-nom-nom-nom-")
    override def eat(animal: Animal): Unit = println(s"I am a ${this.creatureType} and I am eating ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat(dog)
}