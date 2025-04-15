package concurrency

import java.util.concurrent.atomic.AtomicReference

import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.{ParSeq, ParVector}
import scala.concurrent.forkjoin.ForkJoinPool
/*
TODO
   Parallel Collection works on a model named
  Map Reduce
   - which breaks a collection into chunks by the class called Splitter
   - operation: then the operation you wanted to apply over thread is done by multiple future tasks
     and then finally reduce them into a single value result
     Big task → broken into → smaller tasks → each done by a thread

 */

object ParallelUtils extends App {
// parallel Collection means that many threads can work on this collection at the same time
  val parList: ParSeq[Int] =List(1,2,3).par
  val aParVector: ParVector[Int] = ParVector[Int](1,2,3,4)
  def measure[T](operation : => T):Long ={
    val time= System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }
val list= (1 to 10000).toList
  val serialTime = measure{
    list.map(_+1)
  }
  println("Serial Time :==>" + serialTime)
  val parallelTime= measure{
    list.par.map(_+1)
  }


  println("Parallel Time :==>" + parallelTime)
  // be careful using reduce and fold because the function u pass inside the fold and reduce
  // might not be associative
  println(List(1,2,3,4).reduce(_ - _))
  println(List(1,2,3,4).par.reduce(_ - _))

  //Synchronization
  var sum=0
  List(1,2,3).par.foreach(sum += _)
  println(sum)
  // here race condition will occur because in parallel Seq multiple threads Work
  // How to configure Parallel Collection
  // here we are asking the frame work that that with ForkJoinTaskSupport
  // that this is your thread manager with thread pool ForkJoinPool with two threads
  aParVector.tasksupport =new ForkJoinTaskSupport(new ForkJoinPool(2))
  //My own Task support
  /*
  aParVector.tasksupport= new TaskSupport{

    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???

    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???

    override def parallelismLevel: Int = ???

    override val environment: AnyRef = ???
  }
  */
   val atomic= new AtomicReference[Int](3)
  val atomicValue= atomic.get() // this is thread Safe
  atomic.set(4)// this is thread safe write
  atomic.getAndSet(5)// threadsafe combo
  atomic.compareAndSet(3,2)
  //atomic.updateAndGet(_ + 1)
}
