package functional.programming

/*
 *
 *
 * taste of lambdas and anonymous functions in scala which is alternative to oops way ,Of
 *   defining anonymous function and passing them as reference
 */
object AnonymousFunction extends App {
  // anonymous function can be replaced with lambdas or lambda
  //(x) => x * 2 this is apply method implementation
  //val doubler : Function1[Int, Int]
  val doubler: (Int => Int) = (x: Int) => x * 2
  //or Function1[Int, Int]
  val product: (Int => Int) = (x:Int) => x * 2
  //multiple params lamda
  //Function2[Int, Int,Int]
  val adder: (Int, Int) => Int = (a, b) => a + b
  //no params lamda
  val justDoSomething: (() => Int) = () => 3

  println(justDoSomething)
  println(justDoSomething())
  // curlybraces with lambdas or syntacticsugar

  val stringtoInt :(String => Int) = { 
    (str: String) => str.toInt

  }

  //MoAR syntactic sugar
  val niceIncrementer: (Int => Int) = (x) => x + 1
  val niceAdder: (Int, Int) => Int = _ + _ // equivalent to (a,b)=> a+b
  //lambda with curried function
//val superAdd: Function1[Int, Function[Int, Int]]
  val superAdd :(Int =>Int=>Int)= (x: Int) => (y: Int) => (x + y)
}