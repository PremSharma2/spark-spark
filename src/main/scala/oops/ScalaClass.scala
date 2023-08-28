package oops

object ScalaClass {


 // #todo : a basic class this single line works like java multiple line code
  //todo: which will create the constructor here basically and setter getter
  class Person(var name: String, var age: Int)

//  # use the class
  val al = new Person("Al", 42)
  al.name                        // "Al"
  al.age                        // 42 it is getter age
  al.age=45                     // it is a setter method in person class
  //A bit more real-world:

  //TODO: ->  define the class with immutable field values there will no setters
  class Person1(val firstName: String, val lastName: String) {

    println("the constructor begins")
    val fullName = firstName + " " + lastName

    val HOME = System.getProperty("user.home")

    // define some methods
    def foo { println("foo") }
    def printFullName {
      // access the fullName field, which is created above
      println(fullName)
    }
//TODO-----constructor again begins--------------------
    printFullName
    println("still in the constructor")

  }



 // The visibility of class parameters
 // A look at the visibility of class parameters:

  // 'name' is a var
  class Emp(var name: String)
  val e = new Emp("Alvin Alexander")
  e.name                                  // String = Alvin Alexander
  e.name = "Fred Flintstone"              // String = Fred Flintstone

  // 'name' is a val
  class Emp1(val name: String)
  val e1 = new Emp1("Alvin Alexander")
  e1.name                                  // String = Alvin Alexander
  //p.name = "Fred Flintstone"              // error: reassignment to val

  // 'name' is neither var or val
  /**
   If a field doesn’t have a var or val modifier,
    Scala gets conservative, and doesn’t generate a getter or setter method for the field.
    so no getter is there thats why its complaning
    because name is class parameter not fild
   */
  class User(name: String)
  val u = new User("Alvin Alexander")

 // u.name                                  // error: value name is not a member of Person

  // 'name' is 'private var'
  class Staff(private var name: String) { def printName {println(name)}  }

  val s = new Staff("Alvin Alexander")
  //s.name       //TODO : ->  error: variable name in class Staff cannot be accessed here
  s.printName  // Alvin Alexander
  /*
  What var, val, and default mean when applied to class parameters:

    Declaration    Getter?    Setter?
  -----------    -------    -------
  var            yes        yes
  val            yes        no
  default        no         no
  Auxiliary constructors
    Define auxiliary constructors using this
     as the name for each auxiliary constructor.
     Make sure you call a previously defined constructor:


   */
  class Pizza {

    var crustSize = 12
    var crustType = "Thin"

    def this(crustSize: Int) {
      this()
      this.crustSize = crustSize
    }

    def this(crustSize: Int, crustType: String) {
      this(crustSize)
      this.crustType = crustType
    }

    override def toString = {
      "A %s inch pizza with %s crust.".format(crustSize, crustType)
    }

  }
 // That class defines three constructors, which can be called like this:

  object AuxiliaryConstructors extends App {
    println(new Pizza)
    println(new Pizza(14))
    println(new Pizza(16, "Thick"))
  }
  /*
  Running this object results in the following output:

    A 12 inch pizza with Thin crust.
  A 14 inch pizza with Thin crust.
  A 16 inch pizza with Thick crust.
  Private constructor
    A simple way to enforce the Singleton pattern in Scala is to
     make the primary constructor private,
     then put a getInstance method in the companion object of the class:


   */

  /**
   * Singleton pattern in scala is implemented by making a private constructor
   * and that the constructor can be accessed from the companion object
   */
  class Brain private {
    override def toString = "This is the brain."
  }
//todo Factory or Companion Object
  object Brain {
    val brain = new Brain
    def getInstance: Brain = {
      brain
    }
  }

  // we cant do that  val brain=new Brain
  object SingletonTest extends App {

    // this won't work
    // val brain = new Brain

    // this works A PERFECT SINGLETONDESIGN PATTERN
    val brain = Brain.getInstance
    println(brain)
  }

/*
  Default values for constructor parameters
    You can provide default values for constructor parameters:


 */
  class Socket (var timeout: Int = 10000)

  // create instance without a param, get the default value
  val sk = new Socket
  //sk.timeout            // Int = 10000

  // specify your own timeout value
  val sk1 = new Socket(5000)
    sk1.timeout            // Int = 5000
 // When to use an abstract class
//  Why use an “abstract class”? It can have constructor parameters, but a trait cannot.

