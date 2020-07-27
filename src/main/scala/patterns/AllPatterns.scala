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
  val matchanything: Unit = x match{
    case _ => 
  }
  
  // 3 a tuple 
  val atuple=(1,2)
  val matchaTuple: Any =atuple match{
    case (1,1) => 
    case (something,2) => s"I have  found $something"  
  }
  
  val nestedTuple=(1,(2,3))
  val matchnestedTuple= nestedTuple match {
    
    case (_ , (k,v)) => s"Right combination $k and the value is $v"
  }
}