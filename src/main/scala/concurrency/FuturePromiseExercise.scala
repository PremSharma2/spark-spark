package concurrency
import org.apache.spark.sql.catalyst.expressions.Second

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

object FuturePromiseExercise  extends App {

  def fulFillImmediately[T](callByNameExpression: T): Future[T] = Future.apply {
    callByNameExpression
  }

  // running two future task in sequence
  def inSequence[A, B](firstFuture: Future[A], secondFuture: Future[B]): Future[B] =
    firstFuture.flatMap(_ => secondFuture)

  // implement new future with a first value of two futures
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
// here promise is a controller for fa here basically
    // i.e it is fot Type[A]
  val promise = Promise[A]
    fa.onComplete{
      case  Success(value) => promise.success(value)
      case Failure(exception) => promise.failure(exception)
    }
// now again here as awe can see promise is already fulfilled and again we are asking
    // to fulfill the promise so it will throw an exception so we need to wrap the code
    // with try and catch we will do this in new function
    fb.onComplete{
      case  Success(value) => promise.success(value)
      case Failure(exception) => promise.failure(exception)
    }
    promise.future
}

// new function that hanldes exception but we need to refactor it with auxiallary function
  def safefirst[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // here promise is a controller for fa here basically
    val promise = Promise[A]
    fa.onComplete{
      case  Success(value) => try{
        promise.success(value)
      } catch{
        case _ =>
      }
      case  Failure(value) => try{
        promise.failure(value)
      } catch{
        case _ =>
      }
    }
    fb.onComplete{
      case  Success(value) => try{
        promise.success(value)
      } catch{
        case _ =>
      }

      case  Failure(value) => try{
        promise.failure(value)
      } catch{
        case _ =>
      }
    }
    promise.future
  }

// refactored Impls for safe
  def modifiedSafeImpl[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // here promise is a controller for fa here basically
    val promise = Promise[A]
    /*
    Tries to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.
     */
    def tryComplete(promise:Promise[A] , result: Try[A]) = result match {
      case  Success(value) => try{
        promise.success(value)
      } catch{
        case _ =>
      }
      case  Failure(value) => try{
        promise.failure(value)
      } catch{
        case _ =>
      }
    }
    // we can read this like this the future task result i.e future object
    // with this promise try to fulfill the promise
    fa.onComplete(result => tryComplete(promise, result))
    // or we can do this
    /*
    Tries to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.


     */
    fb.onComplete(result => tryComplete(promise, result))
    promise.future
  }
// same thing above we did but with inbuilt scala concurrent api
  def mostOptimizedFirstImpl[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // here promise is a controller for fa here basically
    val promise = Promise[A]
    /*
    Tries to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.
     */

    // we can read this like this the future task result i.e future object
    // with this promise try to fulfill the promise
    fa.onComplete(promise.tryComplete(_))
    // or we can do this
    /*
    promise.tryComplete(_) this function will
    Try to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.
*  @return If the promise has already been completed returns `false`, or `true` otherwise.

     */
    fb.onComplete(promise.tryComplete(_))
    promise.future
  }
  // This will return the last promised future out of the two
 def last[A](fa: Future[A], fb: Future[A]): Future[A] ={
   // initially both the threads try to fulfill the bothPromise
   // but whoever finishes first will fulfill first hence second
   // one cant finish the same promise
   // because same promise cant be fulfilled twice as we discussed earlier
   // so for second thread or the remaining thread can fulfill the second promise


val bothPromise= Promise[A]
   val lastPromise= Promise[A]
// i am saying that if for this Future Task this promise is already fulfilled
   // by another Future Task
   // then this Future task should complete another promise

   val checkAndComplete: Try[A] => Any = (result: Try[A]) => {
     if(!bothPromise.tryComplete(result))
       lastPromise.complete(result)
   }
   fa.onComplete(checkAndComplete)
   fb.onComplete(checkAndComplete)
lastPromise.future
 }

  val fastFuture = Future.apply{
    Thread.sleep(100)
    42
  }
  val slowFuture = Future.apply{
    Thread.sleep(200)
    62
  }

 val result: Future[Int] =mostOptimizedFirstImpl(fastFuture,slowFuture)

  result.foreach(println)

  Thread.sleep(1000)
}
