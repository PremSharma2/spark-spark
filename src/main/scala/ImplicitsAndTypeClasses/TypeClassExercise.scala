package ImplicitsAndTypeClasses

import scala.language.implicitConversions

object TypeClassExercise extends App {

  // --------------------------
  // 1. The Type Class definition
  // --------------------------
  trait Equal[-T] {
    def isEqual(a: T, b: T): Boolean
  }

  // --------------------------
  // 2. Type Class Companion with Apply
  // --------------------------
  object Equal {
    def apply[T](a: T, b: T)(implicit comparator: Equal[T]): Boolean =
      comparator.isEqual(a, b)
  }

  // --------------------------
  // 3.Data  Model
  // --------------------------
  case class User(name: String, age: Int, email: String)

  // --------------------------
  // 4. Type Class Instances
  // --------------------------
  implicit object NameEqual extends Equal[User] {
    def isEqual(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEqual extends Equal[User] {
    def isEqual(a: User, b: User): Boolean =
      a.name == b.name && a.email == b.email
  }

  // --------------------------
  // 5. Syntax Enrichment via implicit def + Ops class
  // --------------------------
  class EqualOps[T](val a: T) extends AnyVal {
    def ===(b: T)(implicit eq: Equal[T]): Boolean = eq.isEqual(a, b)
    def =/=(b: T)(implicit eq: Equal[T]): Boolean = !eq.isEqual(a, b)
  }

  object EqualSyntax {
    implicit def toEqualOps[T](a: T): EqualOps[T] = new EqualOps(a)
  }

  // --------------------------
  // 6. Usage
  // --------------------------
  import EqualSyntax._

  val user1 = User("Prem", 34, "prem.kaushik@outlook.com")
  val user2 = User("Prem", 34, "prem.kaushik@outlook.com")

  println(user1 === user2) // true — uses NameEqual (by name)
  println(user1 =/= user2) // false

  // Ad-hoc polymorphism — works for any type T with an Equal[T] instance
  println(Equal(user1, user2)) // also works
}
