package exception

import scala.util.Success
import scala.util.Failure
import scala.util.Try
import java.util.Random
object Try_And_Option_Exercise extends App {

  val aSuccess = Success(3)
  val map: Try[Int] =aSuccess.map(x => x * 2)
  println(aSuccess.map(x => x * 2))
  val faltmap: Try[Int] =aSuccess.flatMap(x => Success(x * 2))
  println(aSuccess.flatMap(x => Success(x * 2)))
  println(aSuccess.filter(x => x > 20))
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
  object HttpConnectionService {
    val random = new Random(System.nanoTime())
    def getConnection(host: String, port: String): Connection = {
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the Port")
    }
    def getSafeServerConnection(host: String, port: String): Try[Connection] = Try.apply{
      getConnection(host, port)
    }
  }

  val possibleConncetion: Try[Connection] = HttpConnectionService.getSafeServerConnection(hostName, port)
  
  /*
   * def flatMap[U](f: T => Try[U]): Try[U] =
    try {
    f.apply(this.get.value)
    }
    catch {
      case NonFatal(e) => Failure(e)
    }
   * 
   */
  val anotherhtml = possibleConncetion.map(connection => connection.getSafeServerContent("/home")).flatten
  val posiibleHtml: Try[String] = possibleConncetion.flatMap(conection => conection.getSafeServerContent("/home"))
  posiibleHtml.foreach(renderHtml)

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

}