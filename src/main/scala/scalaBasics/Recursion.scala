package scalaBasics

import scala.annotation.tailrec

/*
 * @author psharma
 */
object Recursion extends App {

  /*
   *
   * Recursion Implementation with Outof stack memory error
   */
  def factorial(n: Int): Int = {
    if (n <= 1) 1
    else {

      println("computing factorial of " + n + "- I first need factorial of " + (n - 1))
      val result = n * factorial(n - 1)
      println("computed  factorial of 	" + n)
      result

    }
  }

  def anotherFactorial(n: Int): BigInt = {
    @tailrec
    def factHelper(x: Int, accumlator: BigInt): BigInt = {
      if (x <= 1) accumlator 
      else factHelper(x - 1, x * accumlator)
    }
    factHelper(n, 1)

  }

  /*
   * anotherFactorial(10)=factHelper(10,1)
   * =factHelper(9,10*1)
   * factHelper(8,9*10*1)
   * factHelper(7,8*9*10*1)
   * factHelper(6,7*8*9*10*1)
   * --------
   * -------
   * factHelper(1,1*2*3*4*5*6*7*8*9*10*1)
   * at last
   * accumlator =1*2*3*4*5*6*7*8*9*10*1
   *
   */

  //concat of string using tail recursion
  /*
  The recursive call should be the last thing to do i the function no pending computation is there
   */
  @tailrec
  def concatStringTailRecur(aString: String, n: Int, accumlator: String): String = {
    if (n <= 0) accumlator else concatStringTailRecur(aString, n - 1, aString + accumlator)

  }

  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeTailRec(t: Int, isStillPrimeAccumlator: Boolean): Boolean =
      // if accumulator value is false then we will return false value
      if (!isStillPrimeAccumlator) false
      else if (t <= 1) true
      else {
        var conditionalExpression = n % t != 0
        // if any of these flag is false accumulator value will be false here
       var accumulatedValue: Boolean =  (conditionalExpression && isStillPrimeAccumlator)
        isPrimeTailRec(t - 1, accumulatedValue)
      }
     isPrimeTailRec(n/2, true)
  }

  // println(anotherFactorial(5000))
}