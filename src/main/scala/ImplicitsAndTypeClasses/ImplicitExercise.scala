package ImplicitsAndTypeClasses

object ImplicitExercise extends App {


  /**
   * Represents a Purchase with the number of units and unit price.
   * @param nUnits Number of units in the purchase
   * @param unitPrice Price per unit
   */
  case class Purchase(nUnits: Int, unitPrice: Double) {
    /**
     * Computes the total price of the purchase.
     * @return Total price as Double
     */
    def totalPrice: Double = unitPrice * nUnits
  }

  // Companion object for Purchase
  object Purchase {

    /**
     * Defines ordering for Purchase instances based on total price.
     * This will be used implicitly when comparing instances of Purchase for "total price".
     */
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.totalPrice < _.totalPrice)
  }

  // Object for defining Unit-based Ordering for Purchase
  object UnitCountOrdering {

    /**
     * Defines ordering for Purchase instances based on number of units.
     * This will be used when explicitly specified for comparing instances of Purchase for "unit count".
     */
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  // Object for defining Unit Price-based Ordering for Purchase
  object UnitPriceOrdering {

    /**
     * Defines ordering for Purchase instances based on unit price.
     * This will be used when explicitly specified for comparing instances of Purchase for "unit price".
     */
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }



}
