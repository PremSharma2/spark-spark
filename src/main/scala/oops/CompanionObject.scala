package oops

/**
 * TODO
 * A companion object is an object with the same name as a class or trait and is defined
 * in the same source file as the associated file
 * or trait.
 * A companion object differs from other objects as
 * it has access rights to the class/trait that other objects do not.
 * In particular it can access methods
 * and fields that are private in the class/trait even private Constructor.
 *
 *
 * TODO
 * An analog to a companion object in Java is having a class with static methods.
 * In Scala you would move the static methods to a Companion object.
 *
 * One of the most common uses of a companion object is to define factory methods for class.
 * An example is case-classes. When a case-class is declared a companion object is created for the case-class with a factory
 * method that has the same signature as the primary constructor of the case class.
 * That is why one can create a case-class like: MyCaseClass(param1, param2). No new element is required for case-class instantiation.
 *
 * A second common use-case for companion objects is to create extractors for the class.
 * I will mention extractors in a future topic. Basically extractors allow matching to work with arbitrary classes.
 *
 * NOTE: Because the companion object and the class must be defined in the same source file and in same scope
 * you cannot create them in the interpreter.
 * So copy the following example into a file and run it in script mode
 *
 *
 */
object CompanionObject extends App {

  class MyString(val jString: String) {
    private var extraData = ""

    override def toString = jString + extraData
  }


  /**
   * this is companion object for Mystring class
   */

  object MyString {
    /**
     *  todo : all static fields or class level attributes goes here
     */

    val staticField = 22


    /**
     * TODO
     * Constructor of Factory Pattern for MyString  class
     */
    def apply(base: String, extras: String): MyString = {
      val s = new MyString(base)
      //todo : companion can  access  private field/members of class
      s.extraData = extras
      s
    }

    def apply(base: String) = new MyString(base)

    /**
     * extractor pattern for Mystringclass 
     * using pattern matching
     */

    def unapply(p: MyString): Option[(String, String)] =
      Some(p.jString, p.extraData)


    val mystring = MyString("hello", "world")
    println(MyString("hello", "world"))
    println(MyString("hello"))
    println(MyString.unapply(MyString.apply("Tom", "Jerry")).getOrElse(Some("tom", "jerry")))

    mystring match {
      case MyString(str, str1) => s" firstName:-> ${str} and lastName-> ${str1}"

    }


    //todo scala singleton pattern using scala singleton objects
    class Brain private {
      override def toString = "This is the brain."
    }

    object Brain {
      val brain = new Brain

      def apply: Brain = {
        brain
      }
    }

    //TODO Companion object more examples
    // TODO this worls like helper or utility
    object Clusterconfiguration {
      val MAX_NODES = 20

      def getNumberOfNodes = {
        42
      }
    }

    val max = Clusterconfiguration.MAX_NODES

    //TODO here class + object = companion object
    // instance level logic goes here
    class Kid(name: String, age: Int) {
      def greet(): String = s"Hello, my name is $name and I am $age  years old ,Do I like vegetables ${Kid.LIKES_VEGETABLES}   "
    }

    /**
     * class level or static logic goes here
     * in short companion objects are for static fields and methods
     *
     * */

    object Kid { // all class level or static stuff will go here
      //TODO: ->  its like static boolean LIKES_VEGETABLES = false
      private val LIKES_VEGETABLES = false


    }


  }
}