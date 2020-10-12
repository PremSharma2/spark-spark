package concurrency
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
object ControllableFuture  extends App {

  val future = Future{
    println("Thread is getting Executed ")
    42
  }


// This service is multi threaded
  object MyService{

    def produceAPreciousValue(thearg:Int):String =
      "Fetching the Value From the Database " + {42/thearg}

    def submmitTask[A](actualArg:A)(function: A => Unit): Boolean = {
      // This will apply the function on the given input i.e actualArg
      // at some point in time without our control i.e non determnistic way
      true
    }
  }
//making of  Controllable Future Step 1:
  val promise = Promise[String]()
  // step 2  extract its Future
  val extractedFuture = promise.future
  // step3 consume the future
  /*
  map function:
  It also uses onComplete call back and inside the onComplete it then uses
  map function so this map function i s wrapper
  Like this :
  val p = Promise[S]()
    onComplete { v => p complete (v map f) }
    p.future

  Creates a new future by applying a function to the successful result of this future.
   If this future is completed with an exception then
    the new future will also contain this exception.
Example:

  val f = Future { 5 }
  val g = Future { 3 }
  val h = for {
    x: Int <- f // returns Future(5)
    y: Int <- g // returns Future(3)
  } yield x + y

   */
  val furtherProcessing = extractedFuture.map(_.toUpperCase)

  // step 4 Async call
  def asyncCall(promise:Promise[String]): Unit ={
    promise.success("Your value your majesty")
  }
// step 5 call the producer
  asyncCall(promise)
  // this is the target method
  // we will call service form here
  // this is classical case how to make deterministic or controllable future
  // because service is a async call
  def gimmeMyPreciousValue(args:Int): Future[String] = {
    // step 1 - create the promise
    val thePromise = Promise[String]()

    // step 5: call the the service end point
    MyService.submmitTask(args){
      x:Int =>
        // step 4- producer logic
        val preciousValue= MyService.produceAPreciousValue(x)
        thePromise.success(preciousValue)
    }
    // step 2 - etract the Future
    val theFuture = thePromise.future
     theFuture
  }

  // step -3 Someone will consume my Future
  // this consumer thread will automatically enalbled once the
  // thePromise.success(preciousValue) is fullfilled
}
