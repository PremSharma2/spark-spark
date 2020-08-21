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
  def inSequence[A, B](firstFuture: Future[A], secondFutureTask: Future[B]): Future[B] =
    firstFuture.flatMap(_ => secondFutureTask)


  val future: PartialFunction[Try[Int], Unit] = {
    case  Success(value) => println(s"Thread is completed with the value $value")
    case Failure(exception) => println(s"I have failed with exception $exception")

  }
  // implement new future with a first value of two futures
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
// here promise is a controller for fa here basically
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
  def bestsafeImpl[A](fa: Future[A], fb: Future[A]): Future[A] = {
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

  def mostOptimizedSafeImpl[A](fa: Future[A], fb: Future[A]): Future[A] = {
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
    Tries to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.
*  @return    If the promise has already been completed returns `false`, or `true` otherwise.

     */
    fb.onComplete(promise.tryComplete(_))
    promise.future
  }

}
