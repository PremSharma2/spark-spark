package ImplicitsAndTypeClasses
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
   Value classes are mostly used for performance optimization and memory optimization.
   You can think of many of these classes as your Scala typical primitive,
   like classes Int, Boolean,  Double, etc.
   Use cases where you would want to and where you could apply value classes is for tiny types.
    Let’s look at a couple of examples of how that can be applied.
      case class UserId(id: Int)
case class Username(name: String)
case class TweetId(id: Int)
case class TweetContent(content: String)
case class User(id: UserId, name: Username)
case class Tweet(id: TweetId, content: TweetContent)

Now, what happens when I used the concept of Java types to use more specific
classes for the user ID and username? This means that,
now, I can’t use any number directly and can’t mix a tweet ID with a user ID
because the compiler is helping us to verify that. So,
that is why we created four classes here to wrap around our very simple Int and String values.
So, User and Tweet takes tiny types instead of Int or String values directly.
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
  // here to is also method of scala RichInt implicit class method
  // it will return a seq of 1......10 numbers in it
  val seq: Seq[Int] =1 to 10
  val seq2: Seq[Int] =1.to(10)


  // or one more example of scala implicit class and pimping is scala duration

  import scala.concurrent.duration._
  val seconds: FiniteDuration =3.seconds

  // One imp point compiler does not do implicit searches
  implicit class RicherInt(richInt:RichInt){
    def isOdd:Boolean = richInt.value%2!=0
  }
// so we cant call like 42.isOdd
  // because compiler initially when search for value implicit class for
  //42 it will find RichInt but you are asking on that RichInt search for one more implicit value class
  // that is not possible i.e you are asking search for implicit value class for value implicit class


}
