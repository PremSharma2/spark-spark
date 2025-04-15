package collections.basic.operationAndTransformation

import collections.basic.operationAndTransformation.ForComprehensionInDepth.{monthNames, monthlyConsumptionEnergyAmount}

import java.time.ZonedDateTime
import scala.collection.SeqView
import scala.io.Source

object ForComprehensionInDepth  extends App {


  /**
   * TODO
   * Welcome back. We were discussing Scala control structures.
   * The last item among the control abstraction is the for expression.
   * The Scala's for loop is a swiss army knife of iterations.
   * Here is the general structure of the for expression.
   *
   * // for ( seq ) yield { expr }
   *
   * TODO
   * It looks simple, but I realized that it is quite confusing to explain the above structure.
   * Let me take a progressive approach to explain that. To simplify the structure,
   * let's ignore the yield for now. The yield is optional.
   * You will be using it most of the time, but let's keep it aside for the moment.
   * Now the structure looks like this.
   *
   * for ( seq ) { expr }
   *
   *
   *
   *
   * TODO
   * Let's try to understand this structure.If you learned other languages,
   * the above structure should look familiar.
   * The things inside the parenthesis will control the number of iterations,
   * and those curly braces represent the body of the loop.
   * That's how it is in most of the languages.
   *
   * TODO
   * for (i <- 1 to 10) {
   * statement - 1;
   * statement - 2;
   * }
   *
   * TODO
   * You can have one or more expressions within the body.
   * If you have a single expression, curly braces are optional.
   * We don't have any complexity for the body of the loop.
   * It is almost same as any other language.
   * But the seq is somewhat complicated. It keeps confusing a lot of people.
   *
   * TODO
   * The sequence generator in Scala for loop
   * Let’s start with the simplest form of the seq and then expand it step by step.
   * The simplest form of seq is a generator that looks something like below.
   *
   * e <- collection
   *
   * The collection is a Scala collection,
   * and e is a value that binds to each element of the collection.
   * Let's take an example.
   *
   * val seq = 1 to 5
   * // seq is a Range collection with ten elements.
   * //Now I can iterate through this collection using a for Loop.
   * for (i <- seq) println(i)
   * // You can remove the middleman and get a collection on the fly.
   * for (i <- 1 to 5) println(i)
   */


  // or we can write this
  val seq = List("India", "USA", "China", "Japan")

  for (i <- seq) println(i)
  // TODO we can also write like this like we use to do in traditional for loop in java
  for (i <- seq) {
    println(i) //todo:  statement 1 body of loop
  }

  // the compiler will write this like that
  seq.foreach(println(_))

  //TODO or we can write this and we will using this in FP way not the last one

  for {
    element <- seq

  } println(element)

  // TODO :-> or we can use pattern match for every iteration
  val pf: PartialFunction[String, Unit] = {
    case "India" => println("Delhi")
    case "USA" => println("Washington D.C.")
    case "Japan" => println("Tokyo")
    case _ => println("I don't know")
  }


  val result: Unit = for (country <- List("India", "USA", "China", "Japan")) {
    country match {
      case "India" => println("Delhi")
      case "USA" => println("Washington D.C.")
      case "Japan" => println("Tokyo")
      case _ => println("I don't know")
    }
  }
  // compiler will convert it into this like
  List("India", "USA", "China", "Japan").foreach(pf)

  // or for pattern match on every iteration this is the correct syntax
  val rs: Unit = for (element <- seq) element match {
    case "India" => ""
    case "USA" => ""
    case "Japan" => ""
    case _ => ""
  }



  /*
  TODO
     Internal mechanics for the scala for loop
    The Scala for loop is just a syntactic sugar for Higher Order Control Abstractions.
    Internally, both are same. What does it mean?
    That means the Scala compiler will convert the for loop to a combination of following control abstractions.
  todo
    foreach
   map
   flatMap
   withFilter : ITS FOR  lazy

TODO
     In other words, Scala doesn't have a for loop.
    It's just a syntactic sugar for a set of these methods. So,
   if you don't like the for loop,
 you can manage to code in Scala without even worrying about the for loops.
 The real purpose of the Scala for expression is to write the code in a way that makes more sense.
  You should use the for expression when you think your code is getting
  too cryptic using these methods and it would make more sense if you implement it using a for expression.
TODO
 The Yield in Scala for expression
 Now let's bring the yield back into the structure.
TODO
 for ( seq ) yield { expr }
 in the absence of yield for comprehension behaves like forEach control abstraction
 because forEach returns the unit where as when yield comes it becomes like
 we applying map function after each iteration
 IT WILL be liek seq.flatmap(s => _.map) or seq.map(f)
   */

