package typeClasses

object Implicitly extends App {

  //Lets talk about implicitly
  case class Permissions(mask:String)
  implicit val defaultPermission=Permissions("0744")
  // in some other part of code we want to see the implicit value of the defaultPermission

  val standardPermission: Permissions = implicitly[Permissions]

}
