package concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
object FutureWithAwaitExercise extends App {

case class User(name:String)
  case class Transaction(sender:String, receiver:String,amount:Double, status:String)

  object BankingUtils{
    val name ="Rock the JVM"
    def fetchUser(name:String): Future[User] = Future.apply{
      // fetch the user from the database i.e make a call to database
      Thread.sleep(500)
      // object returned form database
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
      val transactionStatusFuture: Future[String] = for{
        user: User <- fetchUser(userName)
        transaction: Transaction <- createTransaction(user,merchantName,costOfItem)
      } yield  transaction.status
      /*

         Both are blocking for at most the given Duration.
         here 2.seconds returns Duration object
        However, Await.result tries to return the future result right away
        and throws an exception if the future failed
       */
      Await.result(transactionStatusFuture,2.seconds) //implicit conversion -> pimp my library
    }
  }
  val function: (String, Double) => String =BankingUtils.purchase("Prem",_: String,_: Double)
println(BankingUtils.purchase("Prem","Applestore= glasgow",750))

}
