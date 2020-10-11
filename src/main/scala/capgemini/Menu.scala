package capgemini

private trait Food

private trait Hot

private trait Cold

private trait Drink
case class ItemNotSupportedException(message: String) extends Exception(message)
// menu item is an abstract type it will have multiple impls
private sealed trait MenuItem {

  def price: Double

  def isDrink: Boolean = this.isInstanceOf[Drink]

  def isHotFood: Boolean = isFood && isHot

  def isFood: Boolean = this.isInstanceOf[Food]

  def isHot: Boolean = this.isInstanceOf[Hot]

}

private object MenuItem {
  def apply(itemName: String): MenuItem = {
    itemName match {
      case "Cola" => Cola()
      case "Coffee" => Coffee()
      case "Cheese Sandwich" => CheeseSandwich()
      case "Steak Sandwich" => SteakSandwich()
      case _ =>
        throw ItemNotSupportedException(s"item not not supported: $itemName")
    }
  }
}

private case class Cola(price: Double = 0.5)
  extends MenuItem
    with Drink
    with Cold

private case class Coffee(price: Double = 1)
  extends MenuItem
    with Drink
    with Hot

private case class CheeseSandwich(price: Double = 2)
  extends MenuItem
    with Food
    with Cold

private case class SteakSandwich(price: Double = 4.5)
  extends MenuItem
    with Food
    with Hot