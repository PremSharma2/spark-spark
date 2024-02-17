package capgemini

//Adts for temptarute
sealed trait Temperature
case object Hot extends Temperature
case object Cold extends Temperature

//Adts for Itemtype
sealed trait ItemType
case object Food extends ItemType
case object Drink extends ItemType

case class MenuItem(name: String, price: Double, itemType: ItemType, temperature: Temperature)

object Menu {
  val items: List[MenuItem] = List(
    MenuItem("Cola", 0.5, Drink, Cold),
    MenuItem("Coffee", 1.0, Drink, Hot),
    MenuItem("Cheese Sandwich", 2.0, Food, Cold),
    MenuItem("Steak Sandwich", 4.5, Food, Hot)
  )

  def getItem(name: String): Either[String, MenuItem] =
    items.find(_.name == name).toRight(s"Item not supported: $name")
}

case class ItemNotSupportedException(message: String) extends Exception(message)

// Example usage
object Example {
  def main(args: Array[String]): Unit = {
    val item = Menu.getItem("Coffee")
    item match {
      case Right(menuItem) => println(s"Selected item: ${menuItem.name}, Price: ${menuItem.price}")
      case Left(error) => println(error)
    }
  }
}



