import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object MultiThreadingRecap   {

  val aThread= new Runnable {
    override def run(): Unit = println("JavaThread:->Executing")
  }
val aThread1= new Thread(() => println("ScalaThread:-> Executing"))

  aThread1.start()
  //todo: -problems with thread model is different runs produces different Result
  val aHellothread= new Thread(() => (1 to 100).foreach(_ => println("hello")))
  val aGoodByethread= new Thread(() => (1 to 100).foreach(_ => println("hello")))
aHellothread.start()
  aGoodByethread.start()

  // TODO : problem number2 with Existing java concurrency model is

  // DR #1: OO encapsulation is only valid in the SINGLE-THREADED MODEL
/*
TODO : OO encapsulation is only valid in the SINGLE-THREADED MODEL
   A core pillar of OOP is encapsulation.
  Encapsulation dictates that the internal data
  of an object is not accessible directly from the outside;
  it can only be modified by invoking a set of curated methods.
  The object is responsible for exposing safe operations
  that protect the invariant nature of its encapsulated data.
 */
  class BankAccount(private var amount: Int) {
    override def toString = s"$amount"

    def withdraw(money: Int) = synchronized {
      this.amount -= money
    }

    def deposit(money: Int) = synchronized {
      this.amount += money
    }

    def getAmount = amount
  }


  val account = new BankAccount(2000)
  val depositThreads = (1 to 1000).map(_ => new Thread(() => account.deposit(1)))
  val withdrawThreads = (1 to 1000).map(_ => new Thread(() => account.withdraw(1)))

  def demoRace(): Unit = {
    (depositThreads ++ withdrawThreads).foreach(_.start())
    Thread.sleep(1000)
    println(account.getAmount)
  }
//interthread Communication on the JVM
  //wait - notify mechanism
//Scala Concurrency Model
  // Futures
  implicit val ec: ExecutionContext =
  ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aFuture = Future(/* something to be evaluated on another thread*/ 1 + 41)

  // register callback when it finishes
  aFuture.onComplete {
    case Success(value) => println(s"the async meaning of life is $value")
    case Failure(exception) => println(s"the meaning of value failed: $exception")
  }

  val aPartialFunction: PartialFunction[Try[Int], Unit] = {
    case Success(value) => println(s"the async meaning of life is $value")
    case Failure(exception) => println(s"the meaning of value failed: $exception")
  }

  aFuture.onComplete(aPartialFunction)
  //Interms of FP Future is monadic Construct
  // map, flatMap, filter, ...
  val doubledAsyncMOL: Future[Int] = aFuture.map(_ * 2)
  val filter: Future[Int] = aFuture.filter(_%2==0)
  val nonSenseFuture = for{
     a <- aFuture
     b <- filter
    if(a+b==2)
  }yield a+b

  // DR #2 - delegating a task to a thread

  var task: Runnable = null

  val runningThread: Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for a task")
          runningThread.wait()
        }
      }

      task.synchronized {
        println("[background] I have a task!")
        task.run()
        task = null
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable) = {
    if (task == null) {
      task = r
      runningThread.synchronized {
        runningThread.notify()
      }
    }
  }

  def demoBackgroundDelegation(): Unit = {
    runningThread.start()
    Thread.sleep(1000)
    delegateToBackgroundThread(() => println("I'm running from another thread"))
    Thread.sleep(1000)
    delegateToBackgroundThread(() => println("This should run in the background again"))
  }

  // DR #3: tracing and dealing with errors is a PITN in multithreaded/distributed apps

 // implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  // sum 1M numbers in between 10 threads

  val futures: Seq[Future[BigInt]] = (0 to 9)
    .map(i => BigInt(100000 * i) until BigInt(100000 * (i + 1))) // 0 - 99999, 100000 - 199999, and so on
    .map(range => Future {
      // bug
      if (range.contains(BigInt(546732))) throw new RuntimeException("invalid number")
      range.sum
    })

  val sumFuture = Future.reduceLeft(futures)(_ + _)

  def main(args: Array[String]): Unit = {
    sumFuture.onComplete(println)
  }
}
