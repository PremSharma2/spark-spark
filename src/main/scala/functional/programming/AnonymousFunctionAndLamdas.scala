package functional.programming

import functional.programming.WhatsAFunction.MyFunction1

/**
 *
 *
 * taste of lambdas and anonymous functions in scala which is alternative to oops way ,Of
 *   defining anonymous function and passing them as reference
 */
object AnonymousFunctionAndLamdas extends App {
  /*
TODO
  // anonymous function can be replaced with lambdas or lambda
  //(x) => x * 2 this is apply method implementation
  //val doubler : Function1[Int, Int]
   */

  val doubler1: MyFunction1[Int, Int] =
         new MyFunction1[Int,Int]{
    override def apply(element:Int):Int=element*2
  }
  /*
 TODO
  // val fux:MyFunction[Int,Int]=(x:Int) => x*1
  // val doubler: MyFunction1[Int, Int] = (x) => x * 2
  // MyFunction1[Int, Int] this can be replaced with function types
  //(Int => Int)

   */
  val doubler: (Int => Int) = (x) => x * 2
  //or Function1[Int, Int]
  val product: (Int => Int) = (x) => x * 2
  //multiple params lamda
  //Function2[Int, Int,Int]
  val adder: (Int, Int) => Int = (a, b) => a + b
  //no params lamda or zero lambda
  val justDoSomething: () => Int = () => 3

  println(justDoSomething)
  println(justDoSomething())
  // curlybraces with lambdas or syntacticsugar
//todo if we have multiple computations in function we can use syntactic Sugar
  val stringtoInt :(String => Int) = { 
    (str) => if(str.toInt %2==0) 1 else 0
  }

  //MoAR syntactic sugar
  val niceIncrementer: Int => Int = (x) => x + 1
  val niceAdder: (Int, Int) => Int = _ + _ //todo:-> equivalent to (a,b)=> a+b
  //todo lambda with curried function
//todo val superAdd: Function1[Int, Function[Int, Int]]
  /*
TODO
  val specialFunction: Function1[Int, Function[Int, Int]] = new Function1[Int, Function[Int, Int]] {
      override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
    or this can be replaced by lambda this way
   */
  val superAdd :(Int =>Int=>Int)= (x) => (y) => (x + y)

  def api(fx:Function1[Int,Int]) = ???
  // here we can see api is hof which accept function and we can pass function as an argument
  api(niceIncrementer)
}