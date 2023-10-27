package codingChallenge

object SynechronTest3 {


  case class Trade(accountName: String, accountNumber: String)

  case class SeparateTrades(trades: Stream[Trade] = Stream.empty[Trade]) {

    lazy val results: Map[String, Stream[Trade]] =
      trades.groupBy(_.accountName)

    val getTradesForAccount: String => Seq[Trade] = (accountName: String) =>
      results.getOrElse(accountName, Stream.empty).toList

    val tradeNames: Iterable[String] = results.keys

    def getTradeByName(tradeName: String): Option[Trade] =
      trades.find(_.accountName == tradeName)

    def getFilteredTradesForAccount(accountName: String, predicate: Trade => Boolean): Seq[Trade] =
      getTradesForAccount(accountName).filter(predicate)
  }

}
