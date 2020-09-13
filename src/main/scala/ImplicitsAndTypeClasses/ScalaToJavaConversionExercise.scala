package ImplicitsAndTypeClasses
import java.{util => javaCollection}
object ScalaToJavaConversionExercise  extends App {
// exercise to convert java optional ti scala option
   class ToScala[T](value : => T ){
  def asScala:T = value

}
  // For implicit conversion
implicit def asScalaOptional [T](o: javaCollection.Optional[T]) : ToScala[Option[T]]={
new ToScala[Option[T]](
  if (o.isPresent) Some(o.get()) else None
)
}
  val javaOptional :javaCollection.Optional[Int]= javaCollection.Optional.of(2)
  val scalaOption: Option[Int] =  javaOptional.asScala
}
