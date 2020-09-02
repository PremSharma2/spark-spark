package ImplicitsAndTypeClasses
// this is the absolutely correct design for type class
trait TypeClassTemplate [T]{

  def action(value :T): String
}

object TypeClassTemplate{
  def action[T](implicit typeClassInstance:TypeClassTemplate[T]) = typeClassInstance
}