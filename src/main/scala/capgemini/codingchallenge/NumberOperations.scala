package capgemini.codingchallenge

object NumberOperations {

  def roundTodecimals(double: Double): Double = {
    BigDecimal(double).setScale(2, BigDecimal.RoundingMode.DOWN).toDouble
  }

}
