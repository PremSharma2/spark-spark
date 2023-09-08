package ImplicitsAndTypeClasses

import ImplicitsAndTypeClasses.MagnetPatternExercise1.TransactionMagnet.{BillPayment, CheckDeposit}

import scala.language.implicitConversions

object MagnetPatternExercise1 {

  // 1. Define a Magnet Trait
  trait TransactionMagnet {
    type Result

    def apply(): Result
  }

  // 2. Create Implicit Magnet Constructors

  // For Money Transfer
  case class MoneyTransfer(from: String, to: String, amount: Double)

  object TransactionMagnet {
    implicit def fromMoneyTransfer(transfer: MoneyTransfer): TransactionMagnet {
      type Result = String
    } = new TransactionMagnet {
      type Result = String

      def apply(): Result = s"Transferred $$${transfer.amount} from ${transfer.from} to ${transfer.to}."
    }

    // For Bill Payment
    case class BillPayment(account: String, vendor: String, amount: Double)

    implicit def fromBillPayment(payment: BillPayment): TransactionMagnet {
      type Result = String
    } = new TransactionMagnet {
      type Result = String

      def apply(): Result = s"Paid $$${payment.amount} to ${payment.vendor} from ${payment.account}."
    }

    // For Check Deposit
    case class CheckDeposit(account: String, checkNumber: String, amount: Double)

    implicit def fromCheckDeposit(deposit: CheckDeposit): TransactionMagnet {
      type Result = String
    } = new TransactionMagnet {
      type Result = String

      def apply(): Result = s"Deposited $$${deposit.amount} to ${deposit.account} using check ${deposit.checkNumber}."
    }
  }

  // 3. Single Method Accepting Magnet
  def executeTransaction(magnet: TransactionMagnet): magnet.Result = magnet()

  def main(args: Array[String]): Unit = {
    // Real-world usage
    val moneyTransfer: MoneyTransfer = MoneyTransfer("Account1", "Account2", 1000)
    val billPayment: BillPayment = BillPayment("Account1", "Vendor", 200)
    val checkDeposit: CheckDeposit = CheckDeposit("Account1", "CHK123", 500)

    println(executeTransaction(moneyTransfer)) // Output: Transferred $1000 from Account1 to Account2.
    println(executeTransaction(billPayment)) // Output: Paid $200 to Vendor from Account1.
    println(executeTransaction(checkDeposit)) // Output: Deposited $500 to Account1 using check CHK123.
  }


}
