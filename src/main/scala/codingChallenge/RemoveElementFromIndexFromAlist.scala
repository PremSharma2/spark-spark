package codingChallenge

import scala.annotation.tailrec

object RemoveElementFromIndexFromAlist extends App {

   def removeAt(index: Int, list: List[Int]): List[Int] = {
    /*
      [1,2,3,4,5].removeAt(2) = removeAtTailrec([1,2,3,4,5], 0, [])
      = removeAtTailrec([2,3,4,5], 1, [1])
      = removeAtTailrec([3,4,5], 2, [2,1])
      = [2,1].reverse ++ [4,5]
      Complexity: O(N)
     */
    @tailrec
    def removeAtTailrec(remaining: List[Int], currentIndex: Int, predecessors: List[Int]): List[Int] = {
      if (currentIndex == index) predecessors.reverse ++ remaining.tail
      else if (remaining.isEmpty) predecessors.reverse
      else removeAtTailrec(remaining.tail, currentIndex + 1, remaining.head :: predecessors)
    }

    if (index < 0) list
    else removeAtTailrec(list,0,List.empty)


  }
}