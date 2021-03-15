package functional.programming

import scala.annotation.tailrec

object HigherOrderFunctionAndCurries extends App {

  /*
   *
   * TODO
   *      Here superFunction takes two parameters one is int and
   *      another is function which takes String and another function and return of
   *      super function is another function these type of functions
   *      which takes input as function and gives o/p as function are called
   *      Higher Order function
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
      val accumulatedValue = function.apply(accumulator)
      nTimes(function, n - 1, accumulatedValue)
    }

  }

  // val function:Function1[Int,Int] = (x: Int) => x + 1
  val plusOne: Int => Int = (x) => x + 1
  // here as we can see that the now functions can be passed  as an argument to the
  // here also we can notice that we have curried the function calls
  val ft: Int => Int = (x: Int) => plusOne(plusOne(plusOne(x)))
  // another function so functions are treated as primary citizens
  println(nTimes(plusOne, 10, 1))
  // curried approach

  /*
   *TODO
   * All that the theory of currying means is that a function that takes multiple arguments
   *  can be translated into a series of function calls in a cascading manner
   *  that each take a single argument or input .
   * Hence output of this operation will be a curried function
   * Here in Recursion each stack frame will return function when recursion traces back
   * In pseudocode , this means that an expression like this:
   * for example we need to convert
   * fx = (x,y) => x+y
   *  to
   * or like this fz= x => y => fx(x, y)
   * val result =fz(x)(y)
   *
   *TODO
   * In mathematics and computer science, currying is the technique of translating the evaluation of a function
   * that takes multiple arguments into evaluating a sequence of functions, each with a single argument.
   *
   *TODO
   * Here by passing one argument we converted into series of function call
   * x => plusOne.apply(plusOne.apply(plusOne.apply(plusOne.apply(x))))
   * or
   * ntb(f,4)	= x => f(f(f(f(x))))
   * x => f(f(f(f(x)))) hence this is the output of this function ntb(f,4)
   */
  /*
    TODO
        ntimes Better Explanation
        break down of All recursion calls in stack
     * nTimesBetter(f,4) = x => nTimesBetter(f,3).apply(f.apply(x)) // First stack of recursion
     * nTimesBetter(f,3) = x => nTimesBetter(f,2).apply(f.apply(x))//2nd
     * nTimesBetter(f,2) = x => nTimesBetter(f,1).apply(f.apply(x))//3rd
     * nTimesBetter(f,1) = x => nTimesBetter(f,0).apply(f.apply(x))//4th
     * nTimesBetter(f,0) = (x: Int) => x  it will return identity function and

TODO
     now recursion traces back
      Now When Lets Evaluate How recursion Trace Back
      Identity Function is the Return value returned to caller i.e 4th Position
      so it will be like
TODO
      now at 4th position identity Function applied f.apply(f.apply(x)) = f.apply(x) or f(x)
      because x=> f(x) is equivalent to the f(x)
      i.e when u apply identity function to f.apply(x) this input which is function
       fIdentity.apply(f.apply(x)) then it will Return f.apply(x)
       bcz o/p of identity function is i/p
       applied to it hence it will look like this
       fx: x => f(x) this will be the returned value
       ---------------------------------------------
     Now lets evaluate the 3rd Position
     nTimesBetter(f,1) value returned to this call is  fx: x => f(x)
     which is an function
        So the value calculated at trace back recursion at 3rd position
        fx is nothing but f applied to input
        and input is f.apply(x)
        so it will become like this
    fy:    x=>  f.apply(f.apply(x))
         fy is nothing but f applied twice to input
----------------------------------------------------------
TODO
       Now lets evaluate the 2nd Position
         f(f(f(x)))
        fz: x=>   f(f(f(x)))
         it will be returned
         fz is nothing but f applied 3rice to input

         -----------------------------------------
   TODO
         Now Lets Evaluate at 1 position
            f(f(f(f(x))))
           But if You look closely fx fy fz functions are nothing they are just
           currying of f function which is original function so i can write
           ntb(f,4)	= x => f(f(f(f(x))))
      or----------------------------------------------------------------
     * f: (Int => Int) => f => f=> f=> f=>......f=> Int
     * i.e ntb(f,4)	= x => f(f(f(f(x))))
     * x => plusOne.apply(plusOne.apply(plusOne.apply(plusOne.apply(x)))) this lambda is return type
     * increment2=ntb(plusOne,2) = x => plusOne(plusOne(x))
     Important Note
     when we write
     f4= nTimesBetter(f,4)
     then compiler will return like this
     val f4Alt= (x:Int) => nTimesBetter(f,3)(f(x))
     new Function1[Int,Int]{
     apply(x){
           nTimesBetter(f,3).apply(f(x))
     }
     }
     but when we make a call f4alt(5)
     then all function objects are created
     *
     */
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) = {
    if (n <= 0) (x: Int) => x // This is called Identity Function
    //else (x:Int) => nTimesBetter(f, n-1).apply(f.apply(x))
    else (x: Int) => nTimesBetter(f, n - 1)(f(x))
  }

  def nTimesOriginal(function1: Function1[Int, Int], n: Int): Function1[Int, Int] = {
    if (n <= 0)
      new Function[Int, Int] {
        override def apply(x: Int): Int = x
      } // Identity function /JVM object
    else
      new Function[Int, Int] {
        override def apply(x: Int): Int =
          nTimesOriginal(function1, n - 1).apply(function1.apply(x))
      }

  }

  val nTimesOriginal: Function1[Int, Int] =
    nTimesOriginal(plusOne, 2)
  nTimesOriginal.apply(2)

  //Function[Int, Int] here fx is Function[Int, Int] and its
  // function type is (Int, Int) => Int
  /*
  Here we passed an function as input param and
   then it got converted into series of function calls
   Here we are converting a def into curried function
   */
  def toCurry(fx: (Int, Int) => Int): (Int => Int => Int) =
  //result = f(x)(y)(z)
  //  (x => (y => (fx(x, y)) ))
  //x => y => fx.apply(x,y) or x+y

    x => y => fx(x, y)


  // This one is curried to normal i.e revrese
  def fromCurry(function: (Int => Int => Int)): (Int, Int) => Int =
  // it is equivalent to x,y=> x+y
  // (x,y) => function.apply(x).apply(y)
  // function: x=> y => x+y
  // x,y => function(x).apply(y)
    (x, y) => function(x)(y)

  /*def compose(function1: Int => Int, function2: Int => Int): Int => Int =
    x => function1.apply(function2.apply(x))

  def andThen(function: Int => Int, g: Int => Int): Int => Int =
    x => g.apply(function.apply(x))*/

  def compose[A, B, T](function1: A => B, function2: T => A): T => B = {
    // x => f.apply(g.apply(x))
    x => function1(function2(x))
    // or we can write like this
    //x => (function1 compose function2 ) (x)
  }


  //   Composing small exercise
  /*
 Given two functions f1 and f2, implement f3 by composing f1 and f2
val f3: (Int, Int) => String = ???
   */

  val f1: (Int, Int) => Int = (a, b) => a + b
  val f2: Int => String = _.toString
  val f3: (Int, Int) => String = (x, y) => f2(f1(x, y))
  // val f5: (Int, Int) => String = (x, y) =>  f2.compose(f1)
  /*
   TODO
       Compose more live examples here

   */

  def convert: String => String = (value: String) => "converted"
  def verify: String => String = (value: String) => if (value == "converted") "valid" else "invalid"
    val finalfx= (x:String) => convert(verify(x))
  def vc: String => String = convert compose verify
  finalfx("Scala")
  vc("prem")

  def andThen[A, B, C](function: A => B, function1: B => C): A => C =
    x => function1(function(x))
