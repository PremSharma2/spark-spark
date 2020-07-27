package caseClass

object CaseClass extends App {
  /*
  • An apply method is generated inside companion object, so you don’t need to use the new keyword to create
    a new instance of the class.
  • Accessor methods are generated for the constructor parameters because case class
    constructor parameters are val by default. Mutator methods are also generated for
    parameters declared as var.
  • A good, default toString method is generated.
  • An unapply method is generated, making it easy to use case classes in match ex‐
    pressions.
  • equals and hashCode methods are generated.
  • A copy method is generated.
   * 
   * 
   * 
   * 
   */
  
  case class Person(name:String,age:Int)
  //class parameters are promoted to feilds automaticaly in case of case classes ,
  //case classses are simple plain objects used for state repersentation
  // case class have sensible toString Representation as it is needed bcz it is simple pojo 
  // equals and hashcode are implemented out of the box
  val jim1=  new Person("Jim",34)
  val jim2= new Person("Jim",34)
  println(jim1.name)
  println(jim1==jim2)
  // case classes have apply method
  val brud= Person("brud",36)
  // case Classes have handy copy methods
  val jim3=jim1.copy()
  println(jim3)
  val jim4= jim2.copy(name="Jimmy", age=45)  
 println(jim4)
 
 // case classes have  companion pattern in built i.e 
 //we have class and scala object available for case class in same scope i.e we have inbuilt companion objects for case classes 
 
 val thePerson=Person
 // case classes are serializable
 // case calsses have extractor patterns= case classes can be used in pattern matching
 // case classs have the inbuilt apply method 
 val person=Person("veeru",33)
  // here inbuilt apply method is used
 val patternMatch: Unit = person match {
    case Person(name, age) => println(name)

  }
 
  case object UnitedKingdome{
    def name : String= "The Uk of GB and NI"
  }
}