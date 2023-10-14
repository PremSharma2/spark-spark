package lazyevaluation

object StreamUseCase {


  /**
  TODO
       Below is a simplified Scala example that represents
       a basic trading platform using Scala's Stream. In this example,
       I'm creating a TradeOrder class to represent an order,
       and then a stream of such orders.
       We'll perform some basic operations on this stream to filter and process the orders.
   **/

  case class TradeOrder(id: Int, symbol: String, quantity: Int, orderType: String)
  //Now, let's assume you have a function
  // that can generate a Stream of TradeOrder representing incoming orders:
//its a pooling function listing to Kafka topic

  def incomingOrders(startId: Int): Stream[TradeOrder] = {
    // Simulating orders. In a real-world scenario, these would come from some external source.
    val order = TradeOrder(startId, if (startId % 2 == 0) "AAPL" else "GOOGL", startId * 10, if (startId % 3 == 0) "buy" else "sell")
  //The expression order #:: incomingOrders(startId + 1) creates
  // a new Stream node where the head is order and the tail is
  // the result of the recursive call to incomingOrders(startId + 1).
  // Due to the lazy nature of Stream,
  // the tail won't be evaluated until accessed,
  // making it efficient for infinite sequences.
    order #:: incomingOrders(startId + 1)
  }

  def processBuyOrder(order: TradeOrder): Unit = {
    println(s"Processing buy order: ${order.id} for ${order.quantity} shares of ${order.symbol}")
  }

  def processSellOrder(order: TradeOrder): Unit = {
    println(s"Processing sell order: ${order.id} for ${order.quantity} shares of ${order.symbol}")
  }

  def main(args: Array[String]): Unit = {
    // Create a stream of incoming orders starting with id 1
    val orders = incomingOrders(1)

    // Filter out buy orders and process them
    orders.filter(_.orderType == "buy").take(10).foreach(processBuyOrder)

    // Filter out sell orders and process them
    orders.filter(_.orderType == "sell").take(10).foreach(processSellOrder)

    // Filter out AAPL buy orders, transform them to double the quantity, and then process them
    orders.filter(o => o.orderType == "buy" && o.symbol == "AAPL").map(o => o.copy(quantity = o.quantity * 2)).take(10).foreach(processBuyOrder)
  }



}
