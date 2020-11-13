name := "Scala-Basics"

version := "0.1"

scalaVersion := "2.11.0"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.4",
  "org.apache.spark" %% "spark-sql" % "2.4.4" ,
  "org.typelevel" %% "cats-core" % "2.0.0"


)