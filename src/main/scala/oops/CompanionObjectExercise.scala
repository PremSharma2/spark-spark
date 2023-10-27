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
}
