package ImplicitsAndTypeClasses
// old traditional way of designing serialising API
object TypeClasses  extends App {

// Design to Serialize domain objects to render HTML
  trait HtmlSerializable{
    def serializeToHtml:String
  }


  // This is the POJO or BO  we want to map that HTML object
  // we extended this pojo to HtmlSerializable just to mark it it is serializable
  // this approach is tightly coupled
case class User(name:String, age:Int , email:String) extends HtmlSerializable {
    // and here we serialized this
  override def serializeToHtml: String = s"<div> $name {$age} <a href = $email /> </div>"
}
  val user=User("Prem", 34, "prem.kaushik@outlook.com")
  println(user.serializeToHtml)


  /*
  Disadvantages of this design
  1: This only works for the Types we write i.e., it is per implementation specific
  2: This one implementation out of quite a number
   */

  // Design number 2
  /*
  Here we are trying to make this pattern general purpose for every type
  we mae on companion object
  In this design we will pattern match the Instances
  so that it can be reused for other Types of instances
  DisAdvantages:
  We lost the type safety
  No use of Generic i.e this impl is not generic
  We need to modify the code every time we add new match
   */

  object HtmlSerializable{
    def serializableToHtml(value :Any): Unit = value match{
      case User(n,a,e) =>
      case _ =>
    }
  }


  /**
 TODO
    Better Design than last two
  Advantages with this approach
  We can define the  serializers for the generic types
  We can define Multiple Serializers for the same type as well
  Here in scala this trait HtmlSerializer[T] is called Type Class
  because it defines the certain operations which can be applied
  to Type value  passed to it i.e T here
  And these all implementations of this trait are type class instances
  that's why we make then singleton object
  This is called type class pattern
   */

  trait HtmlSerializer[T]{
    def serialize(value : T):String
  }


  //type class instances

  object UserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String =
        s"<div> $user.name {$user.age yo} <a href = $user.email /> </div>"
  }


  import java.util.Date
  object DateSerializer extends HtmlSerializer[Date] {
    override def serialize(value: Date): String =  s"<div>${value.toString}</div>"
  }


  // these are those users who are not logged in
  object PartialUserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String =
      s"<div> $user.name /> </div>"
  }
  println(PartialUserSerializer.serialize(user))
}
