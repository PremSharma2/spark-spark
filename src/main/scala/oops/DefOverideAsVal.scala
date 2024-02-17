package oops

object DefOverideAsVal {

/**
TODO
  In Scala, `val` and `def` are used to define values and methods, respectively.
   The decision to use `val` or `def` depends on
   whether you want the result to be eagerly evaluated (using `val`)
   or lazily evaluated (using `def`).
   In some cases, you might want to override a `def` method with a `val` to change its behavior.
   Let's explore a real-time project scenario where this can be applied.

TODO
  Real-time Project Scenario:
    Imagine you are working on a data processing application
   that deals with a large dataset.
   The application reads data from different sources,
   applies various transformations, and calculates different metrics.
   In this project, you have a base class called `DataProcessor`,
   and you plan to define a method to calculate a time-consuming metric.
   However, since the metric calculation is computationally intensive
   and only needed occasionally, you want to lazily evaluate it.
   But you also want to cache the result once it is computed to avoid redundant calculations.

  Here's how you can design it:



 */
  abstract class DataProcessor {
    // A method to calculate a time-consuming metric (lazily evaluated)
    def calculateMetric(): Double

    // Other methods for data processing can be defined here
  }

  class SampleDataProcessor extends DataProcessor {
    // Assume this class implements other methods as well

    // Override the calculateMetric() method as a 'val' to provide lazy evaluation and caching
    lazy val calculateMetric: Double = {
      // Time-consuming metric calculation
      // ...
      // Return the result
      42.0
    }
  }
 /*
TODO
  In this example, we use a `def` in the base class `DataProcessor`
  to define the method `calculateMetric()`.
  This means that any subclass of `DataProcessor` will have to implement this method.
  However, in the `SampleDataProcessor` class, we override the `calculateMetric()`
  method using a `val` instead of a `def`.
  This allows us to use lazy evaluation and caching.
  The metric will be calculated only when it is accessed for the first time.
  After that, the result will be cached,
  so any subsequent access to `calculateMetric`
   will return the cached value directly without re-computation.

    Using `lazy val` to override a `def` method is a powerful technique when you have expensive calculations that need to be performed only under certain conditions or once. It helps in optimizing the performance of the application and avoiding unnecessary calculations.


  */
}
