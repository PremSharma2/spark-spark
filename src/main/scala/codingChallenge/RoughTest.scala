package codingChallenge

object RoughTest  extends App{

  def countDuplicates(numbers: Seq[Int]): Map[Int,Int] = {
    import scala.collection.immutable.Map
    numbers.foldLeft(Map.empty[Int, Int]) { (map, item) =>
      map + (item -> (map.getOrElse(item, 0) + 1))
    }
  }

 val seq= Seq(1,2,2,3,3,4,5)
 println( countDuplicates(seq))
}
