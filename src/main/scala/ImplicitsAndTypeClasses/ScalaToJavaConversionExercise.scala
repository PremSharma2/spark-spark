package ImplicitsAndTypeClasses

import java.{util => javaCollection}

object ScalaToJavaConversionExercise extends App {


  // exercise to convert java optional to scala option
  class ToScala[T](value: => T) {
    def asScala: T = value

  }

  // For implicit conversion
  trait ScalaDecorator {
    implicit def asScalaOptional[T](o: javaCollection.Optional[T]): ToScala[Option[T]] = {
      new ToScala[Option[T]](
        if (o.isPresent) Some(o.get()) else None
      )
    }
  }

  object ScalaConverters extends ScalaDecorator

  import ImplicitsAndTypeClasses.ScalaToJavaConversionExercise.ScalaConverters._

  val javaOptional: javaCollection.Optional[Int] = javaCollection.Optional.of(2)
  val scalaOption: Option[Int] = javaOptional.asScala
}
