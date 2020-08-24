package typeClasses

object TypeClasses  extends App {

  trait HtmlWritable{
    def toHtml:String
  }
case class User(name:String, age:Int , email:String) extends HtmlWritable {
  override def toHtml: String = s"<div> $this.name {$this.age yo} <a href = $this.email /> </div>"
}
  val user=User("Prem", 34, "prem.kaushik@outlook.com")
  println(user.toHtml)
  /*
  Disadvantages of this design
  1: This only works for the Types we write i.e it is per implementation specific
  2: This one implementation out of quite a number
   */

  // Design number 2
  /*
  In this design we will pattern match the Instances
  so that it can be reused for other Types of instances
  DisAdvantages:
  We lost the type safety
  We need to modify the code everytime we add new match
   */

  object HtmlSerializablePM{
    def serializableToHtml(value :Any) = value match{
      case User(n,a,e) => _
      case _ => _
    }
  }
  // Better DEsign than last two
  /*
  Advantages with this approach
  We can define teh serializers for the other types
  We can define Multiple Serilazers for the same type as well
  Here in scala this trait HtmlSerliazer[T] is alled Type Class
  because it defines the certain operations whcih can be applied
  to Type passed to it
   */
  trait HtmlSerliazer[T]{
    def serialize(value : T):String
  }
  object UserSerializer extends HtmlSerliazer[User] {
    override def serialize(user: User): String =
        s"<div> $user.name {$user.age yo} <a href = $user.email /> </div>"
  }
  import java.util.Date
  object DateSerializer extends HtmlSerliazer[Date] {
    override def serialize(value: Date): String =  s"<div>${value.toString}</div>"
  }
  // these are those users who are not logged in
  object PartialUserSerializer extends HtmlSerliazer[User] {
    override def serialize(user: User): String =
      s"<div> $user.name /> </div>"
  }
}
