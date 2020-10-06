package codingChallenge
/*
A non-empty zero-indexed array A consisting of N integers is given.
Array A represents numbers on a tape.
  Any integer P, such that 0 < P < N, splits this tape into two non-empty parts:
   A[0], A[1], ..., A[P − 1] and A[P], A[P + 1], ..., A[N − 1].
  The difference between the two parts is the value of:
   |(A[0] + A[1] + ... + A[P − 1]) − (A[P] + A[P + 1] + ... + A[N − 1])|
  In other words,
  it is the absolute difference between the sum of the
  first part and the sum of the second part.
  For example, consider array A such that:
    A[0] = 3
    A[1] = 1
    A[2] = 2
    A[3] = 4
    A[4] = 3
  We can split this tape in four places:
  P = 1, difference = |3 − 10| = 7
  P = 2, difference = |4 − 9| = 5
  P = 3, difference = |6 − 7| = 1
  P = 4, difference = |10 − 3| = 7
  Write a function:
  int solution(int A[], int N);
  that, given a non-empty zero-indexed array A of N integers, returns the minimal difference that can be achieved.
  For example, given:
    A[0] = 3
    A[1] = 1
    A[2] = 2
    A[3] = 4
    A[4] = 3
  the function should return 1, as explained above
 */
object DiffrenceBetweenTwoSubArray extends App {

  def solution(seq: Seq[Int]): Int = {
    val sum = seq.sum

    def getDiff(sumLeft: Int, sumRight: Int): Int = Math.abs(sumLeft - sumRight)

    def findEq(P: Int, sumLeft: Int, curMin: Int): Int =
      if (P == seq.length) curMin
      else findEq(P + 1, sumLeft + seq(P - 1),
        Math.min(curMin, getDiff(sumLeft, sum - sumLeft)))

    findEq(2, seq(0), getDiff(seq(0), sum - seq(0)))
  }

}
