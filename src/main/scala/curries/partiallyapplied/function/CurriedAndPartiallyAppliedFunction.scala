package curries.partiallyapplied.function

object CurriedAndPartiallyAppliedFunction extends App {
  // This is called currying or series of Function Calls in Scala
  val superAddition: Int => (Int => Int) = (x) => (y) => x + y

  val add: Int = superAddition(3).apply(2)

  println(superAddition(3).apply(2)) // it is curried function it is series of function calls
// transforming the methods into Function into Scala
  def curriedMethod(x: Int)(y: Int): Int = x + y // it is called curried method
  //Here we transformed the method into function
  // here after declaring the Int => Int function type you are asking compiler that the
  // pls convert the this method into Function it has two argument list so with remaining argument list
  // convert this into Function
  // parameter this process is called lifting or ETA expansion
  val add4: Int => Int = curriedMethod(4)
  println(add4.apply(5))

  //implict ETA expansion by scala compiler
  def inc(x: Int) = x + 1

  List(1, 2, 3).map(inc)
  //Here Compiler converts this method into the function
  // here actually converts this into lambda
  List(1, 2, 3).map(x => inc(x))
  val add5: Int => Int = curriedMethod(5) _ // this will tel the compiler pls dp ETA expansion for me

  //Exercise
  val simpleAddFunction: (Int, Int) => Int = (x: Int, y: Int) => x + y

  def simpleAdd(x: Int, y: Int) = x + y

  def curriedAddMEthod(x: Int)(y: Int): Int = x + y

  val add7: Int => Int = (x: Int) => simpleAddFunction(7, x)
  // this will turn simpleAddFunction function into series of function calls i.e curried Int => Int => Int
  val add7_2: Int => Int => Int = simpleAddFunction.curried
  // here compiler rewrites into this like x => y => simpleAddFunction(x,y)
  val add7_2_1: Int => Int = simpleAddFunction.curried.apply(7)
  // using ETA expansion
  val add7_3: Int => Int = curriedMethod(7) _ // ETA
  //y => curriedAddMEthod(x,y)
  //

  val add7_4: Int => Int = curriedMethod(7)(_) // same ETA but alternative syntax
  // same ETA  expansion of normal methods we have curried the function calls of normal method
  val add7_5: Int => Int = simpleAdd(7, _: Int)
  // compiler rewrites it like this add7_5: Int => Int = y => simpleAdd(7,y) kind of compose
  println(add7_5.apply(3))
  // compiler will convert this into like this y => fx.apply(7,y)
  val add7_6: Int => Int = simpleAddFunction.apply(7, _: Int)


  def concatenate(a: String, b: String, c: String): String = a + "\t" + b + "\t" + c

  val insertName: String => String = concatenate("Hello I am  ", _: String, "How Are you")
  // y => concatenate("Hello Iam ", y , "How Are you")
  val fillInTheBlanks: (String, String) => String = concatenate(_: String, _: String, "di")
  // (x,y) => concatenate(x,y,"Di")
  println(insertName.apply("Prem"))
  println(fillInTheBlanks.apply("Jai", "Mata"))

  //Exercise
  def curriedFormatter(string: String)(number: Double): String = string.format(number)

  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)
  val simpleFormat: Double => String = curriedFormatter("%4.2f") _
  // fx:= x=> y=> curriedFormatter(string:String)(number:Double)
  val seriousFormat: Double => String = curriedFormatter("%8.6f") _

  val preciseFormat: Double => String = curriedFormatter("%14.12f") _
  println(numbers.map(simpleFormat))

  //Exercise 2
  def byName(n: => Int): Int = n + 1

  def byFunction(fx: () => Int): Int = fx.apply() + 1

  def method: Int = 42

  def parentMethod: Int = 42


}