package codingChallenge

object BinaryGap extends App {
    // 100%
    def solution(N: Int): Int = {
      val list = "(?<=1)0+(?=1)".r.findAllIn(N.toBinaryString).toList
      if (list.isEmpty) 0
      else list.maxBy(_.length).length
    }

    solution(15)

}
