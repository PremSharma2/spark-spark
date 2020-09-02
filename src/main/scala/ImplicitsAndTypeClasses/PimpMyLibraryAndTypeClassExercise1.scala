package ImplicitsAndTypeClasses

object PimpMyLibraryAndTypeClassExercise1 extends App {
// implementing full fledged type class pattern with type enrichment or pimping
  // i.e with type conversion also
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
  implicit class TypeSafeEqual[T](value:T){
    def ===(other:T)(implicit equalizer:Equal[T]) = equalizer.apply(value,other)
    def =!=(other:T)(implicit equalizer:Equal[T]) = !equalizer.apply(value,other)
  }
  case class User(name:String, age:Int , email:String)
  val user=User("Prem", 34, "prem.kaushik@outlook.com")
  val anotherUser= User("Prem", 34, "prem.kaushik@outlook.com")
  println(user.===(anotherUser))
  println(user === anotherUser)
  /*
  Here are the steps which compiler will take to do implicit type conversion
  user.===(anotherUSer)
  compiler will look for the implicit value class which takes generic type T
  and has a method === it will find it then it will rewrite the logic
  new TypeSafeEqual[User](user).===(anotherUser)(NameEquality)
  This is type safe because compiler will not allow to compile this below expression
  println(user === 43)
  it will throw an error that both the types must be same
   */

}
