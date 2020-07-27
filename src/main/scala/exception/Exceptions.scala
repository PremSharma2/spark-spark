package exception

object Exceptions extends App {

  val x: String = null
  //println(x.length())

  // throwing and catching Exceptions
  // as we are aware that every thing in scala is expression
  // so throwing an exception will also be an expression which has some value
  // val weirdValue= throw new NullPointerException

  // throwable classes extend the Throwable class
  //Exception  and Error are the major Throwable subtypes

  //How to catch Exceptions
  def getInt(withExceptions: Boolean): Int = {

    if (withExceptions) throw new RuntimeException("No int for you")
    else 42
  }
// catching an exception via pattern matching
  val potentialFail = try {

    getInt(true)
  } catch {
    case e: RuntimeException => 43
  } finally {

    println("finally")
  }
  println(potentialFail)

  //val array=Array.ofDim(Int.MaxValue)

  class OverFlowException extends RuntimeException
  class UnderFlowException extends RuntimeException

  object PocketCalc {
    def add(x: Int, y: Int)  = {
      val result = x + y
      if (x > 0 && y > 0 && result < 0) throw new OverFlowException
      else if(x<0 && y<0 && result>0) throw new UnderFlowException
      else result
      
    }
  }

}