package codingChallenge
import scala.io._
object SapientTest  extends App {


  val data = Source
    .fromFile("C:\\prem\\prem\\Data\\Spark_VM\\Sapient-Dataset.txt").getLines().toList

  val finalData=data.filter(line => !line.isEmpty).map { line =>
    val value = line.split(",")
    (value(0), value(1))
  }.distinct.drop(1).toSet

finalData.foreach(println)

  //#To preserve the order
  //  val test = list.distinct



}
