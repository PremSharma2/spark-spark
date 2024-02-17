package oops

object CompanionObjectExercise {
/**
 TODO thumb rule
    nonstatic member goes to  class
    and static member companion object
 */
  class Pizza (var crustSize: Int, var crustType: String) {

    // one-arg auxiliary constructor
    def this(crustSize: Int) {
      this(crustSize, Pizza.DEFAULT_CRUST_TYPE)
    }

    // one-arg auxiliary constructor
    def this(crustType: String) {
      this(Pizza.DEFAULT_CRUST_SIZE, crustType)
    }

    // zero-arg auxiliary constructor
    def this() {
      this(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
    }

    override def toString = s"A $crustSize inch pizza with a $crustType crust"

  }

  object Pizza {
    //static constants or class level constants are defined here
    val DEFAULT_CRUST_SIZE = 12
    val DEFAULT_CRUST_TYPE = "THIN"
  }

  class Task(val description: String) {
    private var _status: String = "pending"

    def status(): String = _status
  }

  object Task {
    def apply(description: String): Task = new Task(description)

    def apply(description: String, status: String): Task = {
      val task = new Task(description)
      //TODO we can access the private member of class in the Companion Object
      task._status = status
      task
    }

    def unapply(task: Task): Option[(String, String)] = Option(task.description, task.status())
  }



  /*
  TODO
      Real-World Scenario: Database Access Layer
      Let's consider a real-world scenario in a web application
      where you have a User class representing
      users of the application and you need to perform
      database operations like fetching and saving users.
     TODO
      Factory Method:
      The companion object contains an apply method,
       which acts as a factory method to create new User instances.
       This provides a cleaner syntax for creating objects (you don't need to use the new keyword).
TODO
    Database Operations:
    Methods like findById are part of the companion object.
    These are static-like methods that can be called on the class itself.
    It's logical to put database operations here,
    as they typically operate on the class level (like fetching a user by ID) rather than on an instance level.

TODO
   Encapsulation and Accessibility:
   If there are any private fields or methods in the User class
   that need to be accessed by the companion object (for example, private helper methods for database operations),
   they can be accessed directly,
   maintaining encapsulation while allowing
   for convenient access between the class and its companion object.
   */
  class User(val id: Int, val name: String, val email: String) {
    // Instance methods here
    def save(): Unit = {
      // Save instance to the database
    }
  }


  object User {
    // Factory method
    def apply(id: Int, name: String, email: String): User = {
      new User(id, name, email)
    }

    def unapply(user: User): Option[(Int, String, String)] = {
      Some((user.id, user.name, user.email))
    }

    // Method to fetch user from database
    def findById(id: Int): Option[User] = {
      // Fetch user from the database using the id
      // Return None if not found, or Some(user) if found
      Some(User(id,name="Prem",email = "abc.com"))
    }

  }


  // Create a new user instance using the companion object
  val user = User(1, "John Doe", "john@example.com")

  // Save the user to the database
  user.save()

  // Fetch a user by id
  val maybeUser: Option[User] = User.findById(1)

  maybeUser match {
    case Some(user) => println(s"User found: ${user.name}")
    case None => println("User not found")
  }

}
