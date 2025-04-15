package collections.basic.operationAndTransformation

import scala.collection.immutable
import scala.collection.immutable.{NumericRange, Range}
import scala.util.matching.Regex

object Map_FlatMap_Filter_for extends App {
//todo : ->  LinearSeq LinkedList here
  val list = List(1, 2, 3)
  val input = List("hello-world", "scala", "prem")

  /**
  TODO
     Tests whether a predicate holds for at least one element of this traversable collection.
     Note: may not terminate for infinite-sized collections.
     this can be useful when we want to terminate the iteration here
     on this condition and return the result

   */

  val rExists: Boolean = input.exists(_.matches("hello-world"))

  val userRoles: List[String] = List("Editor", "Viewer", "Contributor")

  // Check if the user has at least one of the required roles
  val hasAccess: Boolean = userRoles.exists(role => role == "Admin" || role == "Manager")


  //TODO: the findFirstIn method finds the first match in the String and returns an Option[String]:
  //123 is Ans
  val numPattern = new Regex("[0-9]+")
  val address = "123 Main Street Suite 101"
  val idList= Seq("12567N","13576Y")

  val match1: Option[String] = numPattern.findFirstIn(address)
  val string= idList.find(_.startsWith("12567N"))

  match1.foreach { s =>
    println(s"Found: $s")
  }





  /**
  TODO
    Finds the first element which yields the largest value measured by function f
   */


  val maxby: String = input.maxBy(x => x.length())

  println(input.maxBy(x => x.length()))

  case class Employee(name: String, age: Int, salary: Double)

  val employees = List(
    Employee("Alice", 30, 50000.0),
    Employee("Bob", 28, 60000.0),
    Employee("Charlie", 35, 55000.0),
    Employee("David", 32, 65000.0)
  )

  val employeeWithHighestSalary = employees.maxBy(_.salary)

  println(s"Employee with highest salary: ${employeeWithHighestSalary.name}")

  println(list.head)
  println(list.headOption)
  println(list.tail)
  val l = List(1, 2, 3, 4)
  val l1 = List(5, 6, 7, 8)
  val alist = List(2, 3, 5, 7)



  /**
    TODO
       A copy of the general sequence with an element prepended.
      Note that :-ending operators are right associative (see example).
      A mnemonic for +: vs. :+ is: the CoLon goes on the Collection side.
     Also, the original general sequence is not modified, so you will want to capture the result.
     Example:
      scala> val x = List(1)
      x: List[Int] = List(1)
      scala> val y = 2 +: x
      y: List[Int] = List(2, 1)

   */


  val prepended = 1 +: alist // List(1,2,3,5,7)
  val prepen = 0 :: alist // this Right associative basically i.e alist :: 0



  //TODO  when you chain multiple :: operations together, like a :: b :: c :: list,
  // it associates to the right. This means that it's effectively evaluated as a :: (b :: (c :: list))
  val associtivoperatorlist: immutable.Seq[Int] = 1 :: 2 :: 3 :: Nil

  alist.::(0)
  val appended = alist :+ 9
  println(appended)
  println(l ++ l1)


  /**
TODO
   //List(List(1, 2, 3, 4), 5, 6, 7, 8)
  //Adds an element at the beginning of this list
  // you can pass list or any value
  //1 :: List(2, 3) = List(2, 3).::(1) = List(1, 2, 3)

   */
  val listOfIntegerAndList: immutable.Seq[Any]  = l :: l1

  println(l :: l1)

  println(l.::(0))

  val test: Seq[Int] = (1 to 5).toList // # List(1, 2, 3, 4, 5)
  (1 until 5).toList //# List(1, 2, 3, 4)

  (1 to 10 by 2).toList // # List(1, 3, 5, 7, 9) remove all elements divisible by 2
  (1 until 10 by 2).toList // # List(1, 3, 5, 7, 9)
  (1 to 10).by(2).toList // # List(1, 3, 5, 7, 9)
  //compiler will rewrite this like this l1 :: l now l will be prepended

  /*
   TODO
      def :::[B >: A](prefix: List[B]): List[B]
       Adds the elements of a given list i.e this in front of this prefix list.
    Example:
    List(1, 2) ::: List(3, 4) = List(3, 4).:::(List(1, 2)) = List(1, 2, 3, 4)
   */
  val listOfList: List[Int] = l ::: l1
  println(l ::: l1)



  /*
   TODO
       Applies a function f to all elements of this iterable collection.
   */

  list.foreach(println(_))
  // map
  //TODO override final def map[B, That](f: A => B)(implicit bf: CanBuildFrom[List[A], B, That])
  println(list.map(_ + 1))
  println(list.map(x => List(x + 1)))
 val rrr: immutable.Seq[List[Int]] = list.map(x => List(x + 1))

  //filter
  /**
    TODO
        Selects all elements of this traversable collection which satisfy a predicate.
   */

  println(list.filter(x => x % 2 == 0))
  println(list.filter(_ % 2 == 0))
  val regex = "a.c".r
  val tokens = List("abc", "axc", "abd", "azc")
  tokens filter (x => regex.pattern.matcher(x).matches)
  //result: List[String] = List(abc, axc, azc)

  //flatmap


  val transform: Int => (Int, Int) = x => (x, x + 1)

  //ETW
  //todo : -> transformer: A=>M[B]

  val transformer = (x: Int) => List(transform.apply(x))
  //TODO def flatMap[B, A](f: A => List[B])
  println(list.flatMap(transformer))
  println(list.map(transform))
  val numbers = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  //todo : -> iteration logic
  val combinations: Seq[String] = numbers.
    flatMap(n => chars.map(c => "" + c + n))

  println(combinations)

  val forcomprehension: Seq[String] =
    for {
      n: Int <- numbers
      c: Char <- chars

      if n % 2 == 0 // todo with if filter
    } yield "" + c + n
  println(forcomprehension)

  
  //todo : ->  syntax overload
  list.map(//pattern matching using partial function
    x => x * 2)
  // dropRight
  val m1 = List(1, 1, 3, 3, 3, 5, 4, 5, 2)

  // Applying dropRight method
  //It returns all the elements of the list except the last n elements.
  //List(1, 1, 3, 3, 3, 5)
  // i.e it will drop last 3 elements

  /**
   TODO
       The dropRight method in Scala is used to create a new collection
       by excluding a specified number of elements from the end of the original collection.
      It's often used when you want to trim elements from the end of a sequence
      or collection based on a certain condition or simply by a fixed count.
       Here's a real-time use case for the dropRight method:
TODO
       Suppose you are working on a web application that
       displays recent activity for users.
       This activity log is represented as a list of events.
       However, to avoid overwhelming the user with too much information,
       you want to display only the most recent events while excluding older ones.
   */
  val res = m1.dropRight(3)

  case class ActivityEvent(user: String, eventType: String, timestamp: Long)

  val activityLog: List[ActivityEvent] = List(
    ActivityEvent("user1", "login", 1678912400000L),
    ActivityEvent("user2", "comment", 1678921200000L),
    ActivityEvent("user1", "like", 1678930000000L),
    ActivityEvent("user2", "post", 1678931800000L),
    // ... more events ...
  )

  val recentEventsToShow: List[ActivityEvent] = activityLog.dropRight(2)


  // Displays output
  println(res)

  /*
  drop(n)	Return all elements after the first n elements
   */
  println(m1.drop(2))

  /**
   * TODO
      Method Definition: def dropWhile(p: (A) => Boolean): List[A]
      drops all elements which satisfy the predicate
      Return Type: It returns all the elements of the list except the dropped ones.
  */

  val m3 = List(4,2,4,3,4,2)

  // Applying dropWhile method
  val res2 = m3.dropWhile(x => {
    x % 2 == 0
  })
  println(res2)
  //Ans is List(3,4,2)
  /**
  TODO
      dropWhile discards all the items at the start of a collection for which the condition is true.
      It stops discarding as soon as the first item fails the condition.
      dropWhile drops 1 but stops when it reaches 2 because the condition _ % 2 != 0 is false.
      filter discards all the items throughout the collection where the condition is not true.
       It does not stop until the end of the collection.
       Use Case: Filtering Log Entries

TODO Use case for dropwhile
       Imagine you have a list of log entries where each entry represents
       a user action along with a timestamp.
       You want to process these log entries
       and remove
      all the entries that occurred before
      a certain point in time.
      The dropWhile method can be very useful for achieving this.
   */

  case class LogEntry(action: String, timestamp: Long)

  // Sample log entries
  val logEntries = List(
    LogEntry("login", 1629456000000L),  // Timestamp: 2021-08-21T00:00:00
    LogEntry("click", 1629499200000L),  // Timestamp: 2021-08-21T12:00:00
    LogEntry("logout", 1629513600000L), // Timestamp: 2021-08-21T16:00:00
    LogEntry("click", 1629571200000L)   // Timestamp: 2021-08-22T08:00:00
  )

  // todo: -> Timestamp before which log entries should be removed
  val cutoffTimestamp = 1629513600000L  //todo: -> Timestamp: 2021-08-21T16:00:00

  // todo: -> Drop log entries that occurred before the cutoff timestamp
  val filteredLogEntries = logEntries.dropWhile(entry => entry.timestamp < cutoffTimestamp)

  //todo: Print the filtered log entries
  filteredLogEntries.foreach(entry => println(s"${entry.action} - Timestamp: ${entry.timestamp}"))


  // Creating a list
  val m2 = List(1, 3, 5, 4, 2)

  // Applying dropWhile method
  val res1 = m1.dropWhile(x => {
    x % 2 != 0
  })

  // Displays output
  println(res)


  /**
TODO
    Method Definition : def find(p: (A) => Boolean): Option[A]
   Return Type :It returns an Option value containing the first element of the stated collection
   that satisfies the used predicate else returns None if none exists.
   */


/*
TODO
 An IndexedSeq indicates that random access of elements is efficient,
  such as accessing an Array element as arr(5000).
 By default, specifying that you want an IndexedSeq with Scala 2.10.x creates a Vector:

scala> val x = IndexedSeq(1,2,3)
 */




/**
TODO
      In Scala, an Iterator is a fundamental concept used
      to traverse through a collection of elements one at a time.
      It provides a way to lazily iterate over the elements of a collection,
      which can be especially useful for processing large datasets or streams
      of data without loading everything into memory at once.
      Processing Large Datasets:
         When dealing with large datasets that cannot fit
     entirely in memory, you can use an Iterator to process data in a memory-efficient manner.
    You can read data from a file, database, or network stream,
  and process each element as you iterate through it.
 */
  val iterator: Iterator[Int] = Iterator.apply(2, 4, 5, 1, 13)

  while (iterator.hasNext) {
    val nextNumber = iterator.next()
    println(nextNumber)
  }

  /**
  TODO
        Certainly, let's delve deeper into a real-time use case of using Scala `Iterator`.
  TODO
        **Real-Time Log Processing:**
TODO
     Imagine you are working on a real-time log processing system.
     The system receives logs from various sources,
     and you want to process these logs efficiently without consuming excessive memory.
     This is a perfect scenario for utilizing Scala `Iterator`.

TODO
 1. **Scenario Setup:**
   - The system receives log data from different servers, applications, or devices.
   - Logs can be generated at a high frequency, making it impractical to load all logs into memory simultaneously.
   - Each log entry needs to be analyzed, filtered, or transformed.


  TODO
    2. **Using Scala Iterator:**

      ` ``scala
    // Simulating a log stream with Iterator
     val logStream: Iterator[String] = ... // Obtain the log data iterator

   // Process logs
   logStream
     .filter(log => log.contains("error"))  // Filter logs containing errors
     .map(parseLog)                         // Parse log entries into structured data
     .foreach(processLogEntry)              // Process each log entry
   ```

3. **Explanation:**
TODO
   - The `logStream` is an `Iterator[String]` that lazily fetches log entries one at a time from a data source,
     such as a file or a network stream.
   - The `.filter()` operation processes logs on-the-fly,
     selecting only those containing the keyword "error".
      This is memory-efficient, as only relevant logs are processed.
   - The `.map()` operation transforms each log entry into a structured data format using the `parseLog` function.
   - The `.foreach()` operation processes each log entry,
     potentially performing actions like sending alerts or aggregating statistics.
     Again, this is done one log entry at a time, consuming minimal memory.

4. **Benefits:**
TODO
   - **Memory Efficiency:**
     The `Iterator` processes logs one by one,
     ensuring that only a single log entry is in memory at any given time.
     This is crucial when dealing with a high volume of logs.
   - **Real-Time Processing:**
  Logs are processed in real-time as they arrive,
  allowing you to react quickly to issues
  or extract valuable insights without delay.

  TODO
   - **Scalability:**
    The memory consumption remains relatively constant,
    making it suitable for handling varying workloads without causing memory exhaustion.

  TODO
   - **Lazy Evaluation:**
     The `Iterator`'s lazy evaluation ensures that only the necessary computations are performed, optimizing performance.
TODO
  In this scenario, the use of Scala `Iterator` enables you to efficiently process incoming logs in real-time,
  without needing to load all logs into memory simultaneously.
  This approach is well-suited for applications
  that need to process and analyze data on-the-fly,
  especially when dealing with potentially large and continuously streaming datasets like logs.

   // Simulating a log stream with Iterator
  val logStream: Iterator[String] = ??? // Obtain the log data iterator
TODO
  // Process logs
  logStream
    .filter(log => log.contains("error"))  // Filter logs containing errors
    .map(parseLog)                         // Parse log entries into structured data
    .foreach(processLogEntry)


   */


