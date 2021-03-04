package numberProblems

import scala.util.Random

object ApproximatePi {
  //compute the pi using monte carlo Algo
  val random = new Random(System.currentTimeMillis)

  //TODO : npoints is the total points in sqaure
  def approximatePi(nPoints: Int): Double = {
    val nPointsInsideCircle = (1 to nPoints).map { _ =>
      val x = random.nextDouble
      val y = random.nextDouble
      // TODO :this is the square of the distance between center of circle
      // TODO: and this point which is represented (x,y)
      x * x + y * y
      //TODO:  square of the radius of circle is 1
    }.count(distance => distance < 1)
    nPointsInsideCircle * 4.0 / nPoints
  }

  def main(args: Array[String]): Unit = {
    println(approximatePi(1000))
  }
}
