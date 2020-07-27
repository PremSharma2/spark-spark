package scalaBasics


object CbvVsCbn extends App{
  //call by value function signature
  def calledByValue(x:Long):Unit={

    println("by value" + x)

    println("by value" + x)
  }

  //Call by name function signature
  def calledByName(x: => Long):Unit={
    println("by name" + x)
    println("by name" + x)
  }

  //164013025798918 here evaluation of value is early
  calledByValue(System.nanoTime())
  // here value evaluation is later we pass as expression
  calledByName(System.nanoTime())



  def infinite():Int= 1 + infinite()

  def printFirst(x:Int , y: => Int)= println(x)

  printFirst(infinite, 34)
}