package typemembers

object AOP {

/**
TODO
    Aspects are modular units that encapsulate cross-cutting concerns
    in software engineering, such as logging, security, transactions, etc.
    These concerns are pervasive across multiple
    areas of an application and don't fit well into the main business logic.
 */

/**
TODO
    1. Logging Aspect
   Let's say you want to log every time
   a database query is executed.
   You can create a Logging trait that encapsulates this concern.
 */
  trait Logger {
    def log(message: String): Unit = println(s"LOG: $message")
  }

  /**
TODO
    Here, the Database trait uses a self-type to declare
    that it needs logging functionality.
    Any class that mixes in Database
    must also mix in Logger or extend a class that does
   */
  trait Database {
    self: Logger =>
    def query(s: String): String = {
      log(s"Executing query: $s")
      "some data"
    }
  }

  class App extends Logger {
    def start(): Unit = {
      val db = new Database with Logger
      println(db.query("SELECT * FROM users"))
    }
  }


  //todo: -----------------------------------------------------------------

/**
TODO
    2. Authentication Aspect
  Imagine you have different services in your application
  that require user authentication.
 You can define an Authenticator trait for this purpose.
 authentication here is cross cutting concern
 */
trait Authenticator {
  def isAuthenticated(user: String): Boolean
}

  /**
TODO
      UserService trait declares a self-type,
     indicating it needs to be mixed with Authenticator
     It is an pointcut
   */
  trait UserService {
    self: Authenticator =>

    def getUserInfo(user: String): String = {
      if (isAuthenticated(user)) "User info"
      else "Not authorized"
    }
  }


  /**
TODO
   3. Caching Aspect
  You might want to cache the results of some operations for faster retrieval.
   */

  trait Cache {
    var cache: Map[String, String] = Map()

    def getCached(key: String): Option[String] = cache.get(key)
    def setCache(key: String, value: String): Unit = cache += (key -> value)
  }

  trait ProductCatalog {
    self: Cache =>

    def getProduct(id: String): String = {
      getCached(id) match {
        case Some(product) => product
        case None =>
          val newProduct = s"Product details for $id" // Imagine this is fetched from a database
          setCache(id, newProduct)
          newProduct
      }
    }
  }

}