//TODO example
  def verifiedOutput = verify andThen convert
  // plus10 is series of function calls
  val plus10: Int => Int = nTimesBetter(plusOne, 10)
  //We will get the refrence of functional interface
  println(plus10.getClass.getName)
  println(plus10.apply(1))
  // curried function
  val superAddition: Int => Int => Int = (x) => (y) => x + y
  val adder: (Int, Int) => Int = (x, y) => x + y
  // here we converted adder function which is normal function to curried function
  val superAdder: Int => Int => Int = toCurry(adder)
  // x => y => fx(x, y)
  //or
  // x => y => fx(x).apply(y)
  println(superAdder.apply(2).apply(4))
  val simpleAdder: (Int, Int) => Int = fromCurry(superAdder)
  //(x,y) => function.apply(x).apply(y)
  //(x, y) => function(x)(y)
  println(simpleAdder(2, 4))
  val add3 = superAddition(3)
  val add2 = (x: Int) => x + 2
  val times3 = (x: Int) => x * 3
  val composed: Int => Int = compose(add2, times3)
  val ordered: Int => Int = andThen(add2, times3)
  println("composed" + composed(4))
  println(ordered(4))
  /* println(superadder.getClass.getName)
   println(add3.getClass.getName)
   println(add3.getClass.getDeclaredMethods.foreach(f => f.getParameters.foreach(p => println(p.getParameterizedType.getTypeName))))*/
  println(add3(10))
  // functions with multiple parameter list
  println()

  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: (Double => String) = curriedFormatter("%4.3f")
  println(nTimes(plusOne, 2, 2))
  val ntimes = nTimesBetter(plusOne, 2)
  //println(ntimesBetter(plusOne, 0).getClass.getDeclaredMethods.foreach(println))
  println(nTimesBetter(plusOne, 0).apply(2))
  println(ntimes.getClass.getCanonicalName)
  println(nTimesBetter(plusOne, 2).apply(2))
  //println(standardFormat(Math.PI))

}