package scalaBasics

import scala.annotation.tailrec


/*
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

  def parameterLessFunction(): Int = 42
  //we can call paramless function with their names only

  println(parameterLessFunction)

  //Always use Recursive function in case of looping rather then using For loop or while loop we use recursion with accumlator
  //to make it tail recursive

//this is not tail recursive because there is an pending computation in each call
  def aRecursiveFunction(accumulator: String, n: Int): String = {

    if (n == 1) accumulator else accumulator + aRecursiveFunction(accumulator, n - 1)

  }
  //function returning a Unit,This is also an example of auxiliary function in scala
  def aFunctionWithSideEffects(aString: String): Unit = println(aString)
  //nested function example
  def aBigFunction(n: Int): Int = {
    def aSmallFunction(a: Int, b: Int): Int = a + b
    aSmallFunction(n, n - 1)
  }

   @tailrec
  def factorial(n: Int): Int = {
    if (n <= 0) 1 else factorial(n - 1)

  }

  def isPrime(n: Int): Boolean = {
    //auxiliary nested function
    @tailrec
    def isPrimeUntil(t: Int): Boolean =
      if (t <= 1) true
    else n % t != 0 && isPrimeUntil(t - 1)
       
      
     isPrimeUntil(n / 2)

    
  }
  
  println(isPrime(8))
  
  //println(isPrime(37))println(aRecursiveFunction("spark", 9))

 // println(aBigFunction(4))
 // println(factorial(3))
  
}