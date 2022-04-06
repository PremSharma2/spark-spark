package oops

object AbstractDataType{

  abstract class Animal {

    val creatureType: String
    def eat: Unit
    def beep:String = "beep"
  }

  class Dog extends Animal {
// todo : -> This is everything inside constructor
    override val creatureType: String = "Cannie"
    override def eat = println("crunch crunch")

// todo def can be overridden as val instance property
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
  /*
  TODO
    “A def can be implemented by either of a def, a val, a lazy val, or an object.
    So it’s the most abstract form of defining a member.
    Since traits are usually abstract interfaces,
    saying you want a val is saying how the implementation should do.”
   */
  trait Foo1 {
    def id: Int
    protected val b=2
  }

  class Bar1 extends Foo1 {
    val id = 1
    override protected val b=4
  }
  // we can override def as var/val as constructor argument as well
  // but make sure that method in trait shd be accesor only
  class Bar(override var foo: String) extends Foo {
  }

  val dog = new Dog
  dog.creatureType // its is
  val croc = new Crocodile
  val bar = new Bar("foo")
  bar.foo // this is assessor method
  bar.foo="hi" // this is update method
  // TODO : Exercise
/*
  the trait has a member plumage,
  which the compiler knows it will be overridden in GoldFinch,
   so at the time colours is initialized, plumage is null (in the bytecode)
if you remove the colours member and just keep plumage, it will work
t's null because the runtime knows it's going to be overwritten -
otherwise the runtime will just take that initial value
TODO
 Got it - that's because of the order of initialization.
 Your printColours method uses the colours value defined in the trait,
 which is null because you've overridden it in the object Goldfinch.
 because colors is defined in terms of plumage which you've overridden in the object
 i.e colors and plumage goes hand in hand
 if you comment both, then it works
 because plumage and colours are non-null
 Also the trait has a colours field and the object has a different colours field
 if you override them, then the colours field from the trait is null at initialization because you've overridden it
 Solution :
 change them to defs and they will work if you uncomment your code
 or override everything in sub class
TODO:Best practices
  almost - best practice is to have
 defs in traits, except constants i.e all vals which you aren't going to override (they should stay in companion objects anyway)
 override defs with either defs or vals (depending on what you're doing,
 sometimes your logic will make it impossible to override with val)
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
    override protected val colours: Seq[String] = plumage.colours
    //TODO still it will not work because
  }

  //TODO better Design

  sealed trait Bird1 {
    // We cannot assign a value the variables in trait in scala
    protected def plumage: Plumage = Plumage("Yellow", "Red", "White", "Black")
    protected def colours: Seq[String]  = plumage.colours
    def printColours(): Unit = {
      println(s"Bird colours: [${colours.mkString("/")}]")
    }
  }
  case object Kingfisher1 extends Ordering[String] with  Bird1 {
    override def compare(x: String, y: String): Int = -1

    override protected val plumage = Plumage("Green", "Orange")
    //override protected val colours: Seq[String] = plumage.colours
  }
  case object Goldfinch1 extends Ordering[String] with  Bird1 {
    override def compare(x: String, y: String): Int = throw new NullPointerException
    override protected val plumage = Plumage("Yellow", "Red", "White", "Black")
    override protected val colours: Seq[String] = plumage.colours
    //TODO still it will not work because
  }
  def main(args: Array[String]): Unit = {
    //croc.eat(dog)
  // Kingfisher.printColours()
   // Kingfisher1.printColours()
   Goldfinch.printColours()
    //Goldfinch1.printColours() // this will work
  }

}