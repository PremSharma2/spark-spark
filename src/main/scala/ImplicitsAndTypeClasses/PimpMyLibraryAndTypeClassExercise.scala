package ImplicitsAndTypeClasses


object PimpMyLibraryAndTypeClassExercise  extends App {
// exercise of Type class pattern and Pimp-My-library mixture
  // this is an implicit value class for implicit conversion
// it will covert Buisness DO to HTML
  // serializable form
  //implicit conversion
  // so that we can write USer.toHtml
  //Type class
trait HtmlSerializer[T] {
  def serialize(value: T): String
}

  // Implicit class with Type class instance this will be used to pimp the library
  implicit class HTMLEnrichment[T](value: T) {
    def toHtml(implicit serializer: HtmlSerializer[T]): String = serializer.serialize(value)
  }

  //model
  case class User(name: String, age: Int, email: String)

  object User {
    implicit val userHtmlSerializer: HtmlSerializer[User] = new HtmlSerializer[User] {
      override def serialize(user: User): String = s"""<div>${user.name} (${user.age} yo) <a href="${user.email}"></a></div>"""
    }
  }

  object IntHtml {
    implicit val intHtmlSerializer: HtmlSerializer[Int] = new HtmlSerializer[Int] {
      override def serialize(value: Int): String = s"""<div style="color:blue">$value</div>"""
    }
  }


val user = User("Prem", 30, "prem@gmail.com")
import User._
  println(user.toHtml)


  /*
  Hence type class pattern has three main components to Enhancing a type with type class
  1: type class itself   (trait HtmlSerializer[T])
  2: type class instances (some of which are implicits objects)
  3: implicit conversion with implicit value classes
  (implicit class HTMLEnrichment[T](value:T))
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
