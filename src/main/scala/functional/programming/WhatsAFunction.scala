package functional.programming

object WhatsAFunction extends App {
  
  //use functions as first class elements 
  
  //in oops
  /*
  TODO
   class Action{
    def execute(element:RequestObject):String= ???
    we cannot pass function as a argument
    here as we can see that Objects are treated as First class citizens

    TODO Functions in scala are represented by Function traits
  }*/
  // TODO : in FP functions are treated as first class citizens
  val doubler: MyFunction1[Int, Int] =
      new MyFunction1[Int,Int]{
    override def apply(element:Int):Int=element*2
  }
  trait MyFunction1[-A, +B]{
    def apply(element:A):B
  }
  trait MyFunction[A,B]{
    def apply(element:A):B
  }

  // TODO also we can call the apply method like we call functions
   val result: Int = doubler(20)

  // here we passed function as method argument although
  // it is implemented via anonymous class

  
  // function types in scala
  //function type=Function1[A][B]
  // here stringToIntConverter is of AnyRef type
  val stringToIntConverter: (String => Int) =new Function1[String,Int]{
    override def apply(string:String):Int=string.toInt
  }
   //: ((Int,Int)=>Int) function types can be used as follows with assignment variable
   val adder : ((Int,Int)=>Int)=new Function2[Int,Int,Int]{
    override def apply(a:Int,b:Int):Int=a+b
  }
  println(doubler.apply(10))
  println(doubler(10).+(2))
  println(stringToIntConverter("3").+(4) )
  println(doubler)
  println(stringToIntConverter)
  println(adder)
  println(adder.apply(2,3))
}