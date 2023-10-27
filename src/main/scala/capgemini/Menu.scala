package capgemini

private trait Food {
  def isFood: Boolean = true

  def isDrink: Boolean = false
}

private trait Drink {
  def isDrink: Boolean = true

  def isFood: Boolean = false
}

private trait Hot {
  def isHot: Boolean = true
}

private trait Cold {
  def isHot: Boolean = false
}

case class ItemNotSupportedException(message: String) extends Exception(message)

// Menu item is an abstract type it will have multiple implementations
private sealed trait MenuItem {
  def price: Double

  def isDrink: Boolean

  def isFood: Boolean

  def isHot: Boolean
}

private object MenuItem {
  private case class Cola(price: Double = 0.5) extends MenuItem with Drink with Cold

  private case class Coffee(price: Double = 1) extends MenuItem with Drink with Hot

  private case class CheeseSandwich(price: Double = 2) extends MenuItem with Food with Cold

  private case class SteakSandwich(price: Double = 4.5) extends MenuItem with Food with Hot

  def apply(itemName: String): MenuItem = {
    itemName match {
      case "Cola" => Cola()
      case "Coffee" => Coffee()
      case "Cheese Sandwich" => CheeseSandwich()
      case "Steak Sandwich" => SteakSandwich()
      case _ => throw ItemNotSupportedException(s"Item not supported: $itemName")
    }
  }
}


