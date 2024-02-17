package adts

object AdtsAndSmartConstructor {


  //Adts

  sealed trait Account {
    def id: String

    def balance: BigDecimal
  }

  case class SavingsAccount(id: String, balance: BigDecimal, interestRate: BigDecimal) extends Account

  case class CheckingAccount(id: String, balance: BigDecimal) extends Account

  case class BusinessAccount(id: String, balance: BigDecimal, overdraftLimit: BigDecimal) extends Account

  //smart constructor

  /** _
   * Smart constructors are methods used to instantiate objects while enforcing certain invariants or rules.
   */
  object Account {
    def createSavingsAccount(id: String, balance: BigDecimal, interestRate: BigDecimal): Either[String, SavingsAccount] = {
      if (balance < 0) Left("Balance cannot be negative")
      else Right(SavingsAccount(id, balance, interestRate))
    }

    def createCheckingAccount(id: String, balance: BigDecimal): Either[String, CheckingAccount] = {
      if (balance < 0) Left("Balance cannot be negative")
      else Right(CheckingAccount(id, balance))
    }

  }

}
