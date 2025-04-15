package concurrency

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

object ProducerConsumer {

  // Generic consumer interface
  trait Consumer[T] {
    def consume(item: Future[T]): Unit
  }

  // A producer that creates a Promise and gives its Future to the consumer
  class ControllableProducer[T](consumer: Consumer[T])(implicit ec: ExecutionContext) {

    def produceLater(value:T): Promise[T] = {
      val promise = Promise[T]()
      consumer.consume(promise.future) // give the Future to the consumer
      promise // returned to be completed later
    }
  }

  // A simple consumer that prints the value when it becomes available
  class PrintConsumer[T](implicit ec: ExecutionContext) extends Consumer[T] {
    override def consume(future: Future[T]): Unit = {
      future.onComplete {
        case Success(value) => println(s"[Consumer] Received: $value")
        case Failure(error) => println(s"[Consumer] Failed: ${error.getMessage}")
      }
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

    val consumer = new PrintConsumer[Int]
    val producer = new ControllableProducer[Int](consumer)

    println("[Main] Submitting production task...")

    // Simulate async service which will fulfill the promise


    new Thread(() => {
      Thread.sleep(2000) // simulate external processing
      println("[Service] Fulfilling the promise with value 42")
       producer.produceLater(42)
    }).start()

    // Keep JVM alive until task finishes
    Thread.sleep(3000)

  }
}
