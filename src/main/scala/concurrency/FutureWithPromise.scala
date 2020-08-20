package concurrency

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
object FutureWithPromise extends App {

case class User(name:String)
  case class Transaction(sender:String, receiver:String,amount:Double, status:String)

  object BankingUtils{
    val name ="Rock the JVM"
    def fetchUser(name:String): Future[User] = Future.apply{
      Thread.sleep(500)
      User(name)

    }
    def createTransaction(user: User, merchantName:String, amount:Double): Future[Transaction] =Future{
      Thread.sleep(1000)
      Transaction(user.name , merchantName,amount,status = "Success")
    }
    def purchase(userName: String, merchantName:String, costOfItem:Double) : String ={
      // fetch the user form db
      //create a transaction for the user
      //wait for the transaction to finish
      val transactionStatusFuture= for{
        user <- fetchUser(userName)
        transaction <- createTransaction(user,merchantName,costOfItem)
      } yield  transaction.status
      /*

         Both are blocking for at most the given Duration. here 2.seconds returns Duration object
        However, Await.result tries to return the future result right away
        and throws an exception if the future failed
       */
      Await.result(transactionStatusFuture,2.seconds) //implicit conversion -> pimp my library
    }
  }

}
