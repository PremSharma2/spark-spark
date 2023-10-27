package ImplicitsAndTypeClasses
// this is the absolutely correct design for type class
trait TypeClassTemplate [T]{

  def action(value :T): String
}

object TypeClassTemplate{
  def apply[T](value :T)(implicit typeClassInstance:TypeClassTemplate[T]) = typeClassInstance.action(value)
}