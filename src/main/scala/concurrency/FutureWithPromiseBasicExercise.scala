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
   */
  future.onComplete{
    case  Success(value) => println(s"This is consumer Thread  with the value $value")

  }

  // thread producer
  val aThread= new Thread(new Runnable {
    override def run(): Unit = {
      println("producer Thread")
      //this will force future object or task to complete with the value 42
      promise.success(42)
      println("done")
    }
  })
  aThread.start()
  Thread.sleep(1000)

}
