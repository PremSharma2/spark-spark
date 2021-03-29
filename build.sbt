addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross  CrossVersion.full)
name := "Scala-Basics"

version := "0.1"

scalaVersion := "2.12.10"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.4",
  "org.apache.spark" %% "spark-sql" % "2.4.4" ,
  "org.typelevel" %% "cats-core" % "2.0.0",
  "io.estatico" %% "newtype" % "0.4.4"


)