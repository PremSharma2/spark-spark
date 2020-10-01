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
  val doubler: (Int => Int) = (x) => x * 2
  //or Function1[Int, Int]
  val product: (Int => Int) = (x) => x * 2
  //multiple params lamda
  //Function2[Int, Int,Int]
  val adder: (Int, Int) => Int = (a, b) => a + b
  //no params lamda or zero lambda
  val justDoSomething: (() => Int) = () => 3

  println(justDoSomething)
  println(justDoSomething())
  // curlybraces with lambdas or syntacticsugar

  val stringtoInt :(String => Int) = { 
    (str) => str.toInt

  }

  //MoAR syntactic sugar
  val niceIncrementer: (Int => Int) = (x) => x + 1
  val niceAdder: (Int, Int) => Int = _ + _ // equivalent to (a,b)=> a+b
  //lambda with curried function
//val superAdd: Function1[Int, Function[Int, Int]]
  /*
  val specialFunction: Function1[Int, Function[Int, Int]] = new Function1[Int, Function[Int, Int]] {
      override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
    or this can be replaced by lambda this way
   */
  val superAdd :(Int =>Int=>Int)= (x) => (y) => (x + y)
}