package lazyevaluation

import scala.collection.generic.FilterMonadic

object LazyEvaluation extends App {
  //Lazy values are evaluated only once in code and only when they are
  // used they get only evaluated
  lazy val x = throw new RuntimeException


  // for example lazy expression
  lazy val x1: Int = {
    println("lazy evaluation")
    42
  }
  println(x1)
  // now x1 has been evaluated only once we will keep getting th same value 42
  // it will not evaluated again
  println(x1)

  //More examples
  def sideEffectsCondition: Boolean = {
    println("Boolean")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectsCondition
  //Here Lazycondition is never evaluated because simpleCondition is already False with And
  // condition so scala is smart it will not evaluate the lazyCondtion
  println(if (simpleCondition && lazyCondition) "YES" else "NO")

  //InConjuction With byName
  //Here By name expression will be evaluated three time because you have used it three times n+n+n
  def byName(n: => Int): Int = n + n + n + 1

  //Here we have make it lazy val so it will be evaluated only once
  def byNameModified(n: => Int): Int = {
    // now it will be evaluated once here because its lazy
    lazy val t = n
    t + t + t + 1
  }

  def retriveMagicValue: Int = {

    //side effect or long computation
    // it is eveluated three times becaus it is call byname
    println("Waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameModified(retriveMagicValue))

  // filtering without lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less then 30")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greter then 20")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  //val lt30=numbers.filter(lessThan30)
  //val gt20=lt30.filter(greaterThan20)
  // println(gt20)
  // Filtering With Lazy vals
  // withFilter works with lazy values
  //def withFilter(p: (A) ⇒ Boolean): FilterMonadic[A, Repr]
  val lt30Lazy: FilterMonadic[Int, List[Int]] = numbers.withFilter(lessThan30)
  val gt20Lazy: FilterMonadic[Int, List[Int]] = lt30Lazy.withFilter(greaterThan20)
  println(gt20Lazy)
  println()
  // Because gt20Lazy is lazy so everything will be evaluated from here and that will be need basis
  // i.e it apply both filters together now
  gt20Lazy.foreach(println)
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // if filter use lazy vals using withFilter
  } yield a + 1 // yield uses map
  //this will translates to
  val forComprehension: Seq[Int] = List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1)
  /*

  implement a lazily evaluated Singly Linked Stream
   */

}
