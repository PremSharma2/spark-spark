package codingChallenge

object OtrackTest extends App {

  /* Nil => Nil
   Seq(0) => Seq(1)
   Seq(1, 2, 3) => Seq(1, 2, 4)
   Seq(9, 9, 9) => Seq(1, 0, 0, 0)*/
 // x ::: y ::: z is faster than x ++ y ++ z, because ::: is right associative.
  // x ::: y ::: z is parsed as x ::: (y ::: z),
  // which is algorithmically faster than (x ::: y) ::: z (the latter requires O(|x|) more steps
  /*
  1 :: 2 :: Nil // a list
    list1 ::: list2  // concatenation of two lists

list match {
  case head :: tail => "non-empty"
  case Nil          => "empty"
}



scala> List(1,2,3) ++ List(4,5)
res0: List[Int] = List(1, 2, 3, 4, 5)

scala> List(1,2,3) ::: List(4,5)
res1: List[Int] = List(1, 2, 3, 4, 5)

scala> res0 == res1
res2: Boolean = true
From the documentation it looks like ++ is more general
 whereas ::: is List-specific.
   */

  // Q3
  def incrementByOne(s: Seq[Int]): Seq[Int] = {
    s match {
      case Nil => Nil
      case _ => incrementRec(s.reverse).reverse
    }
  }

  // Seq(3,2,1) - 4,2,1
  def incrementRec(seq: Seq[Int]): Seq[Int] = {
    seq match {
      case Nil => Seq(1)
      case head :: tail if head < 9 => Seq(head + 1) ++ tail
        // Seq(4) ++ Seq(2,1)
      case _ :: tail => Seq(0) ++ incrementRec(tail) // Seq(0) + Seq(0) + Seq(0) + Seq(1)
    }
  }

  println(incrementByOne(Nil))
  println(incrementByOne(Seq(0)))
  println(incrementByOne(Seq(1, 2, 3)))
  println(incrementByOne(Seq(9, 9, 9)))

}
