package codingChallenge
/*
 You are given N counters, initially set to 0, and you have two possible operations on them:
   increase(X) − counter X is increased by 1,
 max counter − all counters are set to the maximum value of any counter.
 A non-empty zero-indexed array A of M integers is given. This array represents consecutive operations:
 if A[K] = X, such that 1 ≤ X ≤ N, then operation K is increase(X),
 if A[K] = N + 1 then operation K is max counter.
   For example, given integer N = 5 and array A such that:
   A[0] = 3
 A[1] = 4
 A[2] = 4
 A[3] = 6
 A[4] = 1
 A[5] = 4
 A[6] = 4
 the values of the counters after each consecutive operation will be:
   (0, 0, 1, 0, 0)
 (0, 0, 1, 1, 0)
 (0, 0, 1, 2, 0)
 (2, 2, 2, 2, 2)
 (3, 2, 2, 2, 2)
 (3, 2, 2, 3, 2)
 (3, 2, 2, 4, 2)
 The goal is to calculate the value of every counter after all operations.

 */
object MaxCounters  extends App {


 def solution(N: Int, A: Array[Int]): Array[Int] = {
   val counters: Array[Int] = Array.ofDim(N)

   def increaseCounter(x: Int) = counters(x - 1) = counters(x - 1) + 1

   def maxCounters() = {
     val max = counters.max
     for (i <- counters.indices){
       counters(i) = max
     }
   }

   def getCounters(ops: List[Int]): Array[Int] = {
     if (ops.isEmpty) counters
     else {
       ops.head match {
         case x: Int if x >= 1 && x <= N => increaseCounter(x)
         case x: Int if x > N => maxCounters()
       }
       getCounters(ops.tail)
     }
   }

   getCounters(A.toList)
 }

 val ops = Array(3, 4, 4, 6, 1, 4, 4)
 println(solution(5, ops))
}
