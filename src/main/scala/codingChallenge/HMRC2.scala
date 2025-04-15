package codingChallenge

object HMRC2 extends App {
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
      case Apple => 1.0
      case Milk => 1.0
      case Bread => 1.0
      case Soup => 1.0

    }

  }


  //Constants Are declared here as case Objects
  final case object Apple extends Item
  final case object Milk extends Item
  final case object Bread extends Item
  final case object Soup extends Item

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
      case Percentage(discountFactor, item)   => discountFactor * item.price *2
      case ThreeForTwo(halvedItem) => halvedItem.price * 0.3333 *3
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
      case Percentage(discountFactor, item) => s"$item ${discountFactor * 100}% off: -${discountedAmount}"
      case ThreeForTwo(threeForTwoItem) =>
        s"three bread after paid for two offer: -> gives $threeForTwoItem 33.33% off: -${discountedAmount}"
    }
  }
  /*
   case class StartSpeakingMessage(textToSpeak: String)
   When you want to use attributes to perform some operation and you want state based pattern matching then always use case class
  Here we are also using the pattern matching
  Case classes are good for modeling immutable data
    */

  final case class Percentage(discountFactor: Double, item: Item) extends Offer
  final case class ThreeForTwo(item: Item) extends Offer

  final case class Bill(items: Seq[Item], offers: Seq[Offer]) {
    def subTotal: Double = items.map(_.price).sum

    def totalDiscounts: Double = offers.map(_.discountedAmount).sum

    def total: Double = subTotal - totalDiscounts

    def display(): Unit = {
      println(s"Subtotal: $subTotal")
      offers.map(_.toString).foreach(println(_))
      if (offers.isEmpty) {
        println("(No offers available)")
      }
      println(s"Total: $total")
    }
  }

  private def calculateOffer(items: Seq[Item]): Seq[Offer] = {
    //val items = args.map(arg => Item.fromString(arg))
    //val items=Seq(Apples, Apples, Milk, Bread, Soup, Soup, Soup, Soup, Bread, Bread)
    //val items = Seq(Apple, Apple, Bread, Bread, Bread, Milk, Soup)

    val breads: Int = items.count { case Bread => true; case _ => false }
    val apple: Int = items.count { case Apple => true; case _ => false }

    val appleOffer: Seq[Percentage] = apple match {
      case 2 => Seq(Percentage(0.5, Apple))
      case _ => Seq.empty

    }

    val breadsoffer: Seq[ThreeForTwo] = breads match {
      case 3 => Seq(ThreeForTwo(Bread))
      case _ => Seq.empty

    }


    breadsoffer ++ appleOffer

  }
    //val items = args.map(arg => Item.fromString(arg))
    //val items=Seq(Apples, Apples, Milk, Bread, Soup, Soup, Soup, Soup, Bread, Bread)
    val items= Seq(Apple, Apple, Bread,Bread,Bread,Milk,Soup)
     val allPossibleOffer: Seq[Offer] =items match {
       case Nil => Nil
       case head :: tail => calculateOffer(tail.:+(head))
     }


    val bill = Bill(items, allPossibleOffer)
    bill.display()



}
