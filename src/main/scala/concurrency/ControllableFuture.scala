package concurrency
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
object ControllableFuture  extends App {
  /*
  Idea behind Controllable Future

  Promise allow completing Futures based on external events/triggers.

Imagine you are using a service to which you post orders,
those orders need a long time to execute so you will only get feedback
(the result of the execution) much later.

How would you handle that with Future only?
You would need to constantly poll for the result (from the Future.apply body).

Using a Promise, you could wait for the service to trigger a callback once done.
 When that trigger happens, you can complete the Future using the Promise.
 You complete the Future from outside of the Future.apply body,
 that's what I meant above by external events/trigger.

In other words, a Promise is a safe way to complete a Future from outside,
and there's no other way to do it: the Promise API is essential.
   */

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
