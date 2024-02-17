package oops

object ScalaObjects extends App {
  /**
   *  A Singleton object is an object which defines a single object of a class.
   *  A singleton object provides an entry point to your program execution.
   *
   *  scala does not have class level functionality ("scala does not know the concept of static")
   *  there is only single instance of the Type Person we refer to with the name Person
   */

  object Person { //type + its only instance
    //"static or class - level functionality 
    //static constant
    // All helper utilities goes here
    // all factory goes here
    val N_EYES = 2 //todo static constants
    def vanFly(): Boolean = true
    //factory method for Person class
    def apply(mother:Person, father:Person):Person= new Person("Bobie")

  }

  class Person(name:String) {
    //instance level functionality
    def print= println(Person.N_EYES)
  }
// This Pattern of writing class and object of same type in same scope is called Companion
  //Person class and person object
  println(Person.N_EYES)
  println(Person.vanFly())
  
  //scala object=Singleton Instance  
  //scala instances are singleton by definition
  // no extra code needed from you to make it singleton
  val mary1=Person
  //val john=Person
  val  mary=new Person("Mary")
  val  john=new Person("John")
  println(mary==john)
  val bobie=Person.apply(mary, john)
  //or calling via apply factory method 
  
  val bob=Person(mary,john)

/*
TODO
  UTILITY CLASSES
  Depending on your needs,
  creating a private class constructor may not be necessary at all.
 For instance, in Java you’d create a file utilities class by
  defining static methods in a Java class,
 but in Scala you do the same thing by putting all the methods in a Scala object:


 */
object FileUtils {

  def readFile(filename: String) = {
    // code here ...
  }

  def writeToFile(filename: String, contents: String) {
    // code here ...
  }
}
}