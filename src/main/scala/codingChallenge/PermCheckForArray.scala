package codingChallenge
/*
A non-empty zero-indexed array A consisting of N integers is given.
A permutation is a sequence containing each element from 1 to N once, and only once.
For example, array A such that:
A[0] = 4
A[1] = 1
A[2] = 3
A[3] = 2
is a permutation, but array A such that:
A[0] = 4
A[1] = 1
A[2] = 3
is not a permutation, because value 2 is missing.
The goal is to check whether array A is a permutation.
Write a function:
object Solution { def solution(A: Array[Int]): Int }
that, given a zero-indexed array A, returns 1 if array A is a permutation and 0 if it is not.
For example, given array A such that:
A[0] = 4
A[1] = 1
A[2] = 3
A[3] = 2
the function should return 1.
Given array A such that:
A[0] = 4
A[1] = 1
A[2] = 3
the function should return 0.

 */
object PermCheckForArray  extends App {

  def solution(A: Array[Int]): Int = {
    def isPerm(list: List[Int]): Int = {
      if (list.tail.isEmpty) 1
      else if (list.tail.head != list.head + 1) 0
      else isPerm(list.tail)
    }

    val list: List[Int] = A.toList.sorted
    if (list.head != 1) 0
    else if (list.last != list.size) 0
    else isPerm(list)
  }

  println(solution(Array(4, 1, 3, 2)))

}
