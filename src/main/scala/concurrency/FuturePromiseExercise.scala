package concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object FuturePromiseExercise extends App {

  def fulFillImmediately[T](callByNameExpression : T): Future[T] =
    Future.apply {
    callByNameExpression
  }


  // accessing two future task in sequence
  def inSequence[A, B](firstFuture: Future[A], secondFuture: Future[B]): Future[B] =
    firstFuture.flatMap(_ => secondFuture)

  // or
  /*
    def f1 : Future[Unit] = ???
    def f2 : Future[Unit] = ???
    def f3 : Future[Unit] = ???
    def f4 : Future[Unit] = ???

    f1.flatMap(_ => f2.flatMap(_ => f3.flatMap(_=> f4)))

   */
  // implement new future with a first value of two futures
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // here promise is a controller for fa here basically
    // i.e., it is for Type[A]
    val promise = Promise[A]
    // calling a callback function of scala concurrency
    fa.onComplete {
      case Success(value) => promise.success(value)
      case Failure(exception) => promise.failure(exception)
    }
    // now again here as awe can see promise is already fulfilled and again we are asking
    // to fulfill the promise so it will throw an exception so we need to wrap the code
    // with try and catch we will do this in new function
    fb.onComplete {
      case Success(value) => promise.success(value)
      case Failure(exception) => promise.failure(exception)
    }
    promise.future
  }

  // new function that hanldes exception but we need to refactor it with auxiallary function
  def safefirst[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // here promise is a controller for fa here basically
    val promise = Promise[A]

    val rs: Unit = fa.onComplete {
      case Success(value) => try {
        promise.success(value)
      } catch {
        case _ => " Promise fulfilled, again you are asking to fulfill"
      }
      case Failure(value) => try {
        promise.failure(value)
      } catch {
        case _ =>
      }
    }


    val rs1: Unit = fb.onComplete {
      case Success(value) => try {
        promise.success(value)
      } catch {
        case _ =>
      }

      case Failure(value) => try {
        promise.failure(value)
      } catch {
        case _ =>
      }
    }
    promise.future
  }

  // refactored Impls for safe
  def modifiedSafeImpl[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // here promise is a controller for fa here basically
    // or promise is guarantee for a task of the future
    val promise = Promise[A]

    /*
    Tries to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.
     */
    def tryCompletePromise(promise: Promise[A], result: Try[A]) = {
      result match {

        case Success(value) => try {
          promise.success(value)
        } catch {
          case _ =>
        }
        case Failure(value) => try {
          /*
         promise.failure(value)
        The throwable to complete the promise with.
         If the throwable used to fail this promise is an error,
         a control exception or an interrupted exception,
        it will be wrapped as a cause within an ExecutionException which will fail the promise.
       If the promise has already been fulfilled,
         failed or has timed out, calling this method will throw an IllegalStateException.
         */
          promise.failure(value)
        } catch {
          case _ => "Exception Thrown From DataBase No Connection Fetched"
        }
      }
    }
    // we can read this like this the future task result i.e future object
    // with this promise try to fulfill the promise with a promised value
    val promiseResult: Unit = fa.onComplete(result => tryCompletePromise(promise, result))
    // or we can do this
    /*
    Tries to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.


     */
    fb.onComplete(result => tryCompletePromise(promise, result))
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
    //def tryComplete(result: Try[T]): Boolean
    //Returns:
    //If the promise has already been completed returns false, or true otherwise.
    fa.onComplete(promise.tryComplete)
    // or we can do this
    /*
    promise.tryComplete(_) this function will
    Try to complete the promise with either a value or the exception.
    Note: Using this method may result in non-deterministic concurrent programs.
*  @return If the promise has already been completed returns `false`, or `true` otherwise.

     */
     fb.onComplete(promise.tryComplete)
    // returning the fulfilled promise future
    promise.future
  }

  // This will return the last promised future out of the two
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // initially both the threads try to fulfill the bothPromise. However,
    //  whoever finishes first will fulfill first hence second
    // one cant finish the same promise
    // Same  cant be fulfilled twice as we discussed earlier
    // so for second thread or the remaining thread can fulfill the second promise


    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    // i am saying that if for this Future Task this promise is already fulfilled
    // by another Future Task
    // then this Future task should complete another promise

    def checkAndComplete(result: Try[A]): Unit = {
      if (!bothPromise.tryComplete(result)) {
        lastPromise.complete(result)
      }
    }

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)
    lastPromise.future
  }

  def retryUntil[A](action: () => Future[A], predicate: A => Boolean): Future[A] = {
    action().
      recoverWith {
        case _ => retryUntil(action, predicate)
      }
  }

  def retryDataBaseConnection[A](action: () => Future[A]): Future[A] = {
    action.apply().
      recoverWith {
        case _ => retryDataBaseConnection(action)
      }
  }


  val fastFuture: Future[Int] = Future.apply {
    println("-----------First Thread get Schduled------")
    Thread.sleep(100)
    42
  }
  val slowFuture: Future[Int] = Future.apply {
    println("-----------Second Thread get Schduled------")

    Thread.sleep(200)
    62
  }
  val result: Future[Int] = mostOptimizedFirstImpl(fastFuture, slowFuture)
  val result1: Future[Int] = last(fastFuture, slowFuture)
  /*
  forEach:
  Asynchronously processes the value in the future once the value becomes available.
  Will not be called if the future fails.
   */
  result.foreach(println)
  result1.foreach(println)

  val random = new Random()
  val action: () => Future[Int] = () => Future.apply {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("Generated  Next Value : By Future task->" + nextValue)
    nextValue
  }
  val predicate: Int => Boolean = (x: Int) => x < 10
  val retryResult: Future[Int] = retryUntil(action, predicate)
  retryResult.foreach(result => println("After Lots of Retry We got the Result As Promised" + "\t" + result))
  // Thread.sleep(1000)


  Thread.sleep(1000)


}
