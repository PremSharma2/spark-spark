package concurrency
import concurrency.ControllableFuture.MyService.produceAPreciousValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.control.NonFatal
object ControllableFuture  extends App {
  /*
TODO
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
/*
TODO
    Let’s assume this important service is also impossible to change,
    for various reasons (API breaks etc). In other words,
    the “production” logic is completely fixed and deterministic.
     However, what’s not deterministic is when the service will actually
     end up calling the production function. In other words, you can’t implement your function as
 */

  def fetchFromDB(yourArg: Int): Future[String] = Future{
    //this is wrong implementation u cant spawn other service thread here
    //because spawning up the thread responsible for evaluating the production function is not up to you.
    produceAPreciousValue(2)
  }

// This service is multi threaded
  object MyService{
 // 1 A "production" function which is completely deterministic.

    def produceAPreciousValue(thearg:Int):String =
      "Fetching the Value From the Database " + {42/thearg}
//2) A submission function which has a pretty terrible API,
// because the function argument will be evaluated on
// one of the service's threads and
// you can't get the returned value back from another thread's call stack.
    def submmitTask[A](actualArg:A)(function: A => Unit): Boolean = {
      // This will apply the function on the given input i.e actualArg
      // at some point in time without our control i.e non determnistic way
      true
    }
  }

/*
TODO
     The solution
     Introducing Promises - a “controller” and “wrapper” over a Future.
     Here’s how it works. You create a Promise,
     get its Future and use it (consume it) with the assumption it will be filled in later:
 */

//making of  Controllable Future Step 1:
  val promise = Promise[String]()
  // step 2  extract its Future
 //promise.complete()
  val extractedFuture = promise.future
  // step3 consume the future using map oR flatMap


  /*
TODO
  map function:
  It also uses onComplete call back and inside the onComplete it then uses
  map function so this map function i s wrapper
  Like this :
  val p = Promise[S]()
    this.onComplete { v => p complete (v map f) }
    p.future

TODO
  Creates a new future by applying a function to the successful result of this future.
   If this future is completed with an exception then
    the new future will also contain this exception.
   */


  //TODO because map returns a promise.future
  val furtherProcessing: Future[String] = extractedFuture.map(_.toUpperCase)

//Then pass that promise to someone else, perhaps an asynchronous service:
  // step 4 Async call
  def asyncCall(promise: Promise[String]): Unit = {
    promise.success("Your value here, your majesty")
  }


// step 5 call the producer
  asyncCall(promise)

  // this is the target method
  // we will call service form here
  // this is classical case how to make deterministic or controllable future
  // because service is a async call
  //And at the moment the promise contains a value,
  // its future will automatically be fulfilled with that value, which will unlock the consumer.


  def gimmeMyPreciousValueFromDB(args:Int): Future[String] = {
    // step 1 - create the promise
    val thePromise = Promise[String]()

    // submit a task to be evaluated later, at the discretion of the service
    // note: if the service is not on the same JVM,
    // you can pass a tuple with the arg and the promise so the service has access to both
    //step 5 call the service this will be executed on the thread of Service running on other jvm
    MyService.submmitTask(args){
      x:Int =>
        // step 4- producer logic

      val preciousValue= MyService.produceAPreciousValue(x)
        // Full-fill the desired promise with this value
        //or it Completes the promise with either an exception or a value
        //it will call tryComplete
        thePromise.success(preciousValue)
    }
    // step 2 - extract the Future
    /*
 TODO
      The associated Future is then obtained using thePromise.future.
      The Future serves as a read-only view of the result of some asynchronous operation,
      which might be running on a different thread or even a different machine.
     */
    val theFuture = thePromise.future
     theFuture
  }


  //TODO or we can produce  like this way ie at producer side

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

  //TODO -------------------------// step -3 Someone will consume my Future-----------------------------------------------------------------
  /*
 TODO
      So we create a promise and then we return its future at the end,
      for whoever wants to consume it. In the middle,
      we submit a function which will be evaluated at some point,
       out of our control. At that moment,
       the service produces the value and fulfils the Promise,
       which will automatically fulfil the Future for the consumer.
   */

  // this consumer thread will automatically enalbled once the
  // thePromise.success(preciousValue) is fullfilled
  def consumeApi(completedFuture:Future[String]) = {
     completedFuture.map(_.contains('s'))
  }
  val completedTask: Future[String] = gimmeMyPreciousValueFromDB(2)
  consumeApi(completedTask)


}
