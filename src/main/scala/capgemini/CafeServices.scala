package capgemini
import capgemini.codingchallenge.NumberOperations.roundTodecimals
trait CafeServices {
  type MenuItem = String
  type Price = Double

  @throws[ItemNotSupportedException]("One or more MenuItems are not supported")
  def standardBill(items: Seq[MenuItem]): Price

  @throws[ItemNotSupportedException]("if any MenuItem is not supported")
  def serviceCharge(items: Seq[MenuItem]): Price

}


 class DefaultService extends CafeServices {

  override def serviceCharge(items: Seq[MenuItem]) = {

    val sc = fullServiceCharge(items)
    if (sc > 20) 20
    else roundTodecimals(sc)
  }

  private def fullServiceCharge(items: Seq[MenuItem]): Double = {
    val standardPrice = standardBill(items)
    val modelItems = toModel(items)
    if (modelItems.forall(_.isDrink)) 0
    else if (modelItems.exists(_.isHot)) standardPrice * 0.2
    else standardPrice * 0.1
  }
    override def standardBill(items: Seq[MenuItem]) = {
    toModel(items).view.map(_.price).sum
  }

  private def toModel(items: Seq[MenuItem]): Seq[capgemini.MenuItem] =
    items.map(name => MenuItem(name))

}