package codingChallenge

import option.OptionExercise.ConnectionApi.Connection

import java.time.ZonedDateTime

//import codingChallenge.RoughTest.{i, map}
import option.OptionExercise.serverConfig

import scala.collection.SeqView
object RoughTest  extends App{

  def countDuplicates(numbers: Seq[Int]): Map[Int,Int] = {
    import scala.collection.immutable.Map
    numbers.foldLeft(Map.empty[Int, Int]) { (map, item) =>
      map + (item -> (map.getOrElse(item, 0) + 1))
    }
  }

 val seq= Seq(1,2,2,3,3,4,5)
  val map: Map[Int, Int] =countDuplicates(seq)
  val list: Seq[(Int, Int)] = map.toList
  list.sortBy(-_._2)
  val indexedTuple: Seq[((Int, Int), Int)] = list.zipWithIndex
 println( countDuplicates(seq))


/*
  def f1 : Future[Unit] = ???
  def f2 : Future[Unit] = ???
  def f3 : Future[Unit] = ???
  def f4 : Future[Unit] = ???

  f1.flatMap(_ => f2.flatMap(_ => f3.flatMap(_=> f4)))
*/

// For Loop and For Comprehension Examples
  /*
  Explanation For every iteration yield will return a sure shot value
  and that value will be wrapped in  existing Functor or Container
  and returned to you

  for(x <- c1; y <- c2; z <- c3) yield {...}
  is translated into

  c1.flatMap(x => c2.flatMap(y => c3.map(z => {...})))


  Like that
   val connection: Option[Connection] =
   it is also like x => f(x).flatMap(g)
     host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))

   */
  val forConnectionStatus: Unit = for {
    h: String <- serverConfig.get("host")
    p: String <- serverConfig.get("port")
    connection <- Connection(h, p)
  }yield  connection.connect



  val nums = Seq(1,2,3)
  val letters = Seq('a', 'b', 'c')
//nums.flatMap(n => letters.flatMap(c => (c,n)))
  val res: Seq[(Int, Char)] = for {
    n <- nums
    c <- letters
  } yield (n, c)

  val monthlyConsumptionAmount =
    Seq(437.8,3339.5,0.0,0.0,0.0,0.0,75.0,99.0,0.0,20.0,66.0)
  val monthNames: Array[String] = Array("Jan", "Feb", "Mar", "Apr", "May",
    "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")



 feedback(monthlyConsumptionAmount)
  def feedback(monthlyConsumptionAmount: Seq[Double]): Unit = {
    val monthNames: Array[String] = Array("Jan", "Feb", "Mar", "Apr", "May",
      "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val result: Unit =for {
      (xs, i) <- monthlyConsumptionAmount.zipWithIndex
    } println(s"Energy use for ${monthNames(i)}: ${"%.2f".format(xs)}")


  }


  val result1: Seq[(String, String)]  =for {
    (xs, i) <- monthlyConsumptionAmount.zipWithIndex
  } yield  monthNames(i) -> "%.2f".format(xs)

//---------------For comprehension on Lazy Seq----------------------------------------------------
  /*
  Except for the Stream class, whenever you create an instance of a Scala collection class,
  you’re creating a strict version of the collection.
  This means that if you create a collection that contains one million elements,
  memory is allocated for all of those elements immediately.
  This is the way things normally work in a language like Java.

In Scala you can optionally create a view on a collection.
A view makes the result non-strict, or lazy.
This changes the resulting collection,
so when it’s used with a transformer method,
the elements will only be calculated as they are accessed, and not “eagerly,”
as they normally would be.

A transformer method is a method that transforms an input collection into
a new output collection, as described in the Discussion.

The signature of the SeqView shows:
      scala.collection.SeqView[Int,scala.collection.immutable.IndexedSeq[Int]] =

Int is the type of the view’s elements.
The scala.collection.immutable.IndexedSeq[Int] portion of the output indicates the
type you’ll get if you force the collection back to a “normal,” strict collection.
   */


  def aggregateMonthlyConsumption(snapshots: Seq[(ZonedDateTime, Double)]): Seq[Double] = {
    // Typical case of For Comprehension here we have explicitly created one seq of range 1 to 12
    // for each element after applying the flat map in each iteration we will
      // calculate one intermediate value
    val out: Seq[Double] = for {
      i <- 1 to 12
      monthWiseTotal = snapshots.view .
        withFilter { case (d, _) => d.getMonthValue() == i } .
        map ( t=> t._2)sum
    } yield monthWiseTotal
    println(out)
    out
  }

  val result: SeqView[(String, String), Seq[_]]  =for {
    (xs, i) <- monthlyConsumptionAmount.view.zipWithIndex
  } yield  monthNames(i) -> "%.2f".format(xs)




/*
  val i = 102119
  var rem = -1
  var num = i
  var lstBuffer = scala.collection.mutable.ListBuffer[(Int,Int)]()
  while(num > 0) {
    rem = num % 10
    if(!lstBuffer.exists(t => t._1 == rem)) {
      lstBuffer.append((rem,1))
    }else{
      var cnt = lstBuffer.filter(t => t._1 == rem).head._2
      lstBuffer -= ((rem,cnt))
      cnt += 1
      lstBuffer.append((rem, cnt))
    }


    lstBuffer.foreach(println)

}

*/


}
