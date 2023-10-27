package actorsInDepth

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable.{Map => MutableMap}

object BreakingActorEncapsulation {

  type CreditCardID = String
  type CreditCardType = String
  type BalanceOfTheCreditCard = Double
  type Amount = Double

  /**
    TODO
        NEVER PASS MUTABLE STATE TO OTHER ACTORS.
        NEVER PASS THE CONTEXT REFERENCE TO OTHER ACTORS.
         Same for Futures.
   */

  // naive bank account

  /**
   * TODO
   * defining the protocols for Account Actor or the Events for Accout actor
   */
//Adts
  sealed trait AccountCommand

  case class Deposit(cardId: CreditCardID, amount: Amount) extends AccountCommand

  case class Withdraw(cardId: CreditCardID, amount: Amount) extends AccountCommand

  case class CreateCreditCard(cardId: CreditCardID) extends AccountCommand

  case object CheckCardStatuses extends AccountCommand



  /**
   * TODO
   * protocols for Credit Card Actor
   *
   *
   */
  sealed trait CreditCardCommand
  case class AttachToAccount(balances: MutableMap[CreditCardID, BalanceOfTheCreditCard], cards: MutableMap[CreditCardType, ActorRef[CreditCardCommand]]) extends CreditCardCommand
  case object CheckStatus extends CreditCardCommand



  //TODO BankAccount Actor

  object NaiveBankAccountActor {
    def apply(): Behavior[AccountCommand] = Behaviors.setup {
      context =>
      val accountBalances: MutableMap[CreditCardID, BalanceOfTheCreditCard] = MutableMap()
      val cardMap: MutableMap[CreditCardType, ActorRef[CreditCardCommand]] = MutableMap()

      // define the behavior of the Actor
      Behaviors.receiveMessage {
        case CreateCreditCard(cardId) =>
          // create the child actor for creating card
          context.log.info(s"[Bank-Account-Actor]:-> Creating card $cardId")
          val creditCardRef = context.spawn(CreditCardActor(cardId), "CreditCardActor"+cardId)
          //send and AttachToAccount message to the child actor
          creditCardRef ! AttachToAccount(accountBalances, cardMap)
          //change the behavior of the Account actor
          Behaviors.same
        case Deposit(cardId, amount) =>
          val oldBalance: BalanceOfTheCreditCard = accountBalances.getOrElse(cardId, 0.0)
          context.log.info(s"[Bank-Account-Actor]:-> Depositing $amount via card $cardId, balance on card: ${oldBalance + amount}")
          accountBalances += cardId -> (amount + oldBalance)
          Behaviors.same

        case Withdraw(cardId, amount) =>
          val oldBalance: Double = accountBalances.getOrElse(cardId, 0)
          if (oldBalance < amount) {
            context.log.warn(s"[Bank-Account-Actor]:-> Attempted withdrawal of $amount via card $cardId: insufficient funds")
            Behaviors.same
          } else {
            context.log.info(s"[Bank-Account-Actor]:-> Withdrawing $amount via card $cardId, balance on card: ${oldBalance - amount}")
            accountBalances += cardId -> (oldBalance - amount)
            Behaviors.same
          }

        case CheckCardStatuses =>
          context.log.info(s"[Bank-Account-Actor]:-> Checking all card statuses")
          cardMap.values.foreach(cardRef => cardRef ! CheckStatus)
          Behaviors.same
      }
    }

    // Define the Credit Card Actor
    object CreditCardActor {
      def apply(cardId: CreditCardID): Behavior[CreditCardCommand] = Behaviors.receive {
        (context, message) =>
        message match {
          case AttachToAccount(balances, cards) =>
            context.log.info(s"[Credit-Card-Actor][$cardId] Attaching to bank account")
            balances += cardId -> 0.0
            cards ++ cardId -> context.self
            Behaviors.same

          case CheckStatus =>
            context.log.info(s"[Credit-Card-Actor][$cardId] All things green.")
            Behaviors.same
        }
      }
    }
  }
  def main(args: Array[String]): Unit = {
    val userGuardian: Behavior[Unit] = Behaviors.setup { context =>
      val bankAccount = context.spawn(NaiveBankAccountActor(), "bankAccount")

      bankAccount ! CreateCreditCard("gold")
      bankAccount ! CreateCreditCard("premium")
      bankAccount ! Deposit("gold", 1000)
      bankAccount ! CheckCardStatuses

      Behaviors.empty
    }

    val system = ActorSystem(userGuardian, "DemoNaiveBankAccount")
    Thread.sleep(10000)
    system.terminate()
  }
}
