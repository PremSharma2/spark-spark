package ImplicitsAndTypeClasses

import ImplicitsAndTypeClasses.TypeClasses.User

object TypeClassExercise  extends App {
  trait Equal[T]{
    def apply(a:T,b:T):Boolean
  }
  //Companion object for equal
  object Equal{
    // this takes an type class instance
    // we ca read like this use this equalizer on these values of Type T
    def apply[T](a:T,b:T)(implicit equalizer:Equal[T]) :Boolean= equalizer.apply(a,b)
  }
  // This is Type Class instance
implicit object NameEquality extends Equal[User] {
  override def apply(a: User, b: User): Boolean = a.name==b.name
}
    object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email==b.email
  }
  case class User(name:String, age:Int , email:String)
  val user=User("Prem", 34, "prem.kaushik@outlook.com")
  val anotherUser= User("Prem", 34, "prem.kaushik@outlook.com")
  println(Equal.apply(user,anotherUser))
  // this is called adhoc polymorphism, because we can call same  Equals companion object for diffrent types
  }
