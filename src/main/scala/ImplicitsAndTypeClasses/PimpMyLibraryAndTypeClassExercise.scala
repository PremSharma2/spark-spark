package ImplicitsAndTypeClasses

import ImplicitsAndTypeClasses.TypeClassAndImplicits.{HtmlSerializer, PartialUserSerializer, User}



object PimpMyLibraryAndTypeClassExercise  extends App {
// exercise of Type class pattern and PimpMylibrary mixuture
  // this is an implicit value class for implicit conversion
  implicit class HTMLEnrichment[T](value:T){
  def toHtml(implicit serializer:HtmlSerializer[T]) :String = serializer.serialize(value)
}
  case class User(name:String, age:Int , email:String)
  implicit object UserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String =
      s"<div> $user.name {$user.age yo} <a href = $user.email /> </div>"
  }
  implicit object IntSerializer extends HtmlSerializer[Int] {
    override def serialize(value: Int): String = s"<div style : color=blue > $value</div>"
  }
  val user=User("Prem", 34, "prem.kaushik@outlook.com")
  val anotherUser= User("Prem", 34, "prem.kaushik@outlook.com")
println(user.toHtml(UserSerializer)) // compiler rewrite this as new HTMLEnrichment(user).toHtml(UserSerializer)
  println(user.toHtml)
  // This proves that this approach can extend the functionalty to the new types though
  //
  println(2.toHtml)
  // we also can choose the implementation
  object PartialUserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String =
      s"<div> $user.name /> </div>"
  }
  println(user.toHtml(PartialUserSerializer))
  /*
  Hence type class pattern has three main components to Enhancing a type with type class
  1: type class itself   (trait HtmlSerializer[T])
  2: type class instances (some of which are implicits objects)
  3: implicit conversion with implicit value classes  (implicit class HTMLEnrichment[T](value:T))
  Which will convert the your Data object to Serialized object implicitly
   */

  /*
  Lets talk about context bounds
   */
  def htmlBoilerPlate[T](content:T)(implicit serializer:HtmlSerializer[T]):String={
    s"<html><body> {${content.toHtml(serializer)}} </body> </html>"
  }
  // we also can write teh above code
  //T: HtmlSerializer this syntax is telling compiler that pls inject one more argument list
  // which take takes (implicit serializer:HtmlSerializer[T]) like this
  // so compiler will change this signature to
  //def htmlSyntacticSugar[T](content:T)(implicit serializer:HtmlSerializer[T])
  def htmlSyntacticSugar [T: HtmlSerializer] (content:T):String = {
    val serializer= implicitly[HtmlSerializer[T]]
    s"<html><body> {${content.toHtml(serializer)}} </body> </html>"
  }
}
