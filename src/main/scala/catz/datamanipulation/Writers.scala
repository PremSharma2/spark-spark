package catz.datamanipulation

import cats.Id
import cats.data.WriterT

object Writers  extends App {
import cats.data.Writer
  /*
    Writer is wrapper over some kind of valuable value denoted by V here
    but you also want keep track of some sought of modifications
    like the Sequence of modification of this valuable value
    or some sought of Logs you want to dump
   TODO
      type  Id[V] = V
      final case class WriterT[F[_], L, V](run: F[(L, V)])
      type Writer[L, V] = WriterT[Id, L, V]
           def apply[L, V](l: L, v: V): WriterT[Id, L, V] = WriterT.apply[Id, L, V]((l, v))
           and WriterT looks like this

   */
  val aWriter: Writer[List[String],Int] = Writer.apply(List("hello scala"),43)
  /*
  def map[Z](fn: V => Z)(implicit functorF: Functor[F]): WriterT[F, L, Z] =
    WriterT {
      functorF.map(run) { z =>
        (z._1, fn(z._2))
      }
    }
   */
  val increasedWriter: WriterT[Id, List[String], Int] = aWriter.map(_+1)
  // TODO and if we want to modify only logs then we will use mapWritten
  val aLogWriter: WriterT[Id, List[String], Int] = aWriter.mapWritten(_ :+"Found interesting ")
  val aWriterWithBoth: WriterT[Id, List[String], Int] = aWriter.bimap(_ :+"Found interesting " , _+1)
  val aWriterWithBoth1: WriterT[Id, List[String], Int] = aWriter.mapBoth{
    (logs,value) => (logs:+ "found something interesting", value+1)
  }

  /*
   TODO
     PAttern gpres like this
     1 define Writer at the beginning
     2 Manipulate them with pure FP
     3 Dump either the value or the Logs
   */
  /*

      def value(implicit functorF: Functor[F]): F[V] =
    functorF.map(run)(_._2)
   */
  val finalResult: Id[Int] = increasedWriter.value

  val logsWritten: Id[List[String]] = aWriterWithBoth.written
  val bothLogsAndValue: (List[String], Int) = aWriterWithBoth.run
  val writerA= Writer.apply(Vector("a","b"),44)
  val writerB= Writer.apply(Vector("c","d"),40)
  // Here problem here is that the Writer Monad is that is has two components Logs and value
  // we have combined values but Writer flatmap also combines the Logs as well
  // because it has to wrap both value and log into context named Writer
  // so logs will e concatinated automatically
  // and two combine List we have Semigroup type class to combine all elements
  // we have to import the SemiGroup[Vector] so that we can combine two vectors
 /*
 TODO Semigroup type class instance for Vector
  class VectorMonoid[A] extends Monoid[Vector[A]] {
    def empty: Vector[A] = Vector.empty
    def combine(x: Vector[A], y: Vector[A]): Vector[A] = x ++ y

  */
  /*
  def flatMap[U](f: V => WriterT[F, L, U])(implicit flatMapF: FlatMap[F], semigroupL: Semigroup[L]): WriterT[F, L, U] =
    WriterT {
      flatMapF.flatMap(run) { lv =>
        flatMapF.map(f(lv._2).run) { lv2 =>
          (semigroupL.combine(lv._1, lv2._1), lv2._2)
        }
      }
    }
   */
  import cats.instances.vector._

  // For example
  writerA.flatMap(intValue => Writer(writerB.written,intValue))
  writerA.flatMap(valueA => writerB.map(valueB=> valueA + valueB))
  // OR
   val compositeWriter = for{
     valueA <- writerA
     valueB <- writerB
   } yield valueA+valueB
  //Lets test this
  println(compositeWriter.run)

  // What if we want to clear the Logs
  import cats.instances.list._ // Monoid[List[Int]]
  /*
  Is is used monoid because we dint which Monad is this
  for Example we dint know it is List, Seq , Option ,Vector
  def reset(implicit monoidL: Monoid[L], functorF: Functor[F]): WriterT[F, L, V] =
    mapWritten(_ => monoidL.empty)
   */
  aWriter.mapWritten(logs=> List.empty)
  val anEmptyWriter= aWriter.reset // clear the logs , and keep the desired value inside

  //TODO
  // Write a function that print thing with Writers
  def countAndSay(n:Int) : Unit ={
    if(n<0) println("starting")
    else {
      countAndSay(n-1 )
      println(n)
    }
  }
  println(countAndSay(10))
  //modify this with writer
  def countAndLog(n:Int):Writer[Vector[String],Int] = {

  }
}
