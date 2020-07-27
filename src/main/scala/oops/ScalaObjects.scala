package oops

object ScalaObjects extends App {

  // scala does not have class level functionality ("scala does not know the concept of static")
// there is only single instance of the Type Person we refer to with the name Person
  object Person { //type + its only instance
    //"static or class - level functionality 
    //static constant
    // All helper utilities goes here
    // all factory goes here
    val N_EYES = 2
    def vanFly(): Boolean = true
    //factory method 
    def apply(mother:Person, father:Person):Person= new Person("Bobie")
  }
  class Person(name:String) {
    //instance level functionality
  }
// This Pattern of writing class and object of same type in same scope is called Companion
  //Person class and person object
  println(Person.N_EYES)
  println(Person.vanFly())
  
  //scala object=Singleton Instance  
  //scala instances are singleton by defination no extra code needed from you to make it singleton
  val mary1=Person
  //val john=Person
  val  mary=new Person("Mary")
  val  john=new Person("John")
  println(mary==john)
  val bobie=Person.apply(mary, john)
  //or calling via apply factory method 
  
  val bob=Person(mary,john)
  // scala Application is scala object with 
  //def main(args:Array[String]):Unit

}