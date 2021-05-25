package option

import java.util.Random
object OptionExercise extends App {

  /*
   * def apply[A, B](elems: (A, B)*): Map[A, B]

A collection of type Map that contains given key/value bindings.


  the key/value pairs that make up the map
   */
  val serverConfig: Map[String, String] =
    Map.apply(
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
  val host: Option[String] = serverConfig.get("host")
  val port = serverConfig.get("port")
  /*
     *
     * if (h !=null)
     * if( p !=null)
     * return Connection.apply(h,p) i.e Option[Connection]
     * return null
     *it is an ETW pattern over Option monad
     * i.e we need to extract it and transform it into another form
     * and put it back into Context or monad called Option
     * this should be replaced by functional style of programming
     *
     */

  val connection: Option[Connection] = host.flatMap(h => port.
    flatMap(p => Connection.apply(h, p)))

  val conn: Option[Connection] =  host.flatMap{
      host => port.flatMap(port => Connection(host,port))

    }
  //val test   = host.flatMap(h => port.map(p => Connection.apply(h, p))).flatten
  //or we can do this way
  /*
  val host1=host.map(host => host)
  val port1=port.map(port => port)
  val conncetion=Connection.apply(host1.get, port1.get)
  */
     /** Here Option[connection]
      * is monad as well as Data type With map Function
      * so idea here is not to transform just get the type
     * if (c !=null)
     *  return c.connect or
     *  return None
      *  i.e we need to map
     */
  val connectionStatus: Option[String] = connection.map(c => c.connect)
  //if (connectionStatus==null) println(None) else (Some(connectionStatus.get))
  println(connectionStatus)
  // if (status !=null)
  //println(status)
  connectionStatus.foreach(println)
  val x: Option[String] =
    serverConfig.get("host").
      flatMap(host => serverConfig.get("port").
      flatMap(port => Connection.apply(host, port)).
      map(connection => connection.connect))


// as we can see we are using the composite Function here so we can decompose
// it further like this  x => f(x).flatmap(g(x,y).map(h(z))
 val compositeFunction: String => Option[String] = host => serverConfig.get("port").
    flatMap(port => Connection.apply(host, port)).
    map(connection => connection.connect)
  val result: Option[String] =serverConfig.get("host") flatMap compositeFunction
  // but if you dont want composite function usage then for Comprehension is good
  // this is transformed into this
  /*
   val connection: Option[Connection] =
     host.flatMap(h => port.
    flatMap(p => Connection.apply(h, p)))
   */
  val forConnectionStatus: Option[String] = for {
    host: String <- serverConfig.get("host")
    port: String <- serverConfig.get("port")
    connection <- Connection(host, port)
  }yield  connection.connect

}