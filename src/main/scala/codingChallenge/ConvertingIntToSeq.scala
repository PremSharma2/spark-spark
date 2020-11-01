package codingChallenge

import java.util

object ConvertingIntToSeq  extends App{
      var number: Int = 122
      var length: Int = 0
      var org: Int = number
      while (org != 0) {
        org = org / 10
        length += 1
        length - 1
      }
      val array: Array[Int] = Array.ofDim[Int](length)
      for (i <- 0 until length) {
        val rem: Int = number % 10
        array(length - i - 1) = rem
        number = number / 10
      }
      println(util.Arrays.toString(array))



    }





