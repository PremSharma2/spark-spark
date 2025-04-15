package ImplicitsAndTypeClasses

import scala.language.implicitConversions

// ---------------------------
// 1. The Type Class
// ---------------------------
trait TypeClassTemplate[T] {
  def action(value: T): String
}

// ---------------------------
// 2. Companion Object with Apply Method
// ---------------------------
object TypeClassTemplate {
  def apply[T](value: T)(implicit instance: TypeClassTemplate[T]): String =
    instance.action(value)
}

// ---------------------------
// 3. Ops Class + Implicit def (Pimp My Library)
// ---------------------------
class TypeClassOps[T](val value: T) extends AnyVal {
  def act(implicit tc: TypeClassTemplate[T]): String = tc.action(value)
}

object TypeClassSyntax {
  implicit def toTypeClassOps[T](value: T): TypeClassOps[T] = new TypeClassOps(value)
}

// ---------------------------
// 4. Example Model + Instance
// ---------------------------
case class Person(name: String, age: Int)

object TypeClassInstances {
  implicit val personInstance: TypeClassTemplate[Person] = new TypeClassTemplate[Person] {
    def action(value: Person): String = s"${value.name} is ${value.age} years old"
  }

  // Example for primitive type
  implicit val intInstance: TypeClassTemplate[Int] = new TypeClassTemplate[Int] {
    def action(value: Int): String = s"Number is $value"
  }
}

// ---------------------------
// 5. Usage
// ---------------------------
object TypeClassTemplateDemo extends App {
  import TypeClassInstances._
  import TypeClassSyntax._

  val p = Person("Prem", 34)
  println(TypeClassTemplate(p))     // Using companion apply
  println(p.act)                    // Using pimped syntax

  println(42.act)                   // Works for Int too!
}
