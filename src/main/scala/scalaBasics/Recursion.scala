package scalaBasics

import scala.annotation.tailrec

/*
 * @author psharma
 */
object Recursion extends App {

  /*
   *
   * Recursion Implementation with Out of stack memory error
   * this is called stack recursion
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
/*
TODO
    Recursive call should be the last in ur code branch
 */
  def anotherFactorial(n: Int): BigInt = {
    @tailrec
    def factHelper(n: Int, accumulator: BigInt): BigInt = {
      if (n <= 1) accumulator
      else factHelper(n - 1, n * accumulator)
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
  def concatStringTailRecur(aString: String, n: Int, accumulator: String): String = {
    if (n <= 0) accumulator else concatStringTailRecur(aString, n - 1, aString + accumulator)

  }

  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeTailRec(t: Int, isNumberPrime: Boolean): Boolean =
      // if accumulator value is false then we will return false value
      if (!isNumberPrime) false
      else if (t <= 1) true
      else {
        var conditionalExpression = n % t != 0
        // if any of these flag is false accumulator value will be false here
        val accumulator: Boolean = (conditionalExpression && isNumberPrime)
        isPrimeTailRec(t - 1, accumulator)
      }
     isPrimeTailRec(n/2, true)
  }

  // println(anotherFactorial(5000))
}