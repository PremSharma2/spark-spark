package codingChallenge

import scala.:+

object SynechronTest1 extends App{


  import scala.collection.mutable

  case class Trade(accountName: String, accountNumber: String)

  object TradeAndAccount {
    def apply(trades: Seq[Trade]): TradeAndAccount = {
      val results = trades.foldLeft(mutable.Map.empty[String, Seq[Trade]].withDefaultValue(Seq.empty)) { (map, trade) =>
        val updatedTrades = map(trade.accountName) :+ trade
        map.updated(trade.accountName, updatedTrades)
      }
      new TradeAndAccount(results.toMap)
    }

    def unapply(separatedTrades: TradeAndAccount): Option[Map[String, Seq[Trade]]] =
      Some(separatedTrades.results)
  }

  class TradeAndAccount(private val results: Map[String, Seq[Trade]]) {
    def getTradesForAccount(accountName: String): Seq[Trade] = results(accountName)

    def getFilteredTradesForAccount(accountName: String, predicate: Trade => Boolean): Seq[Trade] = {
      getTradesForAccount(accountName).filter(predicate)
    }

    def getAllTradeNames: Seq[String] = results.keys.toSeq

    def getTradeByName(tradeName: String): Option[Trade] = {
      results.values.flatten.find(_.accountName == tradeName)
    }
  }



      val trades = Seq(
        Trade("Account1", "123"),
        Trade("Account2", "456"),
        Trade("Account1", "789")
      )

      val separateTrades = TradeAndAccount(trades)

      println(separateTrades.getTradesForAccount("Account1"))
      println(separateTrades.getFilteredTradesForAccount("Account1", _.accountNumber == "123"))
      println(separateTrades.getAllTradeNames)
      println(separateTrades.getTradeByName("Account2"))


}
