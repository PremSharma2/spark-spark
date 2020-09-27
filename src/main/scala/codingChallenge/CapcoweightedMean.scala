package codingChallenge

import org.apache.hadoop.mapred.InvalidInputException

import scala.util.{Success, Try,Failure}

object CapcoweightedMean  extends App {


  import scala.collection.JavaConversions._



    // Function to calculate weighted mean.
    def weightedMean(X: Array[Int], W: Array[Int], n: Int) = {
      Try {
        var sum: Int = 0
        var numWeight: Int = 0
        for (i <- 0 until n) {
          numWeight = numWeight + X(i) * W(i)
          sum = sum + W(i)
        }
        ((numWeight).toFloat / sum).toDouble
      }
    }


      // weight array and initialize it.
      val X: Array[Int] = Array(3,6)
      val W: Array[Int] = Array(4,2)
      // Calculate the size of array.
      val n: Int = X.length
      val m: Int = W.length
      // equal or not.
      val result: Try[Double] =weightedMean(X, W, n)
       result match {
         case Success(value) => println(value)
         case _  => Failure(throw new IllegalArgumentException)
       }





}
