package typeClasses

//Exercise number 2
// Types classes along with the implicits
object TypeClassAndImplicits  extends App {
// type class
  trait HtmlSerializer[T]{
    def serialize(value : T):String
  }
  // This is utility to use smartly Type Class via this companion object
  // as a good design we always access type classes or traits who top most member of faimly
  // via companion object
  // via this utility i.e Type class instances will be passed to utility to this Companion Object
  // We will apply the operation on Type we passed via this utility
  object HtmlSerializer{
    // it take type class instance as argument and the type T
    def serialize[T](value : T)(implicit serializer:HtmlSerializer[T]):String=
      serializer.serialize(value)
    // for more better design
    def apply [T](implicit serializer: HtmlSerializer[T]) = serializer
  }

 implicit object IntSerializer extends HtmlSerializer[Int]{
    override def serialize(value: Int): String =s"<div style : color=blue > $value</div>"

  }

  implicit object UserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String =
      s"<div> $user.name {$user.age yo} <a href = $user.email /> </div>"
  }
  case class User(name:String, age:Int , email:String)
  println(HtmlSerializer.serialize(42)(IntSerializer))
  println(HtmlSerializer.serialize(42))
  val user=User("Prem", 34, "prem.kaushik@outlook.com")
println(HtmlSerializer.apply[User].serialize(user))
}
