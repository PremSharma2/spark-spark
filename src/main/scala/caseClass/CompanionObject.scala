package caseClass

/*
 * A companion object is an object with the same name as a class or trait and is defined in the same source file as the associated file
 * or trait.
 * A companion object differs from other objects as it has access rights to the class/trait that other objects do not.
 * In particular it can access methods and fields that are private in the class/trait.

An analog to a companion object in Java is having a class with static methods.
In Scala you would move the static methods to a Companion object.

One of the most common uses of a companion object is to define factory methods for class.
An example is case-classes. When a case-class is declared a companion object is created for the case-class with a factory
method that has the same signature as the primary constructor of the case class.
That is why one can create a case-class like: MyCaseClass(param1, param2). No new element is required for case-class instantiation.

A second common use-case for companion objects is to create extractors for the class.
I will mention extractors in a future topic. Basically extractors allow matching to work with arbitrary classes.

NOTE: Because the companion object and the class must be defined in the same source file you cannot create them in the interpreter.
So copy the following example into a file and run it in script mode:
 *
 *
 */
object CompanionObject extends App {

  class MyString(val jString: String) {
    private var extraData = ""
    override def toString = jString + extraData
  }
//companion Object
  object MyString {
    /*
     * Constructor of Factory Pattern for MyString class
     */
    def apply(base: String, extras: String) = {
      val s = new MyString(base)
      s.extraData = extras

    }
    def apply(base: String) = new MyString(base)
    /*
     * extractor pattern for Mystringclass 
     * using pattern matching
     */
    def unapply(p: MyString): Option[(String, String)] = p match {
      case MyString(x, y) => Some(x, y)

    }
  }

  println(MyString("hello", "world"))
  println(MyString("hello"))

}