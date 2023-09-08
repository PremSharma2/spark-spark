package typemembers

object TypeMemberExercise {
/*
TODO
    Advantages of Using Abstract Type Members
   API Simplification: Clients do not need to specify types when using the API;
   they just interact with the concrete implementations.
   The types are encapsulated within these concrete implementations.
TODO
    Type Relationship: Abstract type members can have bounds and
    relationships defined inside the trait,
     making them more context-aware.
     For example,
     if you wanted all MetricTypes to implement a common Metric trait,
      you could express that easily:
 */
class Counter
class Gauge
  trait MetricStorage {
    type MetricType // abstract type member
    def add(metric: MetricType): Unit
    def query(name: String): Option[MetricType]
  }

  class CounterStorage extends MetricStorage {
    type MetricType = Counter
    // implementation

    override def add(metric: Counter): Unit = ???

    override def query(name: String): Option[Counter] = ???
  }

  class GaugeStorage extends MetricStorage {
    type MetricType = Gauge
    // implementation

    override def add(metric: Gauge): Unit = ???

    override def query(name: String): Option[Gauge] = ???
  }

}
