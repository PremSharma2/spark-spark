package patterns

object AllPatterns extends App {
  
  //1 constants
  val x: Any="scala"
  val constants: String = x match {
     case 1 => "a number"
     case "scala" => "The scala"
     case true    => "Boolean"
      
  }
  
  // match anything
  //wildcard
  val matchanything: String = x match{
    case _ => "Wildcard"
  }
  
  // 3 a tuple Tuple(Int, Int)
  val atuple: (Int, Int) =(1,2)
  val matchaTuple: Any =atuple match{
    case (1,1) => 
    case (something,2) => s"I have  found $something"  
  }
  
  val nestedTuple: (Int, (Int, Int)) =(1,(2,3))
  val matchnestedTuple: String = nestedTuple match {
    
    case (_ , (k,v)) => s"Right combination $k and the value is also Tuple $v"
  }
}