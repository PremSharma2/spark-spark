package ImplicitsAndTypeClasses
import java.{util => javaCollection}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
object ScalaJavaConversions  extends App {

  import collection.JavaConverters._

val javaSet:javaCollection.Set[Int] = new javaCollection.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)
  // it is like implicit def intToBoolean(i:Int):Boolean= i==1
  // which converts int o boolean via implicit method
  // so same like here we have method
  //implicit def asScalaSetConverter[A](s : ju.Set[A]): AsScala[mutable.Set[A]] =
  //    new AsScala(asScalaSet(s)) in JavaConverters
  // which converts javaSet to AsScala class object
  // and this  AsScala class has method asScala
  val scalaSet: mutable.Set[Int] = javaSet.asScala

  import collection.mutable
  val numbersBuffer: mutable.Seq[Int] = ArrayBuffer[Int](1,2,3,4)
  val javaCollectionBuffer = numbersBuffer.asJava
println(javaCollectionBuffer.asScala  eq numbersBuffer)
  val numbers: Seq[Int] = List(1,2,3,4)
  val javaNumbers = numbers.asJava
  val backToScala: mutable.Seq[Int] = javaNumbers.asScala

  println(backToScala eq numbers)
}