  // TODO Its similar like we have used partial function inside map
  // TODO yield converted <- to map , earlier it was foreach
  val result1: Seq[String] = for (country <- List("India", "USA", "China", "Japan"))
    yield {
      country match {
        case "India" => "Delhi"
        case "USA" => "Washington D.C."
        case "Japan" => "Tokyo"
        case _ => "I don't know"
      }
    }
  val seqView = List("India", "USA", "China", "Japan")
  val output1 = for (_ <- seqView) yield {
    pf
  }
  // so it will look like
  seqView.map(pf)

  //Compiler will convert this into
  val pf1: PartialFunction[String, Unit] = {
    case "India" => "Delhi"
    case "USA" => "Washington D.C."
    case "Japan" => "Tokyo"
    case _ => "I don't know"
  }
  //TODO : compiler will convert this into like this
  val list = List("India", "USA", "China", "Japan")
  list.map(pf1)

  // here As we can see yield  is working as we are applying the map function
  // to each single element
  // of Seq and transforming that into another Seq[String]
  // speciality of yield is that it will return a value for those pattern which are not matched also
  // we say some default values will be returned This one is correct syntax for pattern match on every iteration

  val result2: Seq[String] = for (country <- List("India", "USA", "China", "Japan")) yield {
    country match {
      case "India" => "Delhi"
      case "USA" => "Washington D.C."
      case "Japan" => "Tokyo"
      case _ => "I don't know"
    }
  }
  println(result2)

  //TODO :  more examples of for comprehension

  val filePath = "C:\\prem\\prem\\Data\\Spark_VM\\emp.txt"
  val source = Source.fromFile(filePath)
  val dataSeq = try source.getLines().toList finally source.close()

  for (line <- dataSeq) {
    val fields: Array[String] = line.split(",")
    println(fields.apply(0) + "----" + fields.apply(1) + "----" + fields.apply(2))
  }


  // TODO : here compiler will convert this into the following
  val xx: Unit = dataSeq.foreach {
    line =>
      val fields = line.split(",")
      println(fields.apply(0) + "----" + fields.apply(1) + "----" + fields.apply(2))
  }


  //TODO Lets put assignment inside the For not in the body this is more clean code
  // make body as simple as possible

  for {
    line <- dataSeq
    fields = line.split(",")
  } println(fields.apply(0) + "----" + fields.apply(1) + "----" + fields.apply(2))

  // applying if filter inside for
  /*
  TODO
    In crux For Comprehension has three components
   A generator
   B a defination or assignment
   C Filter

   like this in the
   line <- dataSeq
     fields = line.split(",")
   */

  val myresult: Seq[String] = for {
    line <- dataSeq // TODO: 1 step: generator and it control the number of iterations

    //TODO : Second step : assignment -  for each record perform these operations
    record: Array[String] = line.split(",") //todo  assignment

    //todo: if filter i.e using this we will filter the current iteration of record
    if record.apply(2).equals("SALESMAN")
  } yield (record.apply(0) + "----" + record.apply(1) + "----" + record.apply(2))



  //TODO : Transforming a dataset of customer transactions to calculate total sales per customer.

  val transactions: List[(String, Double)] = List(("Alice", 50.0), ("Bob", 40.0), ("Alice", 100.0))

  val totalSalesPerCustomer = for {
    //todo : Generator
    (customer, _) <- transactions
    //todo assignment
    groupedByCustomer = transactions.groupBy(_._1)
    total = groupedByCustomer.mapValues(_.map(_._2).sum)

    //todo we are not using withFilter here its not required here
  } yield (customer, total(customer))


  // or for complex map and flatmap combination we can use this approach as well
  // TODO : here we are calculating sum
  val resultn: Seq[Long] = for {
    line <- dataSeq // generateor
    totalSumByDeptt =
      line.split(","). // assignent
        withFilter(x => x.equals("SALESMAN")). // filter
        map(x => x.apply(2).toLong).sum
  } yield totalSumByDeptt


