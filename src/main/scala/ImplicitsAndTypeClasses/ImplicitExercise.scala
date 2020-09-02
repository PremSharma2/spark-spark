package ImplicitsAndTypeClasses

object ImplicitExercise extends App {

  case class Purchase(nUnits:Int, unitPrice:Double){
    def totalPrice : Double= unitPrice * nUnits
  }
object Purchase{
  implicit def totalPriceOrdering : Ordering[Purchase] = Ordering.fromLessThan(_.totalPrice< _.totalPrice)

}
object UnitPriceOrdering{
  implicit val unitcountOrdering : Ordering[Purchase] = Ordering.fromLessThan(_.nUnits<_.nUnits)
}
  object PriceOrdering{
    implicit val unitPriceOrdering : Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice<_.unitPrice)

  }



}
