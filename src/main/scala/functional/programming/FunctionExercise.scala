package functional.programming

object Exercise extends App {
  //anonymous class inside a function concat
  def concat: ((String, String) => String) = new Function2[String, String, String] {
    override def apply(s1: String, s2: String): String = s1 + s2
  }

    concat("hi","ti")
  println(concat.apply("hello", "scala"))
    println("concat"+ concat.getClass)

  
  
  //curried function without lambda

  val specialFunction: Function1[Int, Function[Int, Int]] =
    new Function1[Int, Function[Int, Int]] {
      override def apply(x: Int): Function1[Int, Int] = new Function1[Int, Int] {
      override def apply(y: Int): Int = x + y
    }

  }
  val adder: Int =specialFunction(3)(4)
  println(specialFunction)
  println(adder.getClass)
  println(adder)
  // this is called curried function
  println(specialFunction.apply(3).apply(4))

}