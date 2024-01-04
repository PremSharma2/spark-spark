
import calculator.{SeparateTradesByAccount, Trade}
import org.scalatest.{FlatSpec, Matchers}

class TradeCalcTest extends FlatSpec with Matchers {

  "SeparateTradesByAccount" should "correctly separate trades by account" in {
    // Given the input
    val trade1 = Trade("Account1", "123")
    val trade2 = Trade("Account1", "456")
    val trade3 = Trade("Account2", "789")


    // When api is invoked
    val trades = Seq(trade1, trade2, trade3)
    val separatedTrades = SeparateTradesByAccount(trades)

    //Then result shd be like this
    separatedTrades.getTradesForAccount("Account1") should contain theSameElementsInOrderAs Seq(trade1, trade2)
    separatedTrades.getTradesForAccount("Account2") should contain only trade3
  }


  "SeparateTradesByAccount" should "return an empty list for non-existing account" in {
    // Given that
    val trade1 = Trade("Account1", "123")
    val trade2 = Trade("Account1", "456")
    val trade3 = Trade("Account2", "789")


    // When
    val trades = Seq(trade1, trade2)
    val separatedTrades = SeparateTradesByAccount(trades)

    //Then
    separatedTrades.getTradesForAccount("NonExistingAccount") shouldBe empty

  }

  "SeparateTradesByAccount " should "correctly filter trades based on the predicate" in {
    // Given that
    val trade1 = Trade("Account1", "123")
    val trade2 = Trade("Account1", "456")
    val trade3 = Trade("Account2", "789")


    // When
    val trades = Seq(trade1, trade2, trade3)
    val separatedTrades = SeparateTradesByAccount(trades)

    //Then
    val filteredTrades = separatedTrades.getFilteredTradesForAccount("Account1", _.accountNumber == "123")
    filteredTrades should contain only trade1

  }

  "SeparateTradesByAccount API " should "return all trade names " in {
    // Given that
    val trade1 = Trade("Account1", "123")
    val trade2 = Trade("Account1", "456")
    val trade3 = Trade("Account2", "789")


    // When
    val trades = Seq(trade1, trade2, trade3)
    val separatedTrades = SeparateTradesByAccount(trades)

    //Then
    separatedTrades.getAllTradeNames should contain allOf("Account1", "Account2")

  }


  "SeparateTradesByAccount API " should "return trade by name " in {
    // Given that
    val trade1 = Trade("Account1", "123")
    val trade2 = Trade("Account1", "456")
    val trade3 = Trade("Account2", "789")


    // When
    val trades = Seq(trade1, trade2, trade3)
    val separatedTrades = SeparateTradesByAccount(trades)

    //Then
    separatedTrades.getTradeByName("Account1") shouldBe Some(trade1)
    separatedTrades.getTradeByName("NonExistingAccount") shouldBe None
  }

}
