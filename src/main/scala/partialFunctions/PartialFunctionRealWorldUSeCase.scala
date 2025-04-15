package partialFunctions

object PartialFunctionRealWorldUSeCase {

  //Adts
  sealed trait Transaction

  case class Deposit(amount: Double) extends Transaction

  case class Withdrawal(amount: Double) extends Transaction

  case class Transfer(amount: Double) extends Transaction

  case class LoanPayment(amount: Double) extends Transaction

  val transactionFeeCalculator: PartialFunction[Transaction, Double] = {
    case Deposit(_) => 0.0
    case Withdrawal(amount) => amount * 0.01
    case Transfer(amount) => amount * 0.02
  }


  def calculateFee(tx: Transaction): String = {
    if (transactionFeeCalculator.isDefinedAt(tx)) {
      s"Fee: ${transactionFeeCalculator(tx)}"
    } else {
      "Transaction type not supported for fee calculation."
    }
  }

  def main(args: Array[String]): Unit = {
    val tx1 = Deposit(1000)
    val tx2 = Withdrawal(500)
    val tx3 = LoanPayment(300)
    println(calculateFee(tx1)) // Fee: 0.0
    println(calculateFee(tx2)) // Fee: 5.0
    println(calculateFee(tx3)) // Transaction type not supported for fee calculation.
  }

}
