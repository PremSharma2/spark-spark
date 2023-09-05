package functional.programming

object CurryingUseCase {
  /**
TODO
     Real-world Use Case:
   Configurable Data Transformation Pipeline
   Let's say you're working on a data transformation pipeline
   in a data-intensive application.
   You receive data from multiple sources,
   and you need to apply several transformations
   before storing it. Each transformation depends on some configuration.
   Here's how you can use curried functions
   to create a configurable data transformation pipeline.
   First, we define some basic transformations:
   */

  def filterOutliers(config: Double)(data: Seq[Double]): Seq[Double] = {
    data.filter(x => x <= config)
  }

  def scaleData(factor: Double)(data: Seq[Double]): Seq[Double] = {
    data.map(_ * factor)
  }

  def addNoise(noise: Double)(data: Seq[Double]): Seq[Double] = {
    data.map(_ + Math.random() * noise)
  }
  val filterConfigured = filterOutliers(100.0) _
  val scaleConfigured = scaleData(2.0) _
  val addNoiseConfigured = addNoise(1.0) _

  //Finally, you can compose these transformations:
  val pipeline = filterConfigured andThen scaleConfigured andThen addNoiseConfigured

  val data = Seq(50.0, 200.0, 75.0)
  val transformedData = pipeline(data)


}
