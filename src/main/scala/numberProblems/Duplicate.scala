package numberProblems

import scala.annotation.tailrec

object Duplicate {
  //TODO all numbers in the list appear EXACTLY twice,
  //TODO EXCEPT one: find that number
  //TODO Complexity: O(N^2) because for every element we are calling remainder.count(_ == elemnt) def
  // which is O(n)
  // so it turns out to be O(N^2)
  def duplicates(list:List[Int]):Int={
    @tailrec
    def naive(remainder:List[Int]):Int=
      if(remainder.isEmpty) throw new IllegalArgumentException("List doesn't contain The Non Duplicate Number!!!")
      else {
        val elemnt= remainder.head
        val ementCount= remainder.count(_ == elemnt)
        if(ementCount==1) elemnt
        else naive(remainder.tail)
      }
    //todo Complexity: O(N) time, O(N) space
    @tailrec
    def naiveWithMemory(remainder: List[Int], occurrences: Map[Int, Int] = Map()): Int =
      if (remainder.isEmpty) occurrences.filter(_._2 == 1).head._1
      else {
        val currentNumber = remainder.head
        val currentOccurrences = occurrences.getOrElse(currentNumber, 0)

        naiveWithMemory(remainder.tail, occurrences + (currentNumber -> (currentOccurrences + 1)))
      }

    //todo Complexity: O(N) time, O(1) space with some optimization, at most N/2 elements in the set
    @tailrec
    def withLessMemory(remainder: List[Int], memory: Set[Int] = Set()): Int ={
      if(remainder.isEmpty)memory.head
      else if(memory.contains(remainder.head)) withLessMemory(remainder.tail,memory - remainder.head)
      else withLessMemory(remainder.tail,memory + remainder.head)
    }
    withLessMemory(list)
    //naiveWithMemory(list,Map())
    //naive(list)
  }

  def main(args: Array[String]): Unit = {
    println(duplicates(List(1)))
    println(duplicates(List(1,2,1)))
    println(duplicates(List(1,2,3,2,1)))
    val first1000 = (1 to 100000).toList
    println(duplicates(first1000 ++ List(52369426) ++ first1000))
  }
}
