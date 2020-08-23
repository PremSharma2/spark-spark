package codingChallenge

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
object RoughTest  extends App{

  def countDuplicates(numbers: Seq[Int]): Map[Int,Int] = {
    import scala.collection.immutable.Map
    numbers.foldLeft(Map.empty[Int, Int]) { (map, item) =>
      map + (item -> (map.getOrElse(item, 0) + 1))
    }
  }

 val seq= Seq(1,2,2,3,3,4,5)
 println( countDuplicates(seq))



  def f1 : Future[Unit] = ???
  def f2 : Future[Unit] = ???
  def f3 : Future[Unit] = ???
  def f4 : Future[Unit] = ???

  f1.flatMap(_ => f2.flatMap(_ => f3.flatMap(_=> f4)))
}