  /**
   TODO
       Use Case: Generating Date Range
       Imagine you need to generate a sequence of dates
       within a given range
       and perform calculations on them, such as :
       finding the weekdays, counting weekends,
       or performing some custom logic on each date.
       Using Iterator can help you achieve this efficiently
       without pre-generating the entire date sequence.
       In this example,
       the dateRange function generates an Iterator
      of LocalDate instances within the given date range.
      The Iterator.iterate function starts from the start date
      and increments it by one day at a time
      until the condition specified by takeWhile is met.
      This allows you to generate a sequence of dates lazily as needed.
   */

  import java.time.LocalDate
  import java.time.temporal.ChronoUnit


    def dateRange(start: LocalDate, end: LocalDate): Iterator[LocalDate] =
      Iterator.iterate(start)(_ plusDays 1).takeWhile(!_.isAfter(end))

    val startDate = LocalDate.of(2023, 1, 1)
    val endDate = LocalDate.of(2023, 1, 10)


    val weekdaysCount = dateRange(startDate, endDate).count {
      date =>
        date.getDayOfWeek.getValue <= 5 // 1-5 represents weekdays (Monday to Friday)
    }

    println(s"Weekdays count between $startDate and $endDate: $weekdaysCount")




  // Applying
/**
 * TODO
 *  Finds the first value produced by the iterator satisfying a
 *  predicate, if any.
 *
 *
 */


  //val iterator: Iterator[Int] = Iterator.apply(2, 4, 5, 1, 13)
  val result: Option[Int] = iterator.find(_ > 1)

  // Displays output
  println(result) //todo: ->  Some(2)

  /**
TODO
  init	All elements except the last one
   Real World Use Case: Processing Historical Data
TODO
   Imagine you have a list of daily stock prices,
   and you want to calculate the average price over a certain period,
   excluding the most recent day's price.
   The init method can help you achieve this
   by discarding the latest price before performing the calculation.
   */
  val initList: Seq[Int] = m2.init
  println(initList)

  val stockPrices: List[Double] = List(100.0, 105.5, 110.2, 108.8, 112.5, 115.0)
//most recent price   is 115.0
  val historicalPrices = stockPrices.init
  val averagePrice = historicalPrices.sum / historicalPrices.length

  println(s"Average stock price over historical days: $averagePrice")


  /**
TODO
  intersect(s)	Return the intersection of the list and another sequence s
   */

  val intersection: Seq[Int] = m1.intersect(m2)
  println(intersection)

  /**
TODO
      lastOption	The last element as an Option
   */

  val lastOption: Option[Int] = intersection.lastOption


  /**
TODO
  takeWhile(p)	The first subset of elements that matches the predicate p
   USe Case:
   Imagine you have a list of temperature readings collected from a sensor
   over a period of time. You want to extract the initial segment of temperature readings
   where the temperature remains within a certain comfortable range.
   The takeWhile method can be used to achieve this by
   filtering the data based on the condition.
   */


  val takeWhileSeq = Seq(2, 4, 6, 8, 3,5,7,9)
  val takenWhile: Seq[Int] = takeWhileSeq.takeWhile(x => {
    x % 2 == 0
  })

  val temperatureReadings: List[Double] = List(22.5, 23.0, 23.5, 24.0, 25.0, 25.5, 26.0, 26.5, 27.0)

  import scala.collection.immutable.NumericRange

  val start = 23.0
  val end = 25.0
  val step = 0.1

  //val comfortableTemperatureRange: NumericRange.Inclusive[Double] = NumericRange.inclusive(start, end, step)



  val targetTempRangeSpectrum = Range.Double.inclusive(start, end, step)


  val comfortableReadings = temperatureReadings.takeWhile(temperature => targetTempRangeSpectrum.contains(temperature))


  println(s"Initial comfortable temperature readings: $comfortableReadings")

  println(lastOption.get)
  println(takenWhile) // o/p should be [2,4,6,8]
}