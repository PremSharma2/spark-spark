package scalaBasics

import scala.annotation.tailrec


/**
tODO
 * Single Object which exposes some functions
 * 
 * @author psharma
 */

object Function extends App {

  def aFunction(a: String, b: Int) =

    a + "" + b

  def aFunctionWithCodeBlock(a: String, b: Int) = {

    a + "" + b
  }

 // println(aFunction("hello", 3))
// TODO : its is a getter function so in FP function function shd look like  properties
  def parameterLessFunction: Int = 42
  //TODO : -> we can call parameterless function with their names only

  println(parameterLessFunction)

  //Always use Recursive function in case of looping rather then using For loop or while loop we use recursion with accumulator
  //to make it tail recursive

//this is not tail recursive because there is an pending computation in each recursive call
  def aRecursiveFunction(accumulator: String, n: Int): String = {

    if (n == 1) accumulator else accumulator + aRecursiveFunction(accumulator, n - 1)

  }

  //function returning a Unit,This is also an example of auxiliary function in scala
  def aFunctionWithSideEffects(aString: String): Unit = println(aString)

  //TODO : nested function example
  def aBigFunction(n: Int): Int = {
    def aSmallFunction(a: Int, b: Int): Int = a + b
    aSmallFunction(n, n - 1)
  }

  //todo: one more example for nested method
  /*
 TODO
    In this scenario, isValidLength and containsSpecialCharacter
    are nested functions within processUserInput.
    They encapsulate specific validation checks,
     making the main function more readable and easier to maintain.
   */
  def processUserInput(input: String): Boolean = {
    def isValidLength(str: String): Boolean = str.length >= 8

    def containsSpecialCharacter(str: String): Boolean = str.exists(!_.isLetterOrDigit)

    isValidLength(input) && containsSpecialCharacter(input)
  }

  val userInput = "Scala@2024"
  val isValid = processUserInput(userInput)
  println(s"Is user input valid? $isValid")

  //TODO :-> concept of tail recursion
   @tailrec
  def factorial(n: Int): Int = {
    if (n <= 0) 1 else factorial(n - 1)

  }

  def isPrime(n: Int): Boolean = {
    //auxiliary nested function

    def isPrimeUntil(t: Int): Boolean =
      if (t <= 1) true
    else {
        val accumulator: Boolean = n % t != 0
        accumulator  && isPrimeUntil(t - 1)
      }
       
      
     isPrimeUntil(n / 2)

    
  }
  
  println(isPrime(8))
  
  //println(isPrime(37))println(aRecursiveFunction("spark", 9))

 // println(aBigFunction(4))
 // println(factorial(3))
  
}