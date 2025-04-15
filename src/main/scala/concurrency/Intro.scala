package concurrency

import java.util.concurrent.Executors
import java.lang.Runnable
import java.lang.Thread

object Intro  extends  App {

  //jvm threads
  val aThread= new Thread(
    new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  aThread.start()

  val runnable: () => Unit = () => (1 to 5) .foreach(_ => println("Hello"))
//val aThreadHello= new Thread(runnable)
  val pool=Executors.newFixedThreadPool(2)
 // pool.execute(() => println("hello Thread pool"))

 // val pool = java.util.concurrent.Executors.newFixedThreadPool(2)

  1 to 2 foreach { x =>
    pool.execute(
      new Runnable {
        def run() {
          println("n: %s, thread: %s".format(x, Thread.currentThread.getId))
          val account =new BankAccount(50000)
          account.buy(account,"shoes",3000)
          Thread.sleep(1000)
        }
      }
    )
  }

  class BankAccount(var amount:Int){
    override def toString: String = "" + amount

    def buy(account: BankAccount,thing:String,price:Int) = {
      account.amount -= price
      println("I have bought " + thing)
      println("My account is now " + account)

    }
    def buySafe(account: BankAccount,thing:String,price:Int) = {
      account.synchronized{
        account.amount -= price
        println("I have bought " + thing)
        println("My account is now " + account)
      }
    }


  }
 /*var x=0
  Runable runnable = () => x += 1
  val threads= (1 to 100).map(_ => new Thread(() => x += 1))

  */
}
