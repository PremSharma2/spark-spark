package oops

object TraitVsAbstarctclass  extends App{
  
  //You want to create a base class that requires constructor arguments then go for Abstract Class
  //The above reason is pretty straightforward - trait doesn’t allow for constructor arguments.
  //Therefore, if you want to do something like the next example, you will have to use abstract class.
  abstract class Animal ( name: String) { // trait doesn't allow this
  var age: Int = 0 // concrete
  val hello: String // abstract
  val greeting: String = s"I like to play with you!" // concrete
  def sayHello:Unit =  println(hello) 
  override def toString = s"$hello, $greeting"
}

class Dog (name: String) extends Animal (name) {
  override val hello = "Woof"
  override val greeting: String = s"I am $name and I like to play with you!"
}


/*
 * Besides the points above, trait in general is more powerful and can be used in many cases.
 *  A class can extend multiple trait, but only one abstract class. 
 *  Moreover, trait can be combined together with abstract class to take advantage of its constructor parameter. 
 * These all make trait very flexible and more commonly 	used to implement base behavior.
 * 
 * 
 * //You want to create a base class that requires constructor arguments.then go for abstract class else trait
  //we can say that Scala trait is a partially implemented interface. We can use it to share fields and interfaces between classes
  // traits vs abstract class: traits and abstract class both can have the abstract and non abstract methods and fields
  //traits do not have constructor parameter
  // multiple traits may be inherited by the same class
  //trait is for behavior and abstract class is for thing
  //Although Scala has abstract classes, it’s much more common to use traits than abstract classes to implement base behavior. 
  //A class can extend only one   class, but it can implement multiple traits, so using traits is more flexible.
  //To make sure a trait named MyTrait can only be mixed into a class that is a subclass of a type named BaseType, 
  //begin your trait with a this: BaseType => declaration, as shown here:

/*trait MyTrait {
  this: BaseType =>
  
}*/
 * 
 * 
 */

trait Angry {
  var isAngry: Boolean
}

trait BallLover {
  def catchBall:Unit =  println("catch the ball!") 
}

class Croc ( val name: String) extends Animal (name) with Angry with BallLover {
  val hello = "Woof"
  override  var isAngry = true
  override val greeting: String = s"I am $name and I like to play with you!"
}

val tom = new Croc("Tom")
println(tom.name)
println(tom.greeting)
println(tom.isAngry)
println(tom.catchBall)

}