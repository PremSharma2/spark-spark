package codingChallenge

object HMRCTest extends App{

  import scala.collection.mutable
  import scala.collection.mutable.ArrayBuffer
  val productGroup = new scala.collection.concurrent.TrieMap[String, Int]()
  val dropList = new mutable.HashSet[String]() with mutable.SynchronizedSet[String]
    val orderList= new ArrayBuffer[String]
    orderList +=("Melon", "Apple", "Lime", "Apple", "Banana", "Melon", "Lime", "Lime")
    val map =orderList.groupBy(item => item)
    println(map)
  val discountList= new ArrayBuffer[String]
  discountList +=("Lime","Melon")
/*

a new collection of type
 That which contains all elements of this sequence except
 some of occurrences of elements that also appear in that.
 If an element value x appears n times in that,
  then the first n occurrences of x will not form part of the result,
  but any following occurrences will.
 */
  println(orderList.toList diff(discountList))
//List(Apple, Apple, Banana, Melon, Lime, Lime)
  }


