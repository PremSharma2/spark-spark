package scalaBasics

object StringOperations extends App{
  
  val str:String="Hello I am learning Scala"
  println(str.charAt(2))
  println(str.substring(7, 11))
  println(str.split(" ").toList)
  
  val numberStr:String="45"
  println(numberStr.toInt)
  val name="Prem"
  val age=12
  val greeting=s"hello, my name is  $name and Iam $age years old"
  println(greeting)
}