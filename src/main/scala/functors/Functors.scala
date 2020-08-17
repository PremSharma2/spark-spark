package functors

object Functors  extends App {

  class Functor[T] (val value:T)  {

    def map[A,B] (rawFunction : A => B): Functor[A] => Functor[B]=
      (a:Functor[A]) => new Functor[B](rawFunction.apply(a.value))
  }

  def rawLengthOfString(str:String):Int = str.length
  val stringContainer : Functor[String] = new Functor[String]("Hello-scala")
  val transFormedLength: Functor[String] => Functor[Int] =stringContainer.map(rawLengthOfString _)
  val result: Functor[Int] = transFormedLength(stringContainer)

}
