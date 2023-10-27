package concurrency

import scala.util.control.NonFatal

object PromiseInDepth  extends App{

  import scala.concurrent._
  import ExecutionContext.Implicits.global
  //implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))
def fetchValue= 2
  val p = Promise[String]
  val q = Promise[String]


  p.future foreach {
    case text => println(s"Promise p succeeded with '$text'")
  }


/*
TODO
   Completes the promise with a value.
   Params:
   value – The value to complete the promise with.
   If the promise has already been fulfilled,
   failed or has timed out, calling this method will throw an IllegalStateException.
 */

  p success "kept"

  /*
  TODO
      Tries to complete the promise with a value.
      Note: Using this method may result in non-deterministic concurrent programs.
     Returns:
     If the promise has already been completed returns false, or true otherwise.
   */
  val secondAttempt = p trySuccess "kept again"

  println(s"Second attempt to complete the same promise went well? $secondAttempt")
/*
TODO
     Completes the promise with an exception.
     internally it will give Future[Throwable] that is future with error
     because promise was to complete with error not success
   Params:
   cause – The throwable to complete the promise with.
   If the throwable used to fail this promise is an error,
   a control exception or an interrupted exception,
   it will be wrapped as a cause within an ExecutionException which will fail the promise.
   If the promise has already been fulfilled,
   failed or has timed out, calling this method will throw an IllegalStateException.
 */
  q failure new Exception("not kept")

  //TODO here we are fetching the exception from the promise i.e at the consumer side
  q.future.failed foreach {
    case t => println(s"Promise q failed with $t")
  }

  def runByPromise[T](block: => T)(implicit ec: ExecutionContext): Future[T] = {
    val p = Promise[T]
    ec.execute { () =>
      try {
        p.success(block)
      } catch {
        case NonFatal(e) => p.failure(e)
      }
    }
    p.future
  }
  val fetchedResult: Future[Int] =runByPromise(fetchValue)
}


object PromisesCustomAsync extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.util.control.NonFatal

  def myFuture[T](body: =>T): Future[T] = {
    val p = Promise[T]

    global.execute(new Runnable {
      def run() = try {
        val result = body
        p success result
      } catch {
        case NonFatal(e) =>
          p failure e
      }
    })

    p.future
  }

  val future = myFuture {
    "naaa" + "na" * 8 + " Katamari Damacy!"
  }

  future foreach {
    case text => println(text)
  }

}


object PromisesAndCallbacks extends App {
  import java.io.File

  import org.apache.commons.io.monitor._

  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def fileCreated(directory: String): Future[String] = {
    val p = Promise[String]

    val fileMonitor = new FileAlterationMonitor(1000)
    val observer = new FileAlterationObserver(directory)
    val listener = new FileAlterationListenerAdaptor {
      override def onFileCreate(file: File) {
        try p.trySuccess(file.getName)
        finally fileMonitor.stop()
      }
    }
    observer.addListener(listener)
    fileMonitor.addObserver(observer)
    fileMonitor.start()

    p.future
  }

  fileCreated(".") foreach {
    case filename => println(s"Detected new file '$filename'")
  }

}


object PromisesAndCustomOperations extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  implicit class FutureOps[T](val self: Future[T]) {
    def or(that: Future[T]): Future[T] = {
      val p = Promise[T]
      self onComplete { case x => p tryComplete x }
      that onComplete { case y => p tryComplete y }
      p.future
    }
  }

  val f = Future { "now" } or Future { "later" }

  f foreach {
    case when => println(s"The future is $when")
  }

}


object PromisesAndTimers extends App {
  import java.util._

  import PromisesAndCustomOperations._

  import scala.concurrent._
  import ExecutionContext.Implicits.global

  private val timer = new Timer(true)

  def timeout(millis: Long): Future[Unit] = {
    val p = Promise[Unit]
    timer.schedule(new TimerTask {
      def run() = p.success(())
    }, millis)
    p.future
  }

  val f = timeout(1000).map(_ => "timeout!") or Future {
    Thread.sleep(999)
    "work completed!"
  }

  f foreach {
    case text => println(text)
  }

}


object PromisesCancellation extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def cancellable[T](b: Future[Unit] => T): (Promise[Unit], Future[T]) = {
    val p = Promise[Unit]
    val f = Future {
      val r = b(p.future)
      if (!p.tryFailure(new Exception))
        throw new CancellationException
      r
    }
    (p, f)
  }

  val (cancel, value) = cancellable { cancel =>
    var i = 0
    while (i < 5) {
      if (cancel.isCompleted) throw new CancellationException
      Thread.sleep(500)
      println(s"$i: working")
      i += 1
    }
    "resulting value"
  }

  Thread.sleep(1500)

  cancel.trySuccess(())

  println("computation cancelled!")


}
