package typeClasses

import typeClasses.TypeClasses.User

object TypeClassExercise  extends App {
  trait Equal[T]{
    def apply(a:T,b:T)
  }
  // This is Type Class instance
object NameEquality extends Equal[User] {
  override def apply(a: User, b: User): Unit = a.name==b.name
}
  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Unit = a.name == b.name && a.email==b.email
  }
  }
