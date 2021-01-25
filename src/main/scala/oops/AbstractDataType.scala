package oops

object AbstractDataType{

  abstract class Animal {

    val creatureType: String
    def eat: Unit
    def beep:String = "beep"
  }

  class Dog extends Animal {

    override val creatureType: String = "Cannie"
    override def eat = println("crunch crunch")

    override val beep  = super.beep
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
//TODO Using scala constructor to set variable defined in trait
  /*
  TODO
     An abstract var field results in getter and setter methods being generated
     for the field.
     An abstract val field results in a getter method being generated for the field.
     if you use var,
     then the compiler automatically detects the modifier method
     (because  you can change a var)
    Otherwise you'll need to implement that method because you can't change a val
    And foo_= is a method
    with a special structure which then allows you to say bar.foo = "a string",
     which the compiler translates to bar.foo_=("a string")
   */
  trait Foo {
    def foo: String
    def foo_= (s: String): Unit
  }
  class Bar(override var foo: String) extends Foo {
  }

  val dog = new Dog
  val croc = new Crocodile

  // TODO : Exercise
/*
  the trait has a member plumage,
  which the compiler knows it will be overridden in GoldFinch,
   so at the time colours is initialized, plumage is null (in the bytecode)
if you remove the colours member and just keep plumage, it will work
t's null because the runtime knows it's going to be overwritten -
otherwise the runtime will just take that initial value
 */
  case class Plumage(colours: String*)
  sealed trait Bird {
    // We cannot assign a value the variables in trait in scala
    protected val plumage: Plumage = Plumage("Yellow", "Red", "White", "Black")
    protected val colours: Seq[String]  = plumage.colours
    def printColours(): Unit = {
      println(s"Bird colours: [${colours.mkString("/")}]")
    }
  }
  case object Kingfisher extends Ordering[String] with  Bird {
    override def compare(x: String, y: String): Int = -1

    //override protected val plumage = Plumage("Green", "Orange")
    //override protected val colours: Seq[String] = plumage.colours
  }
  case object Goldfinch extends Ordering[String] with  Bird {
    override def compare(x: String, y: String): Int = throw new NullPointerException
    override protected val plumage = Plumage("Yellow", "Red", "White", "Black")
    //override protected val colours: Seq[String] = plumage.colours
  }

  def main(args: Array[String]): Unit = {
    //croc.eat(dog)
   Goldfinch.printColours()
  }

}