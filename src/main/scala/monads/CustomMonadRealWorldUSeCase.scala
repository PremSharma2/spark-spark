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

// async call Rest Api
  def getLastOrder(userID:String):Future[Product] = Future.apply{
    Product.apply("Nike-Tshirt", 99.99)
  }
  val myURL= "mystore.com/users/prem"
  // Now to solve this problem ETW patterns comes into picture
  // because first wee need to wait for the Result for this async call to user
  // Once this thread is done call back method onComplete is invoked by Execution Context
  // and then we will extract the value form this and transform that value to anothet type
  // so this use case is best suited for ETW pattern
  //ETW implementation
  // For java F=Developers its look like calling listner to listen remote o/p
  getUSer(myURL).onComplete{
    case Success(User(id)) =>
      val lastOrder: Future[Product] = getLastOrder(id)
      lastOrder.onComplete{
        case Success(Product(sku,price)) =>
          val vatIncludedPrice: Double = price*1.19
      }
  }
  // As this is monad so we have to take advantage of Monade flatMap method
  // Because ETW pattern best impl is done by monad flatMap transformer
// ist step of ETW fetch the value or Extract the value
val vatInclPrice: Future[Double] = getUSer(myURL).
  // 2nd step transform it with monadic transformer
  flatMap(user => getLastOrder(user.id)).
  // 3rd step is to use that o/p (container wrapped  value)    again  using map
    // i.e we want to map the values
  map(_.price*1.19)

  // now using for Comprehension
  val userContainer: Future[User] =getUSer(myURL)
  val vatInclusive: Future[Double] = for{
    user <- userContainer
    product <- getLastOrder(user.id)

  } yield product.price*1.19

  // double for loops
  val numbers= List(1,2,3)
  val chars= List('a','b','c')
  // here one imp thing to be noted here is that
  // we cant use flatMap inside FlatMap because flatmanp is the only function which is used to implement
  // ETW pattern so flatMap inside flatmap does not make any sense
  // so we can only use map
  val checkerBoard: Seq[(Int, Char)] = numbers.flatMap(num=> chars.map(char => (num,char)))
}
