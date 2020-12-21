package catz.datamanipulation

object Evaluation extends App {

  /*
  TODO Eager and Lazy Expression evaluation by type Classes : ->
  TODO
     cats makes a distinction between
    - evaluating an expression eagerly
    - evaluating lazily  and every time you request it
    - evaluating lazily and keeping the value
    TO implement this feature cats API
    import cats.Eval
    Eval is a monad which controls evaluation.
   This type wraps a value (or a computation that produces a value) and
   can produce it on command via the .value method.
   There are three basic evaluation strategies:
   Now: evaluated immediately
   Later: evaluated once when value is needed
   Always: evaluated every time value is needed
   The Later and Always are both lazy strategies while Now is eager.
   Later and Always are distinguished from each other only by memoization:
   once evaluated Later will save the value to be returned immediately if it is needed again.
    Always will run its computation every time.
   */
  import cats.Eval
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
       This type can be used for "lazy" values.
       In some sense it is equivalent to using a Function0 value.
       This type will evaluate the computation every time the value is required.
       It should be avoided except when laziness is required and caching must be avoided.
      Generally, prefer Later.

      final class Always[A](f: () => A) extends Eval[A] {
  def value: A = f()
  def memoize: Eval[A] = new Later(f)
}
   */
  val redoEval: Eval[String] = Eval.always{
    println("Lazily evaluated Expression !!!!!")
    "Lazy-Evaluated-Output"
  }
  //println(redoEval.value)
  //println(redoEval.value)
  val delayedEval = Eval.later{
    println("evaluating lazily and keeping the value")
     "Evaluated lazily and kept the value in caeche"
  }
 // println(delayedEval.value)
 // println(delayedEval.value)

  val ComposedEvaluation = instantEval.
    flatMap(value1 => delayedEval.map(value2=> value1 + value2))
  //println(ComposedEvaluation.value)


  val forcomposedEvaluation: Eval[String] = for{
    value1 <- instantEval
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
  val dontRecompute = redoEval.memoize
  //println(dontRecompute.value)
 // println(dontRecompute.value)


  val tutorial : Eval[String] = Eval
    .always{ println("Step 1:  !!!!!") ; "put the guitar on your lap" }
    .map{step1 => println("Step2!!"); s"$step1 then put your left hand on the neck"}
     .memoize // upto here it will be evaluated only once because we have used memoize
    .map{step12 => println("Step3 !!"); s"$step12 then with right hand strike the strings!!!"}

 // println(tutorial.value)
  //println(tutorial.value)
  // TODO : implement defer such that defer Eval.now should not run side effects
  def defer [T](eval : =>Eval[T]):Eval[T] ={
   Eval.later(()).flatMap(_ => eval)
  }
  defer(Eval.now{
    println("Now")
    4
  }).value


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
  println(reverseEval((1 to 5).toList).value)
}
