package codingChallenge

object SubArrayWithGivenSum extends App {

  def solveWithNegatives(seq: Seq[Int], sum: Int): (Int, Int)  = {
    var numberStore = Map.empty[Int, Int]
    var sumSoFar = 0
    seq.zipWithIndex.foreach { case (value, index) =>
      sumSoFar += value
      if (sumSoFar == sum) return (0, index)
      else if (numberStore.contains(sumSoFar - sum))
        return (numberStore(sumSoFar - sum) + 1 , index)

      numberStore = numberStore + (sumSoFar -> index)
    }
    (-1, -1)
  }

  val result: (Int, Int) =solveWithNegatives(Seq(1, 4, 20, 3, 10, 5),33)
   val s= s"sum found between indexes ${ result._1.toString  +  "," +result._2.toString }"
  println(s)
}
