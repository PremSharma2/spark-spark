package oops

import scala.io.Source

object OOBasics extends App {
  /*val person=new Person("john",26)
  println(person.age)
  println(person.greet("Kaushik"))
  */
  
  val author=new Writer("Prem","kaushik",1986)
  author.year
  val novel=new Novel( "bounce-back",2019,author)
  println(novel author_age)
  println(novel isWrittenBy author)

  val counter= new Immutability_Basics
  counter.inc.print
  //incrementing three times
  counter.inc.inc.inc.print
  val counter1=new Immutability_Basics(0)
  counter1.inc(10).print

  class Stock {
    // a private[this] var is object-private, and can only be seen
    // by the current instance who owns the feild
    private[this] var price: Double = _

    def setPrice(p: Double) { this.price = p }

    // error: this method won't compile because price is now object-private
    //def isHigher(that: Stock): Boolean = this.price > that.price
  }
/*
TODO
    In the following example,
   the field text is set equal to a block of code,
  which either returns (a) the text contained in a file,
  or (b) an error message, depending on whether the file exists and can be read:
  Because the assignment of the code block to the text field
  and the println statement are both in the body of the Foo class,
  they are in the class’s constructor,
  and will be executed when a new instance of the class is created.
  Therefore, compiling and running this example
  will either print the contents of the file,
  or the “Error happened” message from the catch block.
 */
  class Foo {

    // set 'text' equal to the result of the block of code
    val text: String = {
      var lines = ""
      try {
        lines = Source.fromFile("/etc/passwd").getLines.mkString
      } catch {
        case e: Exception => lines = "Error happened"
      }
      lines
    }

    println(text)
  }

  object Test extends App {
    val f = new Foo
  }
//TODO In a similar way, you can assign a class field to the results of a method or function:

  class Foo1 {
    import scala.xml.XML

    // assign the xml field to the result of the load method
    val xml = XML.load("http://example.com/foo.xml")

    // more code here like def etc.. ...
  }
// smart assignment
  class Foo2 {
    lazy val text =
      Source.fromFile("/etc/passwd").getLines.mkString
  }

  object Test2 extends App {
    val f = new Foo
  }
  //TODO  Setting Uninitialized var Field Types
  // TODO You want to set the type for an uninitialized var field in a class, so you begin to write code like this:
  //
  //var x =
  /*
TODO
    In general, define the field as an Option.
    For certain types, such as String and numeric fields,
    you can specify default initial values.
   For instance, imagine that you’re starting a social network,
  and to encourage people to sign up,
  you only ask for a username and password during the registration process.
 Therefore, you define username and password as fields in your class constructor:
   */

  case class Person(var username: String, var password: String)
  /*
TODO
 However, later on,
  you’ll also want to get other information from users,
  including their age, first name, last name, and address. Declaring those first three var fields is simple:

var age = 0
var firstName = ""
var lastName = ""
   */
  /*
TODO
   But what do you do when you get to the address?
  The solution is to define the address field as an Option, as shown here
   */
  case class Person1(var username: String, var password: String) {
    var age = 0
    var firstName = ""
    var lastName = ""
    var address = None: Option[Address]

  }

  case class Address(city: String, state: String, zip: String)
}