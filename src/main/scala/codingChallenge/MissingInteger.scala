package codingChallenge

object MissingInteger extends App {

  def solution(A: Array[Int]): Int = {
    def findMissing(in: Int, l: List[Int]): Int = {
      if (l.isEmpty || l.head != in) in
      else findMissing(in + 1, l.tail)
    }
    findMissing(1, A.toList.filter(_ > 0).distinct.sorted)
  }

  println(solution(Array(1,3,6,4,1,2)))

}
