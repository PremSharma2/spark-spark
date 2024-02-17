package capgemini

import capgemini.Menu.getItem

trait CafeServices {
  def standardBill(items: Seq[String]): Either[String, Double]
  def serviceCharge(items: Seq[String]): Either[String, Double]
}

class DefaultService extends CafeServices {
  private def roundToDecimals(value: Double, decimals: Int = 2): Double =
    BigDecimal(value).setScale(decimals, BigDecimal.RoundingMode.HALF_UP).toDouble

  override def standardBill(items: Seq[String]): Either[String, Double] = {
    val itemPrices = items.map(getItem)
    if (itemPrices.exists(_.isLeft)) Left("One or more MenuItems are not supported")
    else Right(itemPrices.collect { case Right(item) => item.price }.sum)
  }

  override def serviceCharge(items: Seq[String]): Either[String, Double] = {
    standardBill(items).right.flatMap { bill =>
      val modelItems = items.flatMap(getItem(_).toOption)
      val charge =
        if (modelItems.forall(_.itemType == Drink)) 0
       else if (modelItems.exists(_.temperature == Hot)) Math.min(bill * 0.2, 20)
       else Math.min(bill * 0.1, 20)

      Right(roundToDecimals(charge))
    }
  }
}

// Auxiliary object to demonstrate usage
object CafeApp extends App {
  val service = new DefaultService()
  val items = Seq("Coffee", "Cheese Sandwich")

  service.standardBill(items) match {
    case Right(bill) => println(s"Standard bill: $bill")
    case Left(error) => println(s"Error: $error")
  }

  service.serviceCharge(items) match {
    case Right(charge) => println(s"Service charge: $charge")
    case Left(error) => println(s"Error: $error")
  }
}
