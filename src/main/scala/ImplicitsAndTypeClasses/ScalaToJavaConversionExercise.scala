package ImplicitsAndTypeClasses

import java.{util => ju}
import scala.language.implicitConversions
//pimp the library pattern using implicit def + Ops class
//implicit def + Ops class	Separates concerns: conversion logic vs method definition
object ScalaToJavaConversionExercise extends App {

  // Ops class with the actual method
  //The Ops class that holds the extension method
  class JavaOptionalOps[T](val optional: ju.Optional[T]) extends AnyVal {
    def asScala: Option[T] =
      if (optional.isPresent) Some(optional.get)
      else None
  }

  // Syntax object with the implicit conversion
  //he implicit def that wraps ju.Optional with the Ops class
  object JavaOptionalSyntax {
    implicit def toJavaOptionalOps[T](optional: ju.Optional[T]): JavaOptionalOps[T] =
      new JavaOptionalOps(optional)
  }

  // Bring the implicit conversion into scope
  import JavaOptionalSyntax._

  // Test
  val javaOptional: ju.Optional[Int] = ju.Optional.of(42)
  val scalaOption: Option[Int] = javaOptional.asScala

  println(scalaOption) // Output: Some(42)
}
