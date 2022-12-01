package exceptionHandling

import java.util.Random

import caseClass.Animal
import caseClass.Factory.Animal.Dog
import exceptionHandling.Try_And_Option_Exercise.Connection.getConnection

import scala.util.{Success, Try}
object Try_And_Option_Exercise extends App {

  val aSuccess = Success(3)
  val map: Try[Int] =aSuccess.map(x => x * 2)
  println(aSuccess.map(x => x * 2))
  //TODO ETW pattern for try monad
  val flatMap: Try[Int] =aSuccess.flatMap(x => Success(x * 2))
  println(aSuccess.flatMap(x => Success(x * 2)))
  println(aSuccess.filter(x => x > 20))

  /**
   * Create a Api which fetches the content from the server and also handles the Connection
   */
  val hostName = "localhost"
  val port = "8080"
  def renderHtml(page: String) = println(page)
  
  class Connection {
    def getServerContent(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html> .....  </html>"
      else throw new RuntimeException("throwing ConnectionException: Connection Intrupptred")
    }
    def getSafeServerContent(url: String): Try[String] = Try{
      getServerContent(url)
    }

  }

  trait ConnectApi {
    def getConnection(host: String, port: String): Connection = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the Port")
    }
  }
    object Connection extends  ConnectApi

    def getSafeServerConnectionApi(host: String, port: String): Try[Connection] = Try.apply{
      getConnection(host, port)
    }


  val possibleConncetion: Try[Connection] =  getSafeServerConnectionApi(hostName, port)

  
  /*
TODO
   * def flatMap[U](f: T => Try[U]): Try[U] =
    try {
    f.apply(this.get.value)
    }
    catch {
      case NonFatal(e) => Failure(e)
    }
   * 
   */
  //here flatmap function:
  //TODO :->  Maps the given function to the value from this Success or returns this if this is a Failure.
  val anotherhtml: Try[String] = possibleConncetion.map(connection => connection.getSafeServerContent("/home")).flatten
  val posiibleHtml: Try[String] = possibleConncetion.flatMap(conection => conection.getSafeServerContent("/home"))
  posiibleHtml.foreach(renderHtml)

     val connectionString: Try[String] = for{
       possibleConnection<- getSafeServerConnectionApi(hostName, port)
          content        <- possibleConnection.getSafeServerContent("/home")

     } yield content

  val bag = List("1", "2", "three", "4", "one hundred seventy five")
  def toInt(in: String): Option[Int] = {
    try {
      Some(Integer.parseInt(in.trim))
    } catch {
      case e: Exception => None
    }
  }
  val summap=bag.map(toInt).flatten
  val sum=bag.flatMap(toInt)
  println(sum)
  def api(function1: Function1[Dog,Animal])= ???

  /*
  api(new Function1[Animal,Dog] {
    override def apply(v1: Animal): Dog = new Dog
  })

   */
}