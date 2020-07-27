package functional.programming

import scala.annotation.tailrec

object HofAndCurries extends App {

  /*
   *
   *  Here superFunction takes two parameters one is int another is function which takes String and another function and return of
   *  super function is another function these type of functions which takes input as function and gives o/p as function are called
   *  Higher Order function
   */
  //val superfunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = ???

  /*implementation of higher order function which takes plusone function and two int params
   * ntimes(plusone,1,3)
   * ntimes(plusone,0,4)
   * 
   */
  @tailrec
  def nTimes(function: Int => Int, n: Int, accumulator: Int): Int = {
    if (n <= 0) accumulator
    else {
     // nTimes(f, n - 1, function.apply(x))
      var accumulatedValue= function.apply(accumulator)
      nTimes(function, n - 1, accumulatedValue)
    }

  }

  val plusOne: Int => Int = (x: Int) => x + 1

  println(nTimes(plusOne, 10, 1))
  // curried approach
  
  //increment10 = ntb(plusone,10)= x => plusone(plusOne.......(x))
  // it will return lambda nor the value  it is better approach
  /*
   * this is curring the application of this function n number of times 
   * ntb(plusOne,2)
   * = ntb(plusOne,1)(plusOne.apply(x))
   * =ntb(plusOne,0)((plusOne.apply(x))
   * = (f(f(x))
   *
   * All that the theory of currying means is that a function that takes multiple arguments
   *  can be translated into a series of function calls in a cascading manner
   *  that each take a single argument.
   * Hence output of this operation will be a curried function
   * Here in Recursion each stack frame will return function when recursion traces back
   * In pseudocode , this means that an expression like this:
   * result = f(x)(y)(z)
   * or like this  x => y => fx(x, y)
   *
   * In mathematics and computer science, currying is the technique of translating the evaluation of a function
   * that takes multiple arguments into evaluating a sequence of functions, each with a single argument.
   *
   * Here by passing one argument we converted into series of function call
   * x => plusOne.apply(plusOne.apply(plusOne.apply(plusOne.apply(x))))
   * or
   * ntb(f,4)	= x => f(f(f(f(x))))
   * x => f(f(f(f(x)))) hence this is the output of this function ntb(f,4)
   */
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) = {
    if (n <= 0) (x: Int) => x
    /*plusOne(f(x))
     * plusOne(plusOne.........(plusOne)f((x))
     * f: (Int => Int) => f => f=> f=> f=>......f=> Int
     * i.e ntb(f,4)	= x => f(f(f(f(x))))
     * x => plusOne.apply(plusOne.apply(plusOne.apply(plusOne.apply(x)))) this lambda is return type
     * increment2=ntb(plusOne,2) = x => plusOne(plusOne(x))
     *
     */
    //else (x:Int) => nTimesBetter(f, n-1).apply(f.apply(x))
    else (x: Int) => nTimesBetter(f, n - 1)(f(x))
  }
//Function[Int, Int] here fx is Function[Int, Int] and its function type is (Int, Int) => Int
  /*
  Here we passed an function as input param and then it got converted into series of function calls
   */
  def toCurry(fx: (Int, Int) => Int): (Int => Int => Int) =
  //result = f(x)(y)(z)
  //  (x => (y => (fx(x, y)) ))
     //x => y => fx.apply(x,y) or x+y

    x => y => fx(x, y)

  def fromCurry(function: (Int => Int => Int)): (Int, Int) => Int =
    // it is equivalent to x,y=> x+y
   // (x,y) => function.apply(x).apply(y)
     (x, y) => function(x)(y)

  /*def compose(function1: Int => Int, function2: Int => Int): Int => Int =
    x => function1.apply(function2.apply(x))

  def andThen(function: Int => Int, g: Int => Int): Int => Int =
    x => g.apply(function.apply(x))*/

    def compose [A,B,T](function1: A => B, function2: T => A): T => B =
     // x => functions.apply(g.apply(x))
      x => function1(function2(x))

  def andThen [A,B,C](function: A => B, function1: B => C): A => C =
    x => function1(function(x))
// plus10 is series of function calls
  val plus10: Int => Int = nTimesBetter(plusOne, 10)
  //We will get the refrence of functional interface
  println(plus10.getClass.getName)
  println(plus10.apply(1))
  // curried function
  val superAddition: Int => (Int => Int) = (x) => (y) => x + y
  val adder : (Int,Int) => Int  = (x,y) => x+y
  val superAdder :(Int => Int => Int)  = toCurry(adder )
  // x => y => fx(x, y)
  //or
  // x => y => fx(x) fx(y)
  println(superAdder.apply(2).apply(4))
  val simpleAdder : (Int, Int) => Int= fromCurry(superAdder)
  //(x,y) => function.apply(x).apply(y)
  //(x, y) => function(x)(y)
  println(simpleAdder(2,4))
  val add3 = superAddition(3)
   val add2 =(x :Int) => x+2
   val times3= (x:Int) => x*3
   val composed : Int => Int = compose(add2, times3)
   val ordered : Int => Int = andThen(add2, times3)
   println("composed"+composed(4))
   println(ordered(4))
 /* println(superadder.getClass.getName)
  println(add3.getClass.getName)
  println(add3.getClass.getDeclaredMethods.foreach(f => f.getParameters.foreach(p => println(p.getParameterizedType.getTypeName))))*/
  println(add3(10))
  // functions with multiple parameter list
   println()
  def curriedFormatter(c: String)(x: Double): String = c.format(x)
  val standardFormat: (Double => String) = curriedFormatter("%4.3f")
  println(nTimes(plusOne, 2,2))
  val ntimes=nTimesBetter(plusOne, 2)
  //println(ntimesBetter(plusOne, 0).getClass.getDeclaredMethods.foreach(println))
  println(nTimesBetter(plusOne, 0).apply(2))
  println(ntimes.getClass.getCanonicalName)
  println(nTimesBetter(plusOne, 2).apply(2))
  //println(standardFormat(Math.PI))

}