package codingChallenge
import org.apache.spark.{SparkConf, sql}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import  org.apache.spark.sql.functions._

object SynechronTest extends App {

  val spark:SparkSession={
    System.setProperty("hadoop.home.dir", "C:/winutils");
    val sparkConfiguration = new SparkConf().setAppName("Simple Application").
      setMaster(("local[*]")).
      set("spark.executor.memory", "1g") // 4 workers
      .set("spark.executor.instances", "1")
      // 5 cores on each workers
      .set("spark.executor.cores", "5")

    SparkSession.builder
      .config(sparkConfiguration
      ).getOrCreate()
  }
  import spark.implicits._

  val df = Seq(("prem","maths",20),("amit","physics",50),("amit","maths",80)).toDF("name","subject","marks")

  val pivot_df = df.groupBy("name").pivot("subject").sum("marks")

  val cols = df.select("subject").distinct.map(r => r.getString(0)).collect.toSeq

  val final_df = pivot_df.withColumn("totalmarks",
    cols.map(c => when(col(c).isNull,lit(0)).
      otherwise(col(c))).reduce(_ + _).as("totalmarks"))

  final_df.show(false)

}
