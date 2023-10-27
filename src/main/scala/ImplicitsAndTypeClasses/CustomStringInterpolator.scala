package ImplicitsAndTypeClasses

import org.apache.spark.sql.SparkSession

object CustomStringInterpolator {

  import org.apache.spark

  /**
   * TODO
      *You’re surely well aware of the standard Scala string interpolators.
      *They allow us to inject values and even whole expressions into a string.
      *The best known and most used interpolator is the S interpolator,
      *which simply expands values or expressions inside a string.
 */

val lifeOfPi = 3.14159
  val sInterpolator = s"The value of pi is $lifeOfPi. Half of pi is ${lifeOfPi / 2}"


  /**
 TODO
    we have the Raw interpolator,
    which is the same as the S interpolator,
    except that it doesn’t escape characters,
    but keeps them exactly as they are:
   *
   */

  val rawIterpolator = raw"The value of pi is $lifeOfPi\n <-- this is not a newline"

/*
TODO
        In this example, we're defining a regular expression
        pattern for a date in the format dd-mm-yyyy.
        The raw interpolator is used to create
        a raw string so that
        we don't have to escape
        the backslashes in the regular expression.
 */
  val regex = raw"\d{2}-\d{2}-\d{4}".r

/*
TODO
    In this scenario, when working with file paths in Scala,
   the raw interpolator can be used to create
    a string without having to escape
    the backslashes or worry about special characters.
 */
  val filePath = raw"/home/user/documents/file.txt"

  //refcatoring query use raw interpolator
  val complexQuery = raw"""SELECT * FROM products WHERE category = 'Electronics' AND manufacturer = 'XYZ Corp' AND description LIKE '%\\_\\_%' ESCAPE '\\'"""


  /**
   * TODO
          we have the F interpolator,
          which has the ability to control the format
          in which values are shown.
          It has similar functionality to standard printf,
          such as controlling the number of decimals in a number:
   */
  val fInterpolator = f"The approximate value of pi is $lifeOfPi%3.2f"
  //The approximate value of pi is 3.14

  import java.time.LocalDateTime

  val logLevel = "INFO"
  val message = "Processing data invoked!!"
  val timestamp = LocalDateTime.now()

/**
 * TODO
      we're creating a log message with a timestamp,
      log level, and a message.
      The f interpolator allows us to format
      the timestamp as a string
      and embed it directly into the log message.
      The $timestamp is formatted based on the formatting string [$timestamp].
 */
  val logMessage = f"[$timestamp] [$logLevel]: $message"
  val logMessage1 = s"[$timestamp] [$logLevel]: $message"
  //[2023-10-13T14:30:00] [INFO]: Processing data



//SPark also has some interpolator for eg $ interpolator
//val spark=SparkSession.builder().getOrCreate()
  //import spark.implicits._

  /*
  implicit class StringToColumn(val sc: StringContext) {
    def $(args: Any*): ColumnName = {
      new ColumnName(sc.s(args: _*))
    }
  }
   */
  //spark has this custom interpolator

 // val aColumn=$"columnName"

  //Exercise how to write custom interpolator

  //imagine you are using the following case class A LOT in your library:
  //and you are doing a lot of parsing from strings
  // in the form of “name,age” into instances of this Person class

  case class Person(name: String, age: Int)
//normal way of parsing a text file into Person object
  def stringToPerson(line: String): Person = {
    // assume the strings are always "name,age"
    val tokens = line.split(",")
    Person(tokens(0), tokens(1).toInt)
  }

  val bob = stringToPerson("Bob,55")
  // and you're calling stringToPerson everywhere

  //I’ll show you how you can create an interpolator so you can write

 // val bob = person"Bob,55"

  /*
     TODO
       The Mechanics
        A custom interpolator needs only two things:
         an implicit wrapper over a special class called StringContext,
          and a method whose name is identical
          to the name of the interpolator you want to create.
           For “person”, the method name needs to be “person”.
           The method “person” needs to take Any* as argument:
            these are all the expressions you can inject into a string.
            Let me explain. When you write
        TODO
            s"The value of pi is $lifeOfPi. Half of pi is ${lifeOfPi / 2}"
            The value of pi is. Half of pi is //is part
            $lifeOfPi //Expression
            s"The value of pi is $lifeOfPi. Half of pi is ${lifeOfPi / 2}"
            The values you expand with the dollar sign are called arguments,
            and can be of any type (hence the type Any),
            while the pieces of string in between
            the arguments are called parts and
            you can access them by sc.parts.
            In the method “person”, you have access to both,
            so you can process them as you see fit.
            I’m just going to concatenate them all,
            and parse the Person from the resulting String:
   */


  implicit class PersonInterpolator1(sc: StringContext) {
    def mys(args: Any*): Person = {
      // logic here
      val parts: Seq[String] = sc.parts
      //injecting the args into the String
      val totalString: String = sc.s(args:_*)
      val tokens=totalString.split(",")
      Person(tokens(0), tokens(1).toInt )
    }
  }

  implicit class PersonInterpolator(sc: StringContext) {
    def person(args: Any*): Person = {
      // concatenate everything: use the built-in S method (which happens to be used in the S interpolator)
      val tokens = sc.s(args: _*).split(",")
      Person(tokens(0), tokens(1).toInt)
    }
  }
  def main(args: Array[String]): Unit = {
    println(sInterpolator)
    val name = "Bob"
    val age = 23
    val bob = person"$name,$age"
    println(bob)
  }


}
