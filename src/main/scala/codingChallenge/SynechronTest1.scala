package codingChallenge

object SynechronTest1 {


  case class Trade(accountName: String, accountNumber: String)

  case class SeparateTrades(results: Map[String, Stream[Trade]] = Map.empty) {

    def buildAccountMap(trades: Stream[Trade]): Map[String, Stream[Trade]] = {
      val groupedTrades: Stream[(String, Stream[Trade])] = trades.groupBy(_.accountName).toStream
      trades.foldLeft(results) {
        case (map, trade) => map.updated(trade.accountName, groupedTrades.find(_._1 == trade.accountName).get._2)
      }
    }


    val getTradesForAccount: String => Seq[Trade] = (accountName: String) => results.getOrElse(accountName, Stream.empty)

    val tradeNames: Iterable[String] = results.values.flatten.map(_.accountName)

    def getTradeByName(tradeName: String): Option[Trade] = {
      val x = for {
        trade <- results.values.flatten
        if (trade.accountName == tradeName)
      } yield trade
      x.headOption
    }

    def getFilteredTradesForAccount(accountName: String, predicate: Trade => Boolean): Seq[Trade] = {
      getTradesForAccount(accountName).filter(predicate)
    }


  }
}
