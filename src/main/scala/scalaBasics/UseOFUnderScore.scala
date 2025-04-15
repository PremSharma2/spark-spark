package scalaBasics

import scala.language.higherKinds

object UseOFUnderScore  extends App {

  val _ = 5 // defining a value whose name u don't really care
  val onlyfive: Seq[Int] = (1 to 10).map(_*2)
  // another usage of underscore

  /**
   *  We really don't care the type of option we can use _
   *
   */
  def processList(list : List[Option[_]] ): Int = list.length

  // TODO another scenario is the  default initializer
  //TODO:->  let say i have a string variable i want let the jvm decide what value the variable will have
  var mystring:String = _

  //Ignored Variables:
  val (a, _) = (10, "Scala")
  // todo : This is used when you want to ignore the second part of the tuple.


  //Partially Applied Functions:

  def add(a: Int, b: Int) = a + b
  val addTwo: Int => Int = add(2, _: Int)
  addTwo(3) // returns 5


  //Pattern Matching:

    (1, "Scala") match {
    case (_, "Scala") => println("Found Scala")
    case _            => println("Not Found")
  }
  //todo:  _ is used as a wildcard in pattern matching.

  // lambda sugars
  Seq(1,2,3,4) map(x => x*3)
  Seq(1,2,3,4) map(_*3)
val sumfunction : (Int,Int) => Int  = _ + _

  //todo : -> Higher Kinded type
  class MyHigherKindedJewel[M[_]]
  val myjewel= new MyHigherKindedJewel[List]

  // variable arguments methods
  def makeSentence(words : String*) = words.toSeq.mkString(",")
makeSentence("I","Love","Scala")
  val words= Array("I","Love","Akka")
  //I,Love,Akka
  //val vararg: Array[String] = words : _*
  // it automatically converts array into this into varargs
  println(makeSentence(words : _*))

}
