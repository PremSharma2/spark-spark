package collections.basic.operationAndTransformation
import scala.collection.immutable
import scala.util.Random

object LinearCollection  {
/*
TODO
    Seq defines the ordered and linear Collection
    as well as well-defined Ordering and indexing
    val aSeq= Seq(1,2,3)
    This will be an implementaion of some class because Seq is trait
    Mapin api for seq is index an element
 */

  def testSeq():Unit = {
    val aSeq: Seq[Int] = Seq(1, 2, 3, 4)
    val thirdElement = aSeq.apply(2)
    //other methods
    val reversed = aSeq.reverse
    val concat = aSeq ++ Seq(5, 6, 7)
    val sortedSeq = aSeq.sorted
    val anIncrementedSeq = aSeq.map(_ + 1)
    val aSum= aSeq.foldLeft(0)(_+_)
    val aStrRep: String = aSeq.mkString("[",",","]")
    println(aSeq)
    println(thirdElement)
    println(reversed)
    println(concat)
    println(sortedSeq)
    println(anIncrementedSeq)
  }
  def testList() {
    //TODO Seq implementations in scala
    //List
    val aList = List(1, 2, 3, 4)
    //TODO apart from seq methods we have list specific methods as well in list
    //i.e haead and tail
    val head = aList.head
    val remList = aList.tail
    //TODO appending and prepending
    val aBiggerList = 0 +: aList :+ 7
    /*
    TODO
        final case class ::[B](override val head: B, private[scala] var tl: List[B]) extends List[B] {
      override def tail : List[B] = tl
      override def isEmpty: Boolean = false
}
     */
    val prepending = 0 :: aList
    val scala5X: List[String] = List.fill(5)("Scala")

  }

  def testRanges(): Unit ={
    //TODO Ranges are typically Lazy collections Rep in scala
    //TODO i.e (1 to 100) will not be evluated into base collection but just representation, later on it will
    (1 to 10) foreach(_=> println("Scala"))
  }

  def testArrays(): Unit ={
    val anArray= Array(1,2,3,45) //TODO int[] a on the jvm same like java
    //TODO Arrays are not Sequences i.e they are not part of Sequence hierarchy actually
    //TODO but Arrays have Seq like API
    val aSequence: immutable.Seq[Int] = anArray.toIndexedSeq
    //TODO arrays are mutable in nature
    anArray.update(2,30)// no new array is allocated
  }

  def testVectors: Unit ={
    //TODO vector= fast Seq for large amount of data
    val aVector=Vector(1,2,3,45)
  }
  def smallBenchMark: Unit ={
    val maxRuns: Int = 1000
    val maxCapacity: Int = 1000000

    def getWriteTime(collection:Seq[Int]):Double = {
      val aRandom= new Random()
      val times: Seq[Long] = for {
        i <- 1 to maxRuns
      } yield {
        val index= aRandom.nextInt(maxCapacity)
        val currentTime= System.nanoTime()
        val randomElement= aRandom.nextInt()
        val updatedNewSeq = collection.updated(index,randomElement)
        System.nanoTime() - currentTime
      }
      //TODO compute average time for updating the large collection
      times.foldLeft(0l)(_ + _) *1.0/maxRuns
    }
    val numbersList= (1 to maxCapacity).toList
    val numbersVector= (1 to maxCapacity).toVector
    println(getWriteTime(numbersList))
    println(getWriteTime(numbersVector))
    //TODO o/o is 8740704.0 i.e 8 nano seconds for list
    //TODO 5075.6 is 5 micro seconds
  }

  def main(args: Array[String]): Unit = {
    testSeq()
    smallBenchMark
  }

}
