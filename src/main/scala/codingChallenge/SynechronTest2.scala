package codingChallenge

object SynechronTest2 {


  case class Trade(accountName: String, accountNumber: String)

  case class SeparateTrades(trades: Stream[Trade] = Stream.empty[Trade]) {

    lazy val buildAccountMap: Map[String, Stream[Trade]] = {
      val results: Map[String, Stream[Trade]] = Map.empty
      val groupedTrades: Stream[(String, Stream[Trade])] = trades.groupBy(_.accountName).toStream
      trades.foldLeft(results) {
        case (map, trade) => map.updated(trade.accountName,groupedTrades.find(_._1 == trade.accountName).get._2)
      }
    }


    val getTradesForAccount: String => Seq[Trade] = (accountName: String) => buildAccountMap.getOrElse(accountName, Stream.empty)

    val tradeNames: Iterable[String] = buildAccountMap.values.flatten.map(_.accountName)

    def getTradeByName(tradeName: String): Option[Trade] = {
      val x = for {
        trade <- buildAccountMap.values.flatten
        if (trade.accountName == tradeName)
      } yield trade
      x.headOption
    }

    def getFilteredTradesForAccount(accountName: String, predicate: Trade => Boolean): Seq[Trade] = {
      getTradesForAccount(accountName).filter(predicate)
    }


  }
}
