package codingChallenge

object CodlityListrotationTest extends App {

    // 100%
    def solution(A: Array[Int], K: Int): Array[Int] = {
      def rotateKStep(l: List[Int], K: Int): List[Int] = {
        def rotateOneStep(l: List[Int]) = {
          l.take(l.size - 1).+:(l.last)
        }
// if k==0 do not rotate and return from here
        if (K == 0) l
          // else rotate one step
        else rotateKStep(rotateOneStep(l), K - 1)
      }
      if (A.isEmpty) A
      else rotateKStep(A.toList, K).toArray
    }

    println(solution(Array(1, 2, 3, 4, 5), 3).toList)


}
