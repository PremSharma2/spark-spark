import java.io._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

// Java program to print largest contiguous array sum
import java.util._

//remove if not needed
import scala.collection.JavaConversions._

object RoughTest1 {

  def main(args: Array[String]): Unit = {
    val a: Array[Int] = Array(-2, -3, 4, -1, -2, 1, 5, -3)
    println("Maximum contiguous sum is " + maxSubArraySum(a))
    val seq= Seq(2,2,2,3,3,4,5)
    println( KmostFrequent(seq))
    val a1: Array[Int] = Array(-2, -3, -4, -1, -2, -1, -5, -3)
    val n: Int = a.length
    val max_sum: Int = maxSubArraySum1(a1, n)
    println("Maximum contiguous sum is " + max_sum)

  }

  def maxSubArraySum(a: Seq[Int]): Int = {
    val size: Int = a.length
    var max_so_far: Int = java.lang.Integer.MIN_VALUE
    var max_ending_here: Int = 0
    for (i <- 0 until size) {
      max_ending_here = max_ending_here + a(i)
      if (max_so_far < max_ending_here) max_so_far = max_ending_here
      if (max_ending_here < 0) max_ending_here = 0
    }
    max_so_far
  }


  def KmostFrequent(numbers: Seq[Int]): Seq[((Int, Int), Int)] ={
    import scala.collection.immutable.Map
    val map: Map[Int, Int] = numbers.foldLeft(Map.empty[Int, Int]) { (map, item) =>
      map + (item -> (map.getOrElse(item, 0) + 1))
    }
   // val zippedmap: Seq[((Int, Int), Int)] =map.zipWithIndex.toList
    val list: Seq[(Int, Int)] = map.toList
    list.sortBy(-_._2)
    val indexedTuple: Seq[((Int, Int), Int)] = list.zipWithIndex
    //((0,(1->4)), (1,(2->3)), (3,(5->2)) ,(4,(4->1) , (5,(6->1)))
    indexedTuple
  }

  def maxSubArraySum1(a: Array[Int], size: Int): Int = {
    var max_so_far: Int = a(0)
    var curr_max: Int = a(0)
    for (i <- 1 until size) {
      curr_max = Math.max(a(i), curr_max + a(i))
      max_so_far = Math.max(max_so_far, curr_max)
    }
    max_so_far
  }
}