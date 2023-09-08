package typemembers

object UseCaseOfSelfType {
/**
TODO
  1. Dependency Injection
  In software engineering, dependency injection is a technique
  whereby one object supplies the dependencies of another object.
  The self-type can serve as a form of "compile-time" dependency injection.
  In this example, the UserService trait declares
  that it needs to be mixed in with
  something that satisfies the Database trait
  (via the self-type self: Database =>).
  This ensures at compile-time that the UserService
  will only be used in a context where it has access to a database.
 */


trait Database {
  def query(s: String): String
}

  trait UserService {
    self: Database =>

    def getUser(id: Int): String = self.query(s"SELECT * FROM users WHERE id=$id")
  }

  class MySQLDatabase extends Database {
    //custom Query
    def query(s: String): String = s"Querying MySQL: $s"
  }

  class UserSystem extends MySQLDatabase with UserService

  val userSystem = new UserSystem
  println(userSystem.getUser(1))



  //todo : ----------------------------------------------------------------------------------------------------------------------


  /**
   TODO
     2. Rich Interface / Role-based Programming
     In object-oriented design,
     it's often desirable to extend classes
     with additional methods without modifying them.
     Traits can offer these "roles" or "aspects,"
     and the self-type can ensure that
     these roles are only applied where they make sense.
     In this example, the Renderable trait is meant to add
     rendering capability to any Movable object.
     The self-type ensures that rendering is only available to Movable objects.
   */


  trait Movable {
    def position: (Int, Int)
  }

  trait Renderable {
    self: Movable =>

    def render(): Unit = {
      val (x, y) = position
      println(s"Rendering at position ($x, $y)")
    }
  }

  class Sprite(var position: (Int, Int)) extends Movable with Renderable


}
