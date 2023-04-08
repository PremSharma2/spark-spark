package adts

object AdtWithGenericType {

  sealed trait Sum[A, B]

  final case class Left[A, B](value: A) extends Sum[A, B]
  final case class Right[A, B](value: B) extends Sum[A, B]

  sealed trait Maybe[A]

  final case class Full[A](a: A) extends Maybe[A]
  final case object Empty extends Maybe[Nothing]
}
