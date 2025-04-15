package scalaBasics

import scala.concurrent.Future
import scala.util.Try

object CallByNameInDepth {
/*
TODO
  Call-by-Name (CBN) is one of those Scala features
  that had more confused programmers than happy programmers.
  It’s deceptively powerful, often abused, most of the time misused,
  almost never employed to its full potential, and it has a terrible name.
  I’m not going to use 3-dollar words to describe what CBN does.
  Instead, here’s the thing: if you write a normal function
 */
private def byValueFunction(x: Int) = x+2
  byValueFunction(2 + 3)
  /*
TODO Explanation
  the argument is evaluated before the function is invoked.
  So before the function call, 2 + 3 becomes 5 and
  you actually call byValueFunction(5).
  This has been the case since the beginning of time with most programming languages.
  However, you can also mentally conceive a mechanism where you could pass 2 + 3 just as it is,
  and let the function evaluate it whenever it wants, if it needs to.
  The arrow sign before the Int type means
  “I’ll take an Int expression literally and
  I’ll take care to evaluate it if I need to”. So when you call
   */

  def byNameFunction1(x: => Int): Unit ={
    println(x)
    println(x)
  }

  val expression: Int = 2 + 3
  byNameFunction1(expression)




  /*
TODO Explanation
   what you’re passing is not the value of 2 + 3,
   but the expression 2 + 3, literally, as it is.
   The function is now responsible for evaluating the expression
   by simply using the argument name, which is,
   I suspect, where the terrible and very confusing “call-by-name” name came from.
   In the creators’ defense, I can’t find a simple,
   one-two-punch name for this either.
   Call-by-expression? Call-literally? Phony call? DETISS (don’t evaluate till I say so)?
   */
  /*
TODO Trick 1
   Trick 1 - reevaluation
   So this call-by-name mechanism is actually very powerful and
   allows greater expressiveness in Scala.
   he first example I want to show is that
   when you pass a by-name argument,
   you can evaluate it as many times as you like.
   “Why would you want that? 2 + 3 evaluates to 5 at every hour of the day.”
   Yes, but not quite everything does.
   */
  def byValueFunction(x: Long): Unit = {
    println(x)
    println(x)
  }

  def byNameFunction(x: => Long): Unit = {
    println(x)
    println(x)
  }
  /*
TODO Explanation
  180615853269641
  180615853294140
 that is, two different results.
 You can probably guess why:
 because the expression is passed literally and
  it’s being used twice, then it’s evaluated twice, at different moments.
 There is great power - and great responsibility -
 in being able to reevaluate your arguments as you see fit. But more on that, another time.
   */

/*
TODO Trick 2 callbyNeed: ->
 Call-by-name used in conjunction with lazy evaluation -
 which is another beast - allows us to manage infinite data structures in Scala:
 If your Generic class creates or contains elements of type T it should be +T
  here LazyList contains the elements of type T
  In short Containers are always covariant
 */

  //TODO : data modelling in terms  of ADTS of List

abstract class LazyList[+T] {
  def head: T
  def tail: LazyList[T]
}

  case object Empty extends LazyList[Nothing] {
    override def head: Nothing = throw new NoSuchElementException
    override def tail: LazyList[Nothing] = throw new NoSuchElementException
  }



  class NonEmpty[+T](h: => T, t: => LazyList[T]) extends LazyList[T] {
    override lazy val head: T = h
    override lazy val tail: LazyList[T] = t
  }


  /*
TODO Explanation
  The magic happens in the NonEmpty class.
  because we’re using by-name arguments,
  that means the arguments are not evaluated until used.
  Because the only places where we evaluate the arguments are the initialization of lazy values -
  which also trigger on first use - it means that neither the arguments,
  nor the internal fields are evaluated on construction.
  Even better - if the fields are ever used,
  they’re evaluated once and then reused,
  so no more System.nanoTime shenanigans
  if you’re afraid of them. If you’re brave, you can also use something like
   */
/*
TODO
 Trick 3 - hold the door
 The third and most powerful aspect of CBN is that
 it prevents the computation of the argument
 so that the expression can be handled in some other way.
 For example, handled safely in case it threw something:
 If not for CBN, the Try wrapper would have been impossible
 because the argument would not have any choice but to be evaluated first,
 and blow up in our face.
 Scala makes it more beautiful by combining that with the curly brace syntax
 for single-argument functions, which makes Try look like it belonged to the language itself:
 */
val anAttempt: Try[Int] = Try(throw new NullPointerException)

  val anAttempt1: Try[Int] = Try {
    // something that can blow up
    throw new NullPointerException
  }

 //todo Same with a Future - if not for CBN, you couldn’t pass an expression to be evaluated on another thread:
import scala.concurrent.ExecutionContext.Implicits.global
  val aFuture = Future {
    // some nasty computation
    42
  }
}
