package oops

//TODO Mixins cake pattern

/**
 * This is a typical Cake Pattern setup,
 * where traits provide modular components,
 * and a concrete implementation (DonutShoppingMixinCart)
 * mixes in the required traits.
 *
 */
// Abstract Database Trait (No Implementation Here)
trait DonutDatabase {
  def addOrUpdate(donut: Donut): Long
  def query(donut: Donut): Option[Donut]
  def delete(donut: Donut): Boolean
}

// Database Component (Dependency Injection Entry Point)
trait DonutDatabaseComponent {
  val donutDatabase: DonutDatabase
}

// DAO Trait (Injected into Case Class)
trait DonutDao {
  self: DonutDatabaseComponent => // Inject Database Dependency

  def save(donut: Donut): Long = {
    println(s"Saving donut: ${donut.name}")
    donutDatabase.addOrUpdate(donut)
  }

  def find(donut: Donut): Option[Donut] = {
    println(s"Searching for donut: ${donut.name}")
    donutDatabase.query(donut)
  }

  def remove(donut: Donut): Boolean = {
    println(s"Removing donut: ${donut.name}")
    donutDatabase.delete(donut)
  }
}

case class Donut(name:String,price: Double)


//Event Case Class That Accepts a Database Dependency
case class UpdateEvent(name: String, price: Double)(val dbComponent: DonutDatabaseComponent)
  extends DonutDao with DonutDatabaseComponent {

  // Injecting the database dependency from the constructor
  override val donutDatabase: DonutDatabase = dbComponent.donutDatabase

  // Converts UpdateEvent to Donut and provides DAO functions
  def toDonut: Donut = Donut(name,price)

  def save(): Long = save(toDonut)
  def find(): Option[Donut] = find(toDonut)
  def remove(): Boolean = remove(toDonut)
}

// Concrete Database Implementation (Injected Externally)
class InMemoryDonutDatabase extends DonutDatabase {
  private var storage: Map[Long, Donut] = Map()
  private var idCounter: Long = 0

  override def addOrUpdate(donut: Donut): Long = {
    idCounter += 1
    storage += (idCounter -> donut)
    idCounter
  }

  override def query(donut: Donut): Option[Donut] = {
    storage.values.find(_.name == donut.name)
  }

  override def delete(donut: Donut): Boolean = {
    val existing = storage.find(_._2.name == donut.name)
    existing match {
      case Some((id, _)) =>
        storage -= id
        true
      case None => false
    }
  }
}

// wiring is done here Database Component Implementation That Uses `InMemoryDonutDatabase`
object InMemoryDonutDatabaseComponent extends DonutDatabaseComponent {
  override val donutDatabase: DonutDatabase = new InMemoryDonutDatabase
}


object MainApp {
  def main(args: Array[String]): Unit = {
    // Inject database dependency
    val updateEvent = UpdateEvent("Chocolate Donut", 2.99)(InMemoryDonutDatabaseComponent)

    // Now we can use the DAO methods directly
    val donutId = updateEvent.save()
    println(s"Donut saved with ID: $donutId")

    val foundDonut = updateEvent.find()
    println(s"Found Donut: $foundDonut")

    val deleteStatus = updateEvent.remove()
    println(s"Donut Deleted? $deleteStatus")
  }
}
