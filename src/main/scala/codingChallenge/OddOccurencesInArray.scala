package codingChallenge

object OddOccurencesInArray  extends App {

  def solution2(A: Array[Int]): Int = {
    def getUnpaired(list: List[Int]): Int = {
      //def count(p: (A) => Boolean): Int
      // creating a match pair
      val occHead = list.count(_ == list.head)
      if (occHead % 2 == 0) getUnpaired(list.filter(_ != list.head))
      else list.head
    }
    getUnpaired(A.toList)
  }

  val ar1: Array[Int] = Array(9, 3, 9, 3,9,7,9)
  println(solution2(ar1))
}
