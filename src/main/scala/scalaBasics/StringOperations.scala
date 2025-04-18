package scalaBasics

/**
 * Basic string operations and combinators string provides
 */
object StringOperations extends App {

  val str: String = "Hel,lo I am lea,rni,ng Sc,ala"
  println(str.charAt(2))
  println(str.substring(7, 11))
  println(str.split(" ").toList)
  val splitArray: Array[String] = str.split(" ")
  val doubleSplit: Array[Array[String]] = {
      splitArray.map(_.split(","))

  }
  val strreplace = "Hello, world!"
  val newStr = strreplace.replace("world", "Scala")

  val newCascadeStr = strreplace.replace("world", "Scala")
                                .replace("orldw", "Scala")

  val x: Array[Int] = doubleSplit.map(list => list.length)
 val xxx: Array[Seq[String]] = splitArray.map(word => Seq(word))

  val sorted = doubleSplit.sortBy(_.length)
  val thresh = sorted.head.length  // assume qq is non-empty
 val rs: Array[Array[String]] = sorted.takeWhile(_.length== thresh)
  val numberStr: String = "45"

  println(numberStr.toInt)
  val name = "Prem"
  val age = 12
  val greeting = s"hello, my name is  $name and Iam $age years old"
  println(greeting)
}