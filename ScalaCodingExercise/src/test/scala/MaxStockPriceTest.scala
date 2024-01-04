import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AsyncFlatSpec
import stock.MaxStockPrice



class MaxStockPriceTest extends AsyncFlatSpec with Matchers {

  "wordCounter" should "correctly store the input with considering all obligations " in {
    // Given
  val stockPrices= Seq(163,112,105,100,151)
    val wordCount: MaxStockPrice = MaxStockPrice()

    // When
    val outputSummary: Option[Int] = wordCount.findMaximumProfit(stockPrices)

    // Then
    outputSummary.contains(51) should be(true)
  }
}
