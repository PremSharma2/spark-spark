package numberProblems

import tailRecursiveList.ListProblems.{RList, RNil}

import scala.annotation.tailrec

object NumberProblemWithImplicits {

  object NumberOps {

    implicit class RRichInt(n: Int) {
      def isPrime: Boolean = {
        @tailrec
        def isPrimeTailRec(currentDivisor: Int): Boolean = {
          if (currentDivisor > Math.sqrt(Math.abs(n))) true
          else n % currentDivisor != 0 && isPrimeTailRec(currentDivisor + 1)
          //else if (n % currentDivisor != 0) false
          //else isPrimeTailRec(currentDivisor + 1)
        }

        if (n == 1 || n == 0 || n == -1) false
        else isPrimeTailRec(2)
      }


      def decompose: RList[Int] = {
        //as we are putting the current divisor into list so we need to make it variable
        assert(n >= 0)

        @tailrec
        def decomposeTailRec(remaining: Int, currentDivisor: Int, accumulator: RList[Int]): RList[Int] = {
          if (currentDivisor > Math.sqrt(remaining)) remaining :: accumulator
          else if (remaining % currentDivisor == 0) {
            decomposeTailRec(remaining / currentDivisor, currentDivisor, currentDivisor :: accumulator)
          }
          else decomposeTailRec(remaining, currentDivisor + 1, accumulator)
        }

        decomposeTailRec(n, 2, RNil)
      }

    }

  }
  import NumberOps._
  def main(args: Array[String]): Unit = {
    println(2.isPrime)
    println(15.decompose)
    println(53611.isPrime)
  }
}
