package codingChallenge

object MaximumElementInList  extends App {

  def max(xs: List[Int]): Option[Int] = xs match {
    case Nil => None
    case List(x: Int) => Some(x)
    case x :: y :: restOfElements => max( (if (x > y) x else y) :: restOfElements )
  }

}
