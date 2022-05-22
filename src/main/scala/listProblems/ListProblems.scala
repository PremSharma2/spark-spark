package listProblems

import scala.annotation.tailrec

object ListProblems  extends App{
  //[(1,0),(1,1),(2,2),(2,3)]
  /*
  (1 to 5).iterator.sliding(3).toList
  res18: List[Sequence[Int]] = List(List(1, 2, 3), List(2, 3, 4), List(3, 4, 5))
  [[(1,0),(1,1)],[(1,1),(2,2)],[(2,2),(2,3)]]
   */
  def removeDuplicates[T](l: List[T]): List[T] = l.iterator.zipWithIndex
    .sliding(2,1)
    .collect({
      case Seq((onlyElement, _)) =>
        Seq(onlyElement)
      case Seq((element1, 0), (element2, _)) =>
        if (element1 == element2) Seq(element1) else Seq(element1, element2)
      case Seq((element1, _), (element2, _)) if element1 != element2 =>
        Seq(element2)
    })
    .flatten
    .toList

//Find the Odd Occurrences in an Array
  def solution2(A: Array[Int]): Int = {
    def getUnpaired(list: List[Int]): Int = {
      //def count(p: (A) => Boolean): Int
      // creating a match pair
      val occHead: Int = list.count(_ == list.head)
      if (occHead % 2 == 0) getUnpaired(list.filter(_ != list.head))
      else list.head
    }
    getUnpaired(A.toList)
  }


  //
  def maxSubArray(array: Array[Int]):Tuple3[Int,Int,Int] = {
    var sum, maxSum, startIndex, endIndex, maxStartIndexUntilNow = 0

    for (currentIndex <- array.indices) {
      sum += array(currentIndex)

      if (array(currentIndex) > sum) {
        sum = array(currentIndex)
        startIndex = currentIndex
      }

      if (sum > maxSum) {
        maxSum = sum
        startIndex = maxStartIndexUntilNow
        endIndex = currentIndex
      } else if (sum < 0) {
        maxStartIndexUntilNow = currentIndex + 1
        sum = 0;
      }
    }
    (maxSum,startIndex, endIndex)
  }
  val list: List[Int]  =maxSubArray( Array(3, -1, -1, -1, -1, -1, 2, 0, 0, 0)).productIterator.toList.map(_.toString.toInt)
println(list.sum)
  println(maxSubArray( Array(3, -1, -1, -1, -1, -1, 2, 0, 0, 0)))
  //(3,0,0)

  println(maxSubArray( Array(-1, 3, -5, 4, 6, -1, 2, -7, 13, -3)))
  //(17,3,8)

  println(maxSubArray( Array(-6,-2,-3,-4,-1,-5,-5)))
  //(0,0,0)


  implicit class RRichInt(n: Int) { // our rich int
    /**
     * Easy problems
     */
    // check if a number is prime
    def isPrime: Boolean = {
      /*
        isPrime(11) = ipt(2)
        = 11 % 2 != 0 && ipt(3)
        = ipt(3)
        = 11 % 3 != 0 && ipt(4)
        = ipt(4)
        = true
        isPrime(15) = ipt(2)
        = 15 % 2 != 0 && ipt(3) = ipt(3)
        = 15 % 3 != 0 && ipt(4)
        = false
        Complexity: O(sqrt(N))
       */
      @tailrec
      def isPrimeTailrec(currentDivisor: Int): Boolean = {
        if (currentDivisor > Math.sqrt(Math.abs(n))) true
        else n % currentDivisor != 0 && isPrimeTailrec(currentDivisor + 1)
      }

      if (n == 0 || n == 1 || n == -1) false
      else isPrimeTailrec(2)
    }

    // the constituent prime divisors
    def decompose: List[Int] = {
      assert(n >= 0)

      /*
        decompose(11) = decomposeTailrec(11, 2, [])
        = decomposeTailrec(11, 3, [])
        = decomposeTailrec(11, 4, [])
        = [11]
        decompose(15) = decomposeTailrec(15, 2, [])
        = decomposeTailrec(15, 3, [])
        = decomposeTailrec(5, 3, [3])
        = [5,3]
        decompose(16) = decomposeTailrec(16, 2, [])
        = decomposeTailrec(8, 2, [2])
        = decomposeTailrec(4, 2, [2,2])
        = decomposeTailrec(2, 2, [2,2,2])
        = [2,2,2,2]
        Complexity: O(sqrt(N)); can be as low as O(log(N))
       */
      @tailrec
      def decomposeTailrec(remaining: Int, currentDivisor: Int, accumulator: List[Int]): List[Int] = {
        if (currentDivisor > Math.sqrt(remaining)) remaining :: accumulator
        else if (remaining % currentDivisor == 0) decomposeTailrec(remaining / currentDivisor, currentDivisor, currentDivisor :: accumulator)
        else decomposeTailrec(remaining, currentDivisor + 1, accumulator)
      }

      decomposeTailrec(n, 2, List())
    }
  }


  def distinct[A](ls: List[A]) = {
    def loop(set: Set[A], ls: List[A]): List[A] = ls match {
      case hd :: tail if set contains hd => loop(set, tail)
      case hd :: tail => hd :: loop(set + hd, tail)
      case Nil => Nil
    }

    loop(Set(), ls)
  }

  def distinct1(ls: List[Int]) = {
    @tailrec
    def distinctTailRec(remaining:List[Int] , accumlator:List[Int]) :List[Int] ={
      if(remaining.isEmpty) accumlator
      else if(accumlator.contains(remaining.head)) distinctTailRec(remaining.tail, accumlator)
      else distinctTailRec(remaining.tail,remaining.head :: accumlator)
    }
    distinctTailRec(ls,Nil)
  }
  distinct1(List(1,2,3,3,5))
  // sum the list of Option[Int]
  def sumList[ T ] (list: List[Option[T]])(implicit ev: Numeric[T]): Option[T] = {
    list.foldLeft(Option(ev.zero)) { case (acc, el) =>
      el.flatMap(value => acc.map(ac => ev.plus(ac, value)))
    }
  }

  def sum(xs: List[Int]): Int = {
    if (xs.isEmpty) throw new IllegalArgumentException("Empty list provided for sum operation")
    def inner(xs:   List[Int]): Int = {
      xs match {
        case Nil => 0
        case x :: tail => xs.head + inner(xs.tail)
      }
    }
     inner(xs)
  }
}
