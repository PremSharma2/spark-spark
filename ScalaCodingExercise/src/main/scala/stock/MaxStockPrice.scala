package stock

case class MaxStockPrice() extends{

  def findMaximumProfit(stockPrices: Seq[Int]): Option[Int] = {
    val maximumSellPricesFromIonward: Seq[Int] = stockPrices.view
      .scanRight(0)({ case (maximumPriceSoFar, dayPrice) =>
        Math.max(maximumPriceSoFar, dayPrice)
      })
      .toSeq
    val maximumSellPricesAfterI = maximumSellPricesFromIonward.drop(1)
    if (stockPrices.length < 2) None
    else
      stockPrices
        .zip(maximumSellPricesAfterI)
        .map({ case (buyPrice, sellPrice) =>
          getPotentialProfit(buyPrice = buyPrice, sellPrice = sellPrice)
        })
        .max
  }

  def getPotentialProfit(buyPrice: Int, sellPrice: Int): Option[Int] = {
    if (sellPrice > buyPrice) Some(sellPrice - buyPrice) else None
  }

}
