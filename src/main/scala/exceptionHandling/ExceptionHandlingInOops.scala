package exceptionHandling

object ExceptionHandlingInOops extends App {

  val x: String = null
  //println(x.length())
/*
todo
  // throwing and catching Exceptions
  // as we are aware that every thing in scala is expression
  // so throwing an exception will also be an expression which has some return value
  // val weirdValue:Nothing = throw new NullPointerException
  // throwable classes extend the Throwable class
  //Exception  and Error are the major Throwable subtypes


 */
  //todo : -> How to catch Exceptions
  def getInt(withExceptions: Boolean): Int = {

    if (withExceptions) throw new RuntimeException("No int for you")    else  42
  }
//todo:->  catching an exception via pattern matching
  val potentialFail: Int = try {
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


  /*
  Returning an Option from a method
The toInt method used in this book shows how to return an Option from a method.
It takes a String as input and returns a Some[Int]
 if the String is successfully converted to an Int, otherwise it returns a None:

   */

  def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  }
  //same thing but in short

  import scala.util.control.Exception._
  def toInt1(s: String): Option[Int] = allCatch.opt(s.toInt)
  val x1: Int = toInt("prem").getOrElse(2)
  val x2: Option[Int] = toInt("hello").orElse(Some(2))
}