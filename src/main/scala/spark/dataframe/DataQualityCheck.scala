package spark.dataframe

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Column, DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object DataQualityCheck extends App {

  System.setProperty("hadoop.home.dir", "C:/winutils");
  //val logFile = "D:/Spark_VM/emp.txt" // Should be some file on your system
  val spark = new SparkConf().setAppName("Simple Application").
    setMaster(("local[*]")).
    set("spark.executor.memory", "1g") // 4 workers
    .set("spark.executor.instances", "1")
    // 5 cores on each workers
    .set("spark.executor.cores", "5");
  val sparksession: SparkSession = SparkSession.builder.master("local").config(spark).getOrCreate()
  import sparksession.implicits._
  import sparksession.sqlContext.implicits._

  val data = List(("James ","","Smith","36636","M",60000),
    ("Michael ","Rose",null ,"40288","M",70000),
    ("Robert ",null,"Williams","42114","",400000),
    ("Maria ","Anne","Jones","39192","F",500000),
    ("Jen","Mary","Brown","39192","F",0))

  val cols = Seq("first_name","middle_name","last_name","dob","gender","salary")
  val df2= sparksession.createDataFrame(data).toDF(cols:_*)
  df2.select(col("*"), when(col("gender") === "M","Male")
    .when(col("gender") === "F","Female")
    .otherwise("Unknown").alias("new_gender")).show(false)
  val columns=df2.columns
  val columnstoBeSelected: Array[Column] =columns map (column=> count(when(col(column).isNull,column)).as(column))
  df2.select(columnstoBeSelected:_*)
  df2.select(columns map (column => count(when(col(column).isNull,column)).as(column)):_*) show false
 // df2.select(columns :_*)
 val colNames = columns.map(name => col(name))
  val df = df2.select(colNames:_*)
  /*
  SELECT
      COUNT(CASE WHEN Col1 = 'A' THEN 1 END) AS CountWithoutElse,
      COUNT(CASE WHEN Col1 = 'A' THEN 1 ELSE NULL END) AS CountWithElseNull,
      COUNT(CASE WHEN Col1 = 'A' THEN 1 ELSE 0 END) AS CountWithElseZero
  FROM #CountMe;
  After seeing the sql version ,It is clear from here is that we need to select all records and,
   then loop over the all column an check the column value whether it is Null or not
   i.e for each column this operation will be operated
   column=> count(when(col(column).isNull,column)).as(column))
   val
   cols = Seq("first_name","middle_name","last_name","dob","gender","salary")

   */
  /*
  val df = Seq(
    (8, "bat"),
    (64, "mouse"),
    (27, "horse"),
    (None, None)
  ).toDF("number", "word")
*/
 // df.select( df.columns map (column=> count( when( col(column).isNull, column)).as(column)):_*) show false

/*
import org.apache.spark.sql.functions.{col, count, when}

df.select(df.columns.map(c => (count(c) / count("*")).alias(c)): _*)
with -900:

df.select(df.columns.map(
  c => (count(when(col(c) === -900, col(c))) / count("*")).alias(c)): _*)
 */

}
