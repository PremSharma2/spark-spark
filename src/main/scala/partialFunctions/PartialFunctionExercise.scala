package partialFunctions

object PartialFunctionExercise  extends App {
  /*
  Exercise number 1 Construct a PF instance
   */
val anManualFussyFunction:PartialFunction[Int,Int]= new PartialFunction[Int,Int] {
  override def isDefinedAt(x: Int): Boolean =
    x==1|| x==2|| x==5


  override def apply(x: Int): Int = x match {
    case 1 => 42
    case 2 => 43
    case x=> 44
  }
}
  // chatbot
  val chatBot:PartialFunction[String,String]={
    case "Hello" => "Hi My name is Prem"
    case "Scala" => "Scla is ver good language"
    case "goodbye"=> "never say good bye"


  }
scala.io.Source.stdin.getLines().map(chatBot).foreach(println)

}
