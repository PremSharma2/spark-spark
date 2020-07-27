package option

import java.util.Random
object OptionExercise extends App {

  /*
   * def apply[A, B](elems: (A, B)*): Map[A, B]

A collection of type Map that contains given key/value bindings.



Parameters

elems

the key/value pairs that make up the map
   *
   *
   */
  val config: Map[String, String] = Map.apply(

    "host" -> "176.45.36.1",
    "port" -> "2020")

  class Connection {
    def connect :String= "Connected"
  }
  object Connection {
    val random = new Random(System.nanoTime)
    def apply(host: String, port: String): Option[Connection] = {
      if (random.nextBoolean()) Some.apply(new Connection)
      else None
    }

  }
  val host = config.get("host")
  val port = config.get("port")
  /*
     *
     * if (h !=null)
     * if( p !=null)
     * return Connection.apply(h,p) i.e Option[Connection]
     * return null
     *
     * this should be replaced by functional style of programming
     *
     */

  val connection = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))
    host.flatMap{
      host => port.flatMap(port => Connection(host,port))

    }
  val test   = host.flatMap(h => port.map(p => Connection.apply(h, p))).flatten
  //or we can do this way
  val host1=host.map(host => host)
  val port1=port.map(port => port)
  val conncetion=Connection.apply(host1.get, port1.get)
  
     /**
     * if (c !=null)
     *  return c.connect
     *  return null
     */
  val connectionStatus = connection.map(c => c.connect)
  //if (connectionStatus==null) println(None) else (Some(connectionStatus.get))
  println(connectionStatus)
  // if (status !=null)
  //println(status)
  connectionStatus.foreach(println)
  config.get("host").
    flatMap(host => config.get("port").
      flatMap(port => Connection.apply(host, port)).
      map(connection => connection.connect))
    .foreach(println)

  val forConnectionStatus = for {
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  } yield connection.connect

}