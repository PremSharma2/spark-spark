addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross  CrossVersion.full)
name := "Scala-Basics"

version := "0.1"

scalaVersion := "2.12.10"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.4",
  "org.apache.spark" %% "spark-sql" % "2.4.4" ,
  "org.typelevel" %% "cats-core" % "2.0.0",
  "io.estatico" %% "newtype" % "0.4.4",
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15",
  "org.scalatestplus" %% "scalacheck-1-17" % "3.2.15.0",
  "org.scalatest" %% "scalatest" % "3.2.9",
  "org.typelevel" %% "cats-effect" % "3.2.0",
  "co.fs2" %% "fs2-core" % "3.2.4"


)