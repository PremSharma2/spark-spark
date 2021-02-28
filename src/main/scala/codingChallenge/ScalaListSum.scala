package codingChallenge

object ScalaListSum  extends App {
List(2,3).sum
  def sum(xs: List[Int]): Int = {
    if (xs.isEmpty) throw new IllegalArgumentException("Empty list provided for sum operation")
    def inner(xs:   List[Int]): Int = {
      xs match {
        case Nil => 0
        case x :: tail => xs.head + inner(xs.tail)
      }
    }
    return inner(xs)
  }

}
