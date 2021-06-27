package concurrency
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Success

object FutureWithPromiseBasicExercise  extends App {

  val promise= Promise[Int]() // promise is controller over future
  // i.e promise has a member future i.e promise. so promise is managing the future
  // future which hold the future object so future is managed by promise
  val future: Future[Int] = promise.future

  /*
  thread1 - consumer thread

  Oncomplete is an callback method :
  When this future is completed, either through an exception, or a value,
  apply the provided partial function or normal function.
If the future has already been completed,
this will either be applied immediately or be scheduled asynchronously.
Multiple callbacks may be registered;
there is no guarantee that they will be executed in a particular order.
 The provided callback always runs in the provided implicit ExecutionContext,
  though there is no guarantee that the execute() method on the ExecutionContext
   will be called once per callback or that execute() will be called in the current thread.
    That is, the implementation may run multiple callbacks in a batch
    within a single execute() and it may run execute() either immediately or asynchronously.
   */
  future.onComplete{
    case  Success(value) => println(s"This is consumer Thread  with the value $value")

  }

  // thread producer
  val aThread= new Thread(new Runnable {
    override def run(): Unit = {
      println("producer Thread")
      //this will force future object or task to complete with the value 42
      // this statement promises that this Runnable Task will return 42
      //Completes the promise with either an exception or a value.
      promise.success(42)
      println("done")
    }
  })
  aThread.start()
  Thread.sleep(1000)

}
