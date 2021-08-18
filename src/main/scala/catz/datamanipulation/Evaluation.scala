package catz.datamanipulation

object Evaluation  {

  /*
  TODO Eager and Lazy Expression evaluation by type Classes : ->

  TODO
     cats makes a distinction between
    - evaluating an expression eagerly
    - evaluating lazily  and every time you request it
    - evaluating lazily and keeping the value
    TO implement this feature cats API
    import cats.Eval Monad
    Eval is a monad which controls evaluation. in three different ways
    This type wraps a value (or a computation that produces a value) and
   can produce it on command via the .value method.
   There are three basic evaluation strategies:
   Now: evaluated immediately
   Now: eagerly evaluated
   Later: evaluated once when value is needed and value is cached
   Always: evaluated every time value is needed by call by name expression
   The Later and Always are both lazy strategies while Now is eager.
   Later and Always are distinguished from each other only by memoization:
   once evaluated Later will save the value to be returned immediately if it is needed again.
    Always will run its computation every time.
   */
  import cats.Eval
  //def now[A](a: A): Eval[A] = Now(a)
val instantEval: Eval[Int] = Eval.now{
  println("eagerly evaluated Expression !!!!!")
  4
}

  // TO access the o/p of this Eagerly evaluated Expression
  // we have this instantEval.value
  //println(instantEval.value)
  /*

     TODO
       Construct a lazy Eval[A] instance.
       This type can be used for "lazy" values or expressions.
       In some sense it is equivalent to using a Function0 value.
       This type will evaluate the computation every time the value is required.
       to be evaluated
       It should be avoided except when laziness is required and caching must be avoided.
      Generally, prefer Later.
TODO
   final class Always[A](f: () => A) extends Eval[A] {
  def value: A = f()
  def memoize: Eval[A] = new Later(f)
}
   */
  val redoEval: Eval[String] = Eval.always{
    println("Lazily evaluated Expression !!!!!")
    "Lazy-Evaluated-Expression-Output"
  }
  //println(redoEval.value)
  //println(redoEval.value)
  /*
  TODO
   Construct a lazy Eval[A] value with caching (i.e. Later[A]).
    def later[A](a: => A): Eval[A] = new Later(() => a)
   final class Later[A](f: () => A) extends Eval[A]
   */
  val delayedEval: Eval[String] = Eval.later{
    println("evaluating lazily and keeping the value")
     "Evaluated lazily and kept the value in caeche"
  }
 // println(delayedEval.value)
 // println(delayedEval.value)

  /* TODO
        Computation performed in eval call by name expression
         is always lazy, even when called on an
   *    eager (Now) instance.
        Lazily perform a computation based on an Eval[A], using the
   *     function `eval` to produce an Eval[B] given an A.
        This call is stack-safe -- many .flatMap calls may be chained without consumed
        additional stack during evaluation.
        It is also written to avoid left-association problems,
        so that repeated calls to .flatMap will be efficiently applied.
       Computation performed in f is always lazy,
       even when called on an eager (Now) instance.
   */
  /*
TODO
   FlatMap is a type of Eval[A] that is used to chain computations or Lazy expressions
   * involving .map and .flatMap. Along with Eval#flatMap it
   * implements the trampoline that guarantees stack-safety.
   *
   * Users should not instantiate FlatMap instances
   * themselves. Instead, they will be automatically created when
   * needed.
   *
   * Unlike a traditional trampoline, the internal workings of the
   * trampoline are not exposed. This allows a slightly more efficient
   * implementation of the .value method.
TODO
  sealed abstract class FlatMap[A] extends Eval[A] { self =>
    type Start
    val start: () => Eval[Start]
    val run: Start => Eval[A]

    def memoize: Eval[A] = Memoize(this)
    def value: A = evaluate(this)
  }




   */

  val alwaysEval: Eval[Int] = Eval.now{
    println("eagerly evaluated Expression for Testing !!!!!")
    404
  }
 val alwaysTest: Eval[Int] = alwaysEval.map(ev2 => ev2+1)


  val ComposedEvaluation: Eval[String] = instantEval.
    flatMap(value1 => delayedEval.map(value2=> value1 + value2))
  //println(ComposedEvaluation.value)


  val forcomposedEvaluation: Eval[String] = for{
    value1 <-  instantEval
    value2 <-  delayedEval
  } yield  (value1 + value2)

  val ex: Eval[String] = for{
    a <- delayedEval
    b <-  redoEval
    c <-  instantEval
    d <-  redoEval
  } yield a + b+ c+d
 // println(ex.value)
 // println(ex.value)
  // TODO now this will not be recomputed again and again
  val dontRecompute = redoEval.memoize
  //println(dontRecompute.value)
 // println(dontRecompute.value)


  val tutorial : Eval[String] = Eval
    .always{ println("Step 1:  !!!!!") ; "put the guitar on your lap" }
    .map   {step1 => println("Step2!!"); s"$step1 then put your left hand on the neck"}
    .memoize // upto here it will be evaluated only once because we have used memoize
    .map{step12 => println("Step3 !!"); s"$step12 then with right hand strike the strings!!!"}

 // println(tutorial.value)
  //println(tutorial.value)
  // TODO : implement defer such that defer Eval.now should not run side effects
  /* TODO Note:
        Computation performed in eval call by name expression
       (eval : =>Eval[T])  is always lazy, even when called on an
   *    eager (Now) instance.
        Lazily perform a computation based on an Eval[A], using the
   *     function `eval` to produce an Eval[B] given an A.
   We have delayed the expression evaluation by using later
   */

  def defer [T](eval : =>Eval[T]):Eval[T] ={
   Eval.later(()).flatMap(_ => eval)
  }
  defer(Eval.now{
    println("Now")
    4
  }).value

  /*
 TODO
    Scala cats implementation if Defer APi
    sealed abstract class Defer[A](val thunk: () => Eval[A]) extends Eval[A] {
    def memoize: Eval[A] = Memoize(this)
    def value: A = evaluate(this)
  }
   */

  // TODO Exercise 3
  def reverseList[T](list: List[T]): List[T] =
    if(list.isEmpty) list
    else reverseList(list.tail) :+list.head

  def reverseEval[T](list : List[T]) : Eval[List[T]] = {
    if(list.isEmpty) Eval.now(list)
   //else reverseEval(list.tail).map(_:+list.head)
    // better design is to go with
    else defer(reverseEval(list.tail).map(_:+list.head))

  }

  def main(args: Array[String]): Unit = {
    // defer(reverseEval(list.tail).map(_:+list.head))
    // This will create a chain of  Eval.later(()).flatMap(_ => eval) in stack
    // which is by nature stack safe so that in turn makes this stack safe
    println(reverseEval((1 to 5).toList).value)

 //println(alwaysTest.value)
  }

}
