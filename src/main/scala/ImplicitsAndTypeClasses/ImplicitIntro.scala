package ImplicitsAndTypeClasses

object ImplicitIntro extends  App {
// Here "Prem " -> this expression is "Prem" first get converted into AroowAsosc
  //Instance and then on ArrowAssoc Instance we will call -> method of that
  //ArrowAssoc implicit class
  val pair: (String, String) = "Prem " -> "222"
  val intpair: (Int, Int) = 1 -> 2

  case class Person(name:String){
    def greet = s"Hi my name is $name"
  }

  implicit  def StringToPerson(str:String): Person =Person(str)
  // Here Compiler will scan all Classes traits everything which has something which takes
  // String and gives a object which has greet method and which is marked implicit as well
  // here it will find Person case class which does that
  println("Prem".greet)
  //Compiler will re write the code like this
  println(StringToPerson("Prem").greet)
  // implicit parameters
  // it is use ful when we pass spark session from one component to another component
  // we marked the parameter list implicit and then we declare somewhere in the project structure
  //  implicit val defaultAmount=10
  implicit val defaultAmount=10
  def increment(x:Int)(implicit amount:Int) = x+amount

  println(increment(2))

}
