package codingChallenge

import java.io._

import java.util.Scanner

import java.util._

//remove if not needed
import scala.collection.JavaConversions._

object Test {

  def main(args: Array[String]): Unit = {
    val sc: Scanner = new Scanner(System.in)

    var num: Int = 0
    var temp: Int = 0
    var x1: Int = 0
    var x2: Int = 0
    var position1: Int = 0
    var position2: Int = 0
    var maxi: Int = 0
    println("Enter the number of houses: ")
    num = sc.nextInt()
    val house_number: Array[Int] = Array.ofDim[Int](num)
    val position: Array[Int] = Array.ofDim[Int](num)
    val copy_position: Array[Int] = Array.ofDim[Int](num)
    println("Enter the house number and position of the house: ")
    for (i <- 0 until num) {
      house_number(i) = sc.nextInt()
    }
    println("Input: ")
    for (i <- 0 until num) {
      System.out.print("[" + house_number(i) + "," + position(i) + "] ")
    }
    println("")
    for (i <- 0 until num) {
      copy_position(i) = position(i)
    }
    Arrays.sort(copy_position)
    for (i <- 0 until num - 1) {
      temp = copy_position(i + 1) - copy_position(i)
      if (temp > maxi) {
        maxi = temp
        x1 = copy_position(i)
        x2 = copy_position(i + 1)
      }
    }
    for (i <- 0 until num) {
      if (x1 == position(i)) {
        position1 = i
      } else if (x2 == position(i)) {
        position2 = i
      }
    }
    if (//The house number is displayed which is matched by the position obtained
    house_number(position1) > house_number(position2)) {
      println(
        "Result: [" + house_number(position2) + "," + house_number(position1) +
          "] ")
    } else {
      println(
        "Result: [" + house_number(position1) + "," + house_number(position2) +
          "] ")
    }
  }

}
