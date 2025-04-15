package adts

object AdtsUSeCase {


  object HMRC2 extends App {

    // Algebraic Data Type (ADT) for Items
    sealed trait Item {
      def price: Double
    }

    case object Apple extends Item {
      val price: Double = 1.0
    }

    case object Milk extends Item {
      val price: Double = 1.0
    }

    case object Bread extends Item {
      val price: Double = 1.0
    }

    case object Soup extends Item {
      val price: Double = 1.0
    }

    object Item {
      def apply(name: String): Option[Item] = name.toLowerCase match {
        case "apples" => Some(Apple)
        case "milk"   => Some(Milk)
        case "bread"  => Some(Bread)
        case "soup"   => Some(Soup)
        case _        => None
      }
    }

    /**
     * We should use abstract members inside sealed trait when:
     *
     * ✅ 1. We want to enforce method implementation across all case classes
     * If a function must be implemented for every subtype, define it in the trait
     * and we will override val i.e def can overidden as val i.e as property of that type
     */
    // ADT for Offers
    sealed trait Offer {
      def discount: Double
      def description: String
    }

    case class PercentageDiscount(discountFactor: Double, item: Item, quantity: Int) extends Offer {
      val discount: Double = discountFactor * item.price * quantity
      val description: String = s"$quantity x $item at ${discountFactor * 100}% off: -$$$discount"
    }

    case class ThreeForTwo(item: Item) extends Offer {
      val discount: Double = (item.price / 3) * 3
      val description: String = s"Three-for-two offer on $item: -$$$discount"
    }

    // Bill Case Class
    case class Bill(items: Seq[Item], offers: Seq[Offer]) {
      val subTotal: Double = items.map(_.price).sum
      val totalDiscount: Double = offers.map(_.discount).sum
      val total: Double = subTotal - totalDiscount

      def display(): Unit = {
        println(f"Subtotal: $$${subTotal}%.2f")
        if (offers.nonEmpty) {
          offers.foreach(o => println(o.description))
        } else {
          println("(No offers available)")
        }
        println(f"Total: $$${total}%.2f")
      }
    }

    // Function to calculate offers based on the items in the cart
    def calculateOffers(items: Seq[Item]): Seq[Offer] = {
      val itemCounts = items.groupBy(identity).mapValues(_.size)

      val appleOffer = itemCounts.getOrElse(Apple, 0) match {
        case count if count >= 2 => Some(PercentageDiscount(0.5, Apple, count))
        case _                   => None
      }

      val breadOffer = itemCounts.getOrElse(Bread, 0) match {
        case count if count >= 3 => Some(ThreeForTwo(Bread))
        case _                   => None
      }

      Seq(appleOffer, breadOffer).flatten
    }

    // Example cart
    val items: Seq[Item] = Seq(Apple, Apple, Bread, Bread, Bread, Milk, Soup)

    // Compute applicable offers
    val offers: Seq[Offer] = calculateOffers(items)

    // Generate and display the bill
    val bill = Bill(items, offers)
    bill.display()
  }


}