  val salesManSalary: Seq[Long] = dataSeq.map {
    record =>
      val tokenizedRecord: Seq[String] = record.split(",").toList
      val rec: Seq[String] = tokenizedRecord.filter(_.equals("SALESMAN"))
      rec.apply(2).toLong
  }

  val resultn1: Seq[Long] = for {
    line <- dataSeq // generateor
    record: List[String] = line.split(",").toList // assignment  // assignent
    if (record.apply(2).equals("SALESMAN"))
  } yield (record.apply(2).toLong)
  val sum: Long = resultn1.sum
  //


  val monthlyConsumptionEnergyAmount = Seq(437.8, 3339.5, 0.0, 0.0, 0.0, 0.0, 75.0, 99.0, 0.0, 20.0, 66.0)

  val monthNames: Array[String] = Array("Jan", "Feb", "Mar", "Apr", "May",
    "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

  def aggregateMonthlyConsumption(snapshots: Seq[(ZonedDateTime, Double)]): Seq[Double] = {
    val netResult: Seq[Double] = for {
      i <- 1 to 12 //generator
      snapShotView = snapshots.view
      monthWiseTotal: Double =
        snapShotView.
          withFilter { case (d, _) => d.getMonthValue == i }.
          map(t => t._2).sum
    } yield monthWiseTotal
    println(netResult)
    netResult
  }



  /*
TODO
     Initialization:
     Use Array.fill to create an array with 12 zeroes.
     This array represents the total for each month
     and avoids the need for repeatedly filtering and mapping the data.

 TODO
     Single Traversal:
     The function traverses the snapshots sequence once,
      using the foreach method to accumulate totals directly
      into the corresponding index of the monthlyTotals array.
      This avoids the inefficiency of repeatedly filtering the sequence for each month.

TODO
    Direct Index Access:
    By directly accessing the array index that corresponds
    to each month,
    the function efficiently aggregates totals
     without unnecessary transformations or intermediate collections.
TODO
    Performance:
    The revised approach is more efficient in terms of both time (O(n)
     where n is the number of snapshots) and space (using a fixed-size array),
     compared to the original approach that iteratively filters the list for each month.
 */


  import java.time.ZonedDateTime

  def aggregateMonthlyConsumptionModified(snapshots: Seq[(ZonedDateTime, Double)]): Seq[Double] = {
    // Initialize an array of doubles with 12 elements, all set to 0.0
    //This array represents the total for each month
    val monthlyTotals = Array.fill(12)(0.0)

    // Iterate over each snapshot once and accumulate the values directly into the corresponding month
    snapshots.foreach {
      case (date, amount) =>
        // Convert 1-based month to 0-based index for the array
        val monthIndex = date.getMonthValue - 1
        monthlyTotals(monthIndex) += amount
    }

    // Convert the mutable Array to an immutable Seq before returning
    monthlyTotals.toSeq
  }

  // Example Usage:
  val snapshots = Seq(
    (ZonedDateTime.parse("2023-01-15T10:15:30+01:00[Europe/Paris]"), 100.0),
    (ZonedDateTime.parse("2023-01-22T10:15:30+01:00[Europe/Paris]"), 150.0),
    (ZonedDateTime.parse("2023-02-01T10:15:30+01:00[Europe/Paris]"), 200.0)
  )

  val monthlyConsumptionAmount1 = aggregateMonthlyConsumption(snapshots)
  println(monthlyConsumptionAmount1)


  def aggregateMonthlyConsumption1(snapshots: Seq[(ZonedDateTime, Double)]): Seq[Double] = {
    val netResult: Seq[Double] = for {
      i <- 1 to 12
      snapShotView <- snapshots
      if snapShotView._1.getMonthValue == i
    } yield snapShotView._2
    println(netResult.sum)
    netResult
  }

  val forresult1 = for {
    (xs, i) <- monthlyConsumptionEnergyAmount.view.zipWithIndex
  } println(s"Energy use for ${monthNames.apply(i)}: ${"%.2f".format(xs)}")
  //yield monthNames(i) ->  "%.2f".format(xs)


  val forresult: SeqView[(String, String), Seq[_]] = for {
    (xs, i) <- monthlyConsumptionEnergyAmount.view.zipWithIndex
  } yield monthNames(i) -> "%.2f".format(xs)
  // println(s"Energy use for ${monthNames(i)}: ${"%.2f".format(xs)}")


}