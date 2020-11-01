package codingChallenge

import scala.annotation.tailrec

object RotateListKTimesTest extends App {

    // 100%
    def solution(A: Array[Int], K: Int): Array[Int] = {
      def rotateKStep(list: List[Int], K: Int): List[Int] = {

        def rotateOneStep(l: List[Int]) = {
          l.take(l.size - 1).+:(l.last)
        }
// if k==0 do not rotate and return from here
        if (K == 0) list
          // else rotate one step
        else rotateKStep(rotateOneStep(list), K - 1)
      }
      if (A.isEmpty) A
      else rotateKStep(A.toList, K).toArray
    }

    println(solution(Array(1, 2, 3, 4, 5), 3).toList)
// List(3, 4, 5, 1, 2)


  @tailrec
  def rotateTailrec(remaining: List[Int], rotationsLeft: Int, buffer: List[Int]): List[Int] = {
    if (remaining.isEmpty && rotationsLeft == 0) remaining
    else if (remaining.isEmpty) rotateTailrec(remaining, rotationsLeft, Nil)
    else if (rotationsLeft == 0) remaining ++ buffer.reverse
    else rotateTailrec(remaining.tail, rotationsLeft - 1, remaining.head :: buffer)
  }

  println(rotateTailrec(List(1,2,3,4,5), 3, List.empty))
//List(4, 5, 1, 2, 3)


}