 //   Traits don’t allow constructor parameters:

  // this won't compile
 // trait Animal(name: String)

  // so use an abstract class
 // abstract class Animal(name: String)
 // In the following example the methods save, update, and delete are defined in the abstract class BaseController,
  // and the method connect is undefined, and therefore abstract:
/*
  abstract class BaseController(db: Database) {
    def save { db.save }
    def update { db.update }
    def delete { db.delete }
    def connect                             // abstract since it has no implementation
    def getStatus: String                   // an abstract method that returns a String
    def setServerName(serverName: String)   // an abstract method that takes a parameter
  }
  JavaBean classes
    To create JavaBean classes, use the @BeanProperty annotation on your fields, also making sure to declare the fields as the type var. The @BeanProperty annotation can be used in a constructor:

  import scala.reflect.BeanProperty

  class Person(@BeanProperty var firstName: String,
               @BeanProperty var lastName: String,
               @BeanProperty var age: Int) {
    // more code here ...
  }
 // It can also be used on fields in a class:

  //import scala.reflect.BeanProperty

  class EmailAccount {
    @BeanProperty var accountName: String = null
    @BeanProperty var username: String = null
    @BeanProperty var password: String = null
    @BeanProperty var mailbox: String = null
    @BeanProperty var imapServerUrl: String = null
    @BeanProperty var minutesBetweenChecks: Int = 0
    @BeanProperty var protocol: String = null
    @BeanProperty var usersOfInterest = new java.util.ArrayList[String]()
  }


  Scala case classes
  You can generate a lot of useful boilerplate code with a case class:

  case class Person(var name: String, var age: Int)
  With the special case class incantation, Scala generates a great deal of code for you:

    toString, equals, and hashCode methods.
    The constructor parameters you specify become properties with getters and setters.
    You no longer have to use ’new’ to create an instance of your class.
  You can use convenient extractors in match/case statements.
    To see the code that Scala generates for you, first compile a simple class, then disassemble it with javap. First, put this code in a file named Person.scala:

  case class Person(var name: String, var age: Int)
  Then compile the file:
/*
    $ scalac Person.scala
  This creates two class files, Person.class and Person$.class. Disassemble Person.class to see its signature:

    $  javap Person
  Compiled from "Person.scala"
  public class Person extends java.lang.Object implements scala.ScalaObject,scala.Product,scala.Serializable{
    public static final scala.Function1 tupled();
    public static final scala.Function1 curry();
    public static final scala.Function1 curried();
    public scala.collection.Iterator productIterator();
    public scala.collection.Iterator productElements();
    public java.lang.String name();
    public void name_$eq(java.lang.String);
    public int age();
    public void age_$eq(int);
    public Person copy(java.lang.String, int);
    public int copy$default$2();
    public java.lang.String copy$default$1();
    public int hashCode();
    public java.lang.String toString();
    public boolean equals(java.lang.Object);
    public java.lang.String productPrefix();
    public int productArity();
    public java.lang.Object productElement(int);
    public boolean canEqual(java.lang.Object);
    public Person(java.lang.String, int);
  }
  Also disassemble Person$.class to see its signature:

    $  javap Person$
  Compiled from "Person.scala"
  public final class Person$ extends scala.runtime.AbstractFunction2 implements scala.ScalaObject,scala.Serializable{
    public static final Person$ MODULE$;
    public static {};
    public final java.lang.String toString();
    public scala.Option unapply(Person);
    public Person apply(java.lang.String, int);
    public java.lang.Object readResolve();
    public java.lang.Object apply(java.lang.Object, java.lang.Object);
  }
  The protected keyword
  The protected keyword:

    the protected keyword in scala has a different meaning than in java
  protected means current class and subclasses can access member
  unlike java, other classes in package can’t access member
  Example:

   */





 */
 /*
  package foo {

    class Foo {
      // (1) make 'getFoo' protected.
      protected def getFoo = "foo"
    }

    class FooSub extends Foo {
      def doFoo = {
        // (2) we can access getFoo because we're a subclass of Foo
        val x = getFoo
      }
    }
    class Bar {
      def doBar = {
        val f = new Foo
        // (3) this line won't compile because getFoo is protected
        //f.getFoo
      }
    }

  }

  package baz {

    import foo.Foo
    class Baz {
      def doBaz = {
        val f = new Foo
        // (4) this won't work either
        //f.getFoo
      }
    }

  }


  */
}
