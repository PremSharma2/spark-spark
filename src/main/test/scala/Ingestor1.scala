import com.convex.test.SparkSessionUtil

object Ingestor1  extends App with SparkSessionUtil {

  def parse(input: String) = {
    if(input.contains(",")) {
      val arr = input.split(",")
      (arr(0).trim.toInt, arr(1).trim.toInt)
    } else if (input.contains("\t")) {
      val arr = input.split("\t")
      (arr(0).trim.toInt, arr(1).trim.toInt)
    } else {
      (-1,-1)
    }
  }

  def getOddsOccurrence(arr:Array[Int]): List[Int] = {
    val unpaired = scala.collection.mutable.Set[Int]()
    for(ele <- arr) {
      if(unpaired.contains(ele)) {
        unpaired.remove(ele)
      } else unpaired.add(ele)
    }
    unpaired.toList
  }

  def parseInput(inputPath:String) : Unit={
    spark.sparkContext.setLogLevel("ERROR")
    import spark.implicits._
    val filesRDD = spark.sparkContext.wholeTextFiles(inputPath)

    val linesRDD = filesRDD.flatMap(_._2.split("\n").drop(1))

    val parsedRDD = linesRDD.map(parse).filter(rec => rec._1 != -1 && rec._2 != -1)

    val groupedRDD = parsedRDD.groupByKey()
    groupedRDD.foreach(println)

    val oddOccurRDD = groupedRDD.map(t => (t._1, getOddsOccurrence(t._2.toArray)))

    val finalRDD = oddOccurRDD.filter(t => t._2.size == 1)
      .map(t => (t._1, t._2.head))

    val df = finalRDD.toDF("key", "value")
    df.printSchema()
    df.show()

    df.write.mode("overwrite")
      .option("header", true)
      .option("delimiter", "\t")
      .csv(args(1))
  }

  parseInput(args(0))
}
