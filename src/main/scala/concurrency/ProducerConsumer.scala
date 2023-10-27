package concurrency

import java.util.concurrent.Executors
import scala.concurrent.Future

object ProducerConsumer {


  trait Consumer[T] {
    def consume(item: Future[T]): Unit
  }

  import scala.concurrent.{ExecutionContext, Future, Promise}

  class Producer[T](consumer: Consumer[T])(implicit ec: ExecutionContext) {
    def produce(item: T): Unit = {
      val promise = Promise[T]()
      promise.success(item) // Complete the promise with the item
      consumer.consume(promise.future) // Pass the Future to the consumer
    }
  }


  import scala.util.{Failure, Success}

  class PrintConsumer[T](implicit ec: ExecutionContext) extends Consumer[T] {
    override def consume(item: Future[T]): Unit = {
      item.map { value =>
        println(s"Consumed: $value")
      }
    }
  }


  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))

    val consumer = new PrintConsumer[Int]
    val producer = new Producer[Int](consumer)

    // Simulate external service running the producer
    new Thread(() => {
      Thread.sleep(2000) // simulate some work
      producer.produce(42)
    }).start()

    // Just to keep the JVM alive
    Thread.sleep(5000)
  }


}
