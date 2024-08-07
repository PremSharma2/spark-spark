package codingChallenge

import scala.math.min
/*
Enumerations are useful tool for creating groups of constants,
 such as Numeric Constants or Customized Object Constants ,
such as  Days of the week, Months of the year,
 Items in Shopping cart
all these are represented by  case Objects as states are represented by case classes/objects in Scala
But We are intrested  in constants we will use case objects
and many other situations where you have a group of related, constant values.
Sealed trait Approach:
You can also use the following approach of using a Scala trait to create the equivalent of a Java enum.
 Because it uses a “case object” approach it
 And offcourse case object will also help in pattern matching
 */
//When we want to compare o an  constant against the all  possible constants in the enum type.
//then we can implement it using sealed trait
//And if you want to perform some action on these constants
// you can define that action in that trait
// It is enum comparing all possible values as
// key value pair and getting the value of corresponding enum key
sealed trait Item {
  def price: Double = this match {
    case Apple => 1
    case Milk => 1.3
    case Bread => 0.8
    case Soup => 0.65
  }

}


//Constants Are declared here as case Objects
case object Apple extends Item

case object Milk extends Item

case object Bread extends Item

case object Soup extends Item

//Now we want create that Domain related constants objects
// so need companion object of this sealed trait which will act as factory
object Item {
  def apply(str: String): Item = str match {
    case "Apples" => Apple
    case "Milk"   => Milk
    case "Bread"  => Bread
    case "Soup"   => Soup
  }
}



/*
Here s we can see the state is represented by the Percentage case class and
 the HalfPrice case class
But if we want to perform actions on pattern match of these pojos so we will always use the sealed trait in the same file scope
Like this we have done this also here we wanted a Seq of offers so we extended the Offer trait to thse case clases bcz
Seq[Offer] we want to be put inside the Seq because case class represents only state
 */
sealed trait Offer {
  def discountedAmount: Double = this match {
    case Percentage(discountFactor, item)   => discountFactor * item.price
    case HalfPrice(halvedItem, _) => halvedItem.price * 0.5
  }
/*
final case class Percentage(mult: Double, item: Item) extends Offer
final case class HalfPrice(halvedPriceItem: Item, reason: Item) extends Offer
These are constant declaration
 */

/*
Again here we used enums to convert object constant into string representation
 */
  override def toString: String = this match {
    case Percentage(discountFactor, item) =>
      s"$item ${discountFactor * 100}% off: -${discountedAmount}"
    case HalfPrice(halvedItem, reason) =>
      s"two $reason gives $halvedItem 50% off: -${discountedAmount}"
  }
}
/*
 case class StartSpeakingMessage(textToSpeak: String)
 When you want to use attributes to perform some operation and you want state based pattern matching then always use case class
Here we are also using the pattern matching
Case classes are good for modeling immutable data
  */

final case class Percentage(discountFactor: Double, item: Item) extends Offer
final case class HalfPrice(halvedPriceItem: Item, reason: Item) extends Offer

final case class Bill(items: Seq[Item], offers: Seq[Offer]) {
  def subTotal: Double = items.map(_.price).sum

  def totalDiscounts: Double = offers.map(_.discountedAmount).sum

  def total = subTotal - totalDiscounts

  def display(): Unit = {
    println(s"Subtotal: $subTotal")
    offers.map(_.toString).foreach(println(_))
    if (offers.isEmpty) {
      println("(No offers available)")
    }
    println(s"Total: $total")
  }
}

object PriceBasket extends App {
  //val items = args.map(arg => Item.fromString(arg))
  //val items=Seq(Apples, Apples, Milk, Bread, Soup, Soup, Soup, Soup, Bread, Bread)
  val items= Seq(Apple, Milk, Bread,Soup,Soup)
// here we are associating the items with offers also we are preparing  the flat offer of 10%
  val appleOffers: Seq[Percentage] = items.flatMap({
        // here inside flatmap we are using pattern match
    case Apple => Seq(Percentage(0.1, Apple))
    case _      => Seq.empty
  })

  val breads: Int = items.count { case Bread => true; case _ => false }
  val soups: Int = items.count { case Soup => true; case _ => false }

  val soupsBreadOffers: Seq[HalfPrice] =
    (1 to min(breads, soups / 2)).toSeq.map(_ => HalfPrice(Bread, Soup))

  val allPossibleOffer: Seq[Offer] = soupsBreadOffers ++ appleOffers

  val bill = Bill(items, allPossibleOffer)
  bill.display()
}
