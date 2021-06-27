package concurrency

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
object FutureWithOnCompleteAnalysis  extends App {

def calculateMeaningOfLife()={
  Thread.sleep(2000)
  42
}
  /*
    implicit lazy val global: ExecutionContextExecutor =
    impl.ExecutionContextImpl.fromExecutor(null: Executor)
    It handles the Thread Execution in scala For this Future Task we created or Future Object

   */
  //  def apply[T](body: =>T)(implicit @deprecatedName('execctx) executor: ExecutionContext):
  //  Future[T] = impl.Future(body)
  /*
  This is Future.apply method body
  As we can see that this callByName expression is evaluated only by
  Thread executor hence this expression is executed within the thread

  val runnable = new PromiseCompletingRunnable(body)
    executor.prepare.execute(runnable)
    runnable.promise.future
   */
val aFuture: Future[Int] = Future.apply{
  calculateMeaningOfLife
}
  // here now it will get executed bcz value is callByname expression calculateMeaningOfLife
  /*
  TODO
      The current value of this Future.
       Note: using this method yields nondeterministic dataflow programs.
      If the future was not completed the returned value will be None.
     If the future was completed the value will be Some(Success(t))
    if it contained a valid result, or Some(Failure(error)) if it contained an exception.
   */
   println(aFuture.value) //it returns an option of try Option[Try[Int]]
  val valueReturnedByThread: Any =aFuture.value.getOrElse(42)
   println("Waiting for the Future ")
  // Here we reomved t => t match {} because it is a partial functionso we can write this way
  //TODO you can use this call back to return values from the Future
  val futureResult: Unit =aFuture.onComplete{
    case  Success(value) => println(s"Thread is completed with the value $value")
    case  Failure(exception) => println(s"I have failed with exception $exception")
  }
  //or

  val futurePartialFunction: PartialFunction[Try[Int], Option[Int]] = {
    case  Success(value) => Some(value)
    case Failure(exception) => None

  }
  val futureResult2: Unit =aFuture.onComplete(futurePartialFunction)
  Thread.sleep(2000)
}
