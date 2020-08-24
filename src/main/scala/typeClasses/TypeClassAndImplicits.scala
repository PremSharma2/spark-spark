package typeClasses
//Exercise number 2
// Types classes along with the implicits
object TypeClassAndImplicits  extends App {
// type class
  trait HtmlSerializer[T]{
    def serialize(value : T):String
  }
  // This is utility to use smartly Type Class
  // via this utility i.e Type class instances will be passed to utility to this Companion Object
  // We will apply the operation on Type we passed via this utility
  object HtmlSerializer{
    // it take type class instance as argument
    def serialize[T](value : T)(implicit serializer:HtmlSerializer[T]):String=
      serializer.serialize(value)
  }

 implicit object IntSerializer extends HtmlSerializer[Int]{
    override def serialize(value: Int): String =s"<div style : color=blue > $value</div>"

  }
  println(HtmlSerializer.serialize(42)(IntSerializer))
  println(HtmlSerializer.serialize(42))
}
