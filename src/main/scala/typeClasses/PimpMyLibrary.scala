package typeClasses
// implcit classes are used to implement this pimp my library feature
object PimpMyLibrary  extends App {
  // implicit class takes only one argument and this is also value class
  /*
  As you can see in the below section, for a class to be a value class,
  it must have exactly one parameter and have nothing inside — except defs.
  Furthermore, no other class can extend a value class,
  and a value class cannot redefine equals or hashCode.
  To define a value class, make it a subclass of  AnyVal and put the val keyword before the one parameter.
  This is how you can recognize or create value classes in Scala
 Fundamentally, a value class is one that wraps around a very simple type or a simple value, like :
 Int,  Boolean, etc. What that means is that, at compile time,
 you see the value class and use the value class, but at the time bytecode could get generated,
 you are actually using the underlying simple type. So, that means that your instances of the wrapper classes
     creator, which means less initialization,
   increase performance and create less memory usage, because there is no instance of the wrapper classes.
   */
 implicit class RichInt(val value :Int) extends AnyVal {
    def isEven:Boolean= value%2==0
    def squareRoot:Double = Math.sqrt(value)
  }


  println(new RichInt(2).squareRoot)
  // but as this is an implicit class we also can write that as follows
   println(42.isEven)
  // This is called type enrichment or pimping
  // here compiler rewrite the code like this   new RichInt(2).isEven


  // for example scala also uses this feature
  // here to is aslo method of scala RichInt implicit calss method
  // it will return a seq of 1......10 numbers in it
  1 to 10


  // or one more example of scala implicit class and pimping is scala duration

  import scala.concurrent.duration._
  3.seconds

}
