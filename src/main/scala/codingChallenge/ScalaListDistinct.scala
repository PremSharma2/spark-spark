package codingChallenge

import scala.annotation.tailrec

object ScalaListDistinct extends App {

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

}
