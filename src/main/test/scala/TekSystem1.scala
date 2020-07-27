object TekSystem1 {


  def main(args: Array[String]): Unit = {
    // read the capacity and number of stops
    val line = scala.io.StdIn.readLine()
    val Array(totalCapacityOfTrain, totalNumberOfStopsInJourney) = line.split(" ").map(_.toLong)
    if (totalCapacityOfTrain <= 0 || totalCapacityOfTrain > Math.pow(10, 9)) {
      Console.err.println("please enter capacity between 1 and 10 ^ 9")
      System.exit(1)
    } else if (totalNumberOfStopsInJourney < 2 || totalNumberOfStopsInJourney > 100) {
      Console.err.println("please enter number of stops between 2 and 100")
      System.exit(1)
    } else {
      /*
      read the other input
      For example input is arraybuffer=(1 ,0, 1)
       */
      var arrBuffer = scala.collection.mutable.ArrayBuffer[(Int, Int, Int)]()
      var stationCounter = 0
      //We need to terminate loop at last stop of journey where train ends
      while (stationCounter < totalNumberOfStopsInJourney) {
        val input = scala.io.StdIn.readLine()
        val arr = input.split(" ")
        val inputArray = (arr(0).toInt, arr(1).toInt, arr(2).toInt)
        // (0,1,1)
        arrBuffer += inputArray
        //Counter incremented train reached to next station
        stationCounter += 1
      }
      //Total number of Input instruction should be equal to TotalNumber of Stops in Journey
      if (arrBuffer.size > totalNumberOfStopsInJourney) {
        Console.err.println("Number of Input entries should be same as number of stops")
        System.exit(1)
      } else {
        var possible = true
        var currentCapacityOfTrainWrtTotalCapacity = 0
        //Loop for processing the input 0,1,1
        for (i <- 0 until arrBuffer.size) {
          val (passengerVacatedTheTrain, passengerEnteredInTrain, passengerInWaitingAtPlatform) = arrBuffer(i.toInt)
          // check for left
          if (passengerVacatedTheTrain < 0 || passengerVacatedTheTrain > currentCapacityOfTrainWrtTotalCapacity) possible = false
          currentCapacityOfTrainWrtTotalCapacity -= passengerVacatedTheTrain
          if (passengerEnteredInTrain < 0 || passengerEnteredInTrain > totalCapacityOfTrain - currentCapacityOfTrainWrtTotalCapacity) possible = false
          currentCapacityOfTrainWrtTotalCapacity += passengerEnteredInTrain
          if (passengerInWaitingAtPlatform < 0 || (totalCapacityOfTrain > currentCapacityOfTrainWrtTotalCapacity && passengerInWaitingAtPlatform > 0)) possible = false
        }
        if (possible && currentCapacityOfTrainWrtTotalCapacity == 0) {
          Console.out.println("possible")
        } else {
          Console.out.println("impossible")
        }
      }
    }
  }

}
