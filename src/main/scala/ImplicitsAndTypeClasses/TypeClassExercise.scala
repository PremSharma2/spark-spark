package ImplicitsAndTypeClasses
//We want to build one equalizer api whcich checks the equality of objects of same type
// this can be also a good use case  of type class pattern
object TypeClassExercise  extends App {

  trait Equal[-T] {
    def isEqual(a: T, b: T): Boolean
  }


  /// Companion object for Equal trait, providing a generic apply method
  /**
  The companion object's apply method is designed to be a convenient
  way to use the type class to compare two values directly.
  When you call Equal(a, b), you're asking
  "Are a and b equal according to some definition of equality?",
  to which Scala answers with a Boolean true or false.
 If you return the comparator instance itself from the apply method,
  you would then need to call its isEqual method separately,
  defeating the purpose of the syntactic sugar that the apply method is supposed to provide.

   */
  object Equal {
    def apply[T](a: T, b: T)(implicit comparator: Equal[T]): Boolean =
      comparator.isEqual(a, b)
  }


  // Type class instance for User, comparing by name
  implicit object NameEqual extends Equal[User] {
    override def isEqual(a: User, b: User): Boolean = a.name == b.name
  }

  // Type class instance for User, comparing by both name and email
  object FullEqual extends Equal[User] {
    override def isEqual(a: User, b: User): Boolean =
      a.name == b.name && a.email == b.email
  }


  case class User(name:String, age:Int , email:String)
  val user=User("Prem", 34, "prem.kaushik@outlook.com")
  val anotherUser= User("Prem", 34, "prem.kaushik@outlook.com")

  println(Equal(user,anotherUser))
  //TODO:  this is called adhoc polymorphism, because we can call same  Equals companion object for diffrent types
  }
