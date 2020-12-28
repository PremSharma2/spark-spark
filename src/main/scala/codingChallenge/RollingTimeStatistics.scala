package codingChallenge

object RollingTimeStatistics {


  case class Transaction(
                          transactionId: String,
                          accountId: String,
                          transactionDay: Int,
                          category: String,
                          transactionAmount: Double)


  case class Transaction1(creditCardID:Int,value:Int , timeStamp :Long)


  val txns= List(
    Transaction1(1,10,30),
    Transaction1(1,10,60),
    Transaction1(1,10,90),
    Transaction1(1,10,200),
    Transaction1(1,10,311)
  )

val transactions = List(
  Transaction("T000942","A38",28,"EE",694.54),
  Transaction("T000943","A35",28,"CC",828.57),
    Transaction("T000944","A26",28,"DD",290.18),
    Transaction("T000945","A17",28,"CC",627.62),
    Transaction("T000946","A42",28,"FF",616.73),
    Transaction("T000947","A20",28,"FF",86.84),
    Transaction("T000948","A14",28,"BB",620.32),
    Transaction("T000949","A14",28,"AA",260.02),
    Transaction("T000950","A32",28,"AA",600.34)
)

/*

  Transaction(T000949,A14,28,AA,260.02)
  Transaction(T000950,A32,28,AA,600.34)

•The total transaction value of transactions type “AA” in the previous 5 days per account

•The average transaction value of the previous 5 days of transactions per account

The output ideally should contain one line per day per account id and each line should contain each of the calculated statistics:

My code for the first 5 days looks like:
 */



  val a = transactions.
    filter(trans => trans.transactionDay <= 5).
    groupBy(_.accountId).
    mapValues(trans => (trans.map(amount =>
      amount.transactionAmount).sum/trans.map(amount =>
      amount.transactionAmount).size,trans.filter(trans =>
      trans.category == "AA").map(amount =>
      amount.transactionAmount).sum))

  def calcStats(ts: List[Transaction], day: Int): Map[String, (Double, Double)] = {
    // all transactions for date range, grouped by accountID
    val atsById = ts
      .filter(trans => trans.transactionDay >= day - 5 && trans.transactionDay < day)
      .groupBy(_.accountId)
    // "AA" transactions summmed by account id
    val aaSums = atsById.mapValues(_.filter(_.category == "AA"))
      .mapValues(_.map(_.transactionAmount).sum)
    // sum of all transactions by account id
    val allSums = atsById.mapValues(_.map(_.transactionAmount).sum)
    // count of all transactions by account id
    val allCounts = atsById.mapValues(_.length)
    // output is Map(account id -> (aaSum, allAve))
    allSums.map { case (id, sum) =>
      id -> (aaSums.getOrElse(id, 0.0), sum / allCounts(id)) }
  }


  def main(args: Array[String]): Unit = {
 println(calcStats(transactions,30))
  }
}
