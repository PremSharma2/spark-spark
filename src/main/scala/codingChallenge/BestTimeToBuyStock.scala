package codingChallenge
/*
Say you have an data array of stocks
for which the ith element is the price of a given stock on day i. i.e index of array is day

If you were only permitted to complete at most one transaction
 (i.e., buy one and sell one share of the stock), design an algorithm to find the maximum profit.

Note that you cannot sell a stock before you buy one.

Input: [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
             Not 7-1 = 6, as selling price needs to be larger than buying price.
Example 2:

Input: [7,6,4,3,1]
Output: 0
Explanation: In this case, no transaction is done, i.e. max profit = 0.

for (i <- 0 until stockData.length - 1; j <- i + 1 until stockData.length) {
        val profit: Int = stockData(j) - stockData(i)
        if (profit > maxprofit) maxprofit = profit
      }
 */
object BestTimeToBuyStock {
  import scala.collection.JavaConversions._

    def maxProfit(stockData: Array[Int]): Int = {
      var maxprofit: Int = 0
      for  {
        i <- 0 until stockData.length - 1
        j <- i + 1 until stockData.length
        if(stockData(j) - stockData(i)> maxprofit)
       maxprofit = stockData(j) - stockData(i)
      } maxprofit
      maxprofit
    }



    def maxProfit1(prices: Seq[Int]): Int = {
      var minprice: Int = java.lang.Integer.MAX_VALUE
      var maxprofit: Int = 0
      for (i <- 0 until prices.length) {
        if (prices(i) < minprice) minprice = prices(i)
        else if (prices(i) - minprice > maxprofit)
          maxprofit = prices(i) - minprice
      }
      maxprofit
    }

  def climbStairs(n: Int): Int = {
    def climb(i: Int, n: Int): Int = {
      if (i > n) {
        0
      }
      if (i == n) {
        1
      }
      climb(i + 1, n) + climb(i + 2, n)
    }
    climb(0, n)
  }



}
