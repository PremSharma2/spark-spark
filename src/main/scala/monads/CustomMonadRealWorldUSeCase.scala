package monads

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
object CustomMonadRealWorldUSeCase extends App {
// Exercise this census DO data object form data base
  case class Person(firstName:String,lastName:String){
    assert(firstName != null && lastName!=null)
  }

// api to fetch person
  // it is very defensive java style of coding looks ugly
  def getPerson(firstName:String,lastName:String): Person ={
    if(firstName!=null && lastName!=null) Person(firstName,lastName)
    else null
  }
  //Lets solve this problem with scala
  // hence all if and else hase been removed with this functional approach

  def getBetterPerson(firstName:String,lastName:String):Option[Person] ={
    Option(firstName).flatMap{
      firstName => Option(lastName).flatMap{lastName =>
        Option(Person.apply(firstName,lastName))

      }
    }
  }

  def getBetterPerson1(firstName:String,lastName:String):Option[Person]= {
    for{
       firstName <- Option(firstName)
       lastName <- Option(lastName)
    } yield Person.apply(firstName,lastName)
  }
  // asynchronus fetching of User from remote server
  case class User(id:String)
  case class Product(sku:String, price:Double)
  def getUSer(url:String):Future[User] = Future.apply{
    User.apply("Prem")
  }

// async call ot server
  def getLAstOrder(userID:String):Future[Product] = Future.apply{
    Product.apply("Nike-Tshirt", 99.99)
  }
  val myURL= "mystore.com/users/prem"
  // Now to solve this problem ETW patterns comes into picture
  // because first wee need to wait for the Result for this async call to user
  // and then we will extract the value form this and transform that value to anothet type
  // so this use case is best suited for ETW pattern
  //ETW implementation
  // For java F=Developers its look like calling listner to listen remote o/p
  getUSer(myURL).onComplete{
    case Success(User(id)) =>
      val lastOrder= getLAstOrder(id)
      lastOrder.onComplete{
        case Success(Product(sku,price)) =>
          val vatIncludedPrice= price*1.19
      }
  }
  // As this is monad so we have to take advantage of Monade flatMap method
  // Because ETW pattern best impl is done by monad flatMap transformer
// ist step of ETW fetch the value or Extract the value
val vatInclPrice = getUSer(myURL).
  // 2nd step transform it with monadic transformer
  flatMap(user => getLAstOrder(user.id)).
  // 3rd step is to use that o/p container wrapped  value    again  using map
  map(_.price*1.19)
}
