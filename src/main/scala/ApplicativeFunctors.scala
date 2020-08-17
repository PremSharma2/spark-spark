import functors.Functors.Functor

object ApplicativeFunctors  extends App {

  class Applicative[T](val value:T){

    def apply[A,B] (b: Applicative[ A => B]) : Applicative[A] => Applicative[B]=
      (a:Applicative[A]) => new Applicative[B](b.value.apply(a.value))
  }


  def rawLengthOfString(str:String):Int = str.length
  val stringContainer : Applicative[String] = new Applicative[String]("Hello-scala")
  val lengthOF : Applicative[String => Int] = new Applicative(rawLengthOfString _)
  def transformedLength = stringContainer.apply(lengthOF)
  val result: Applicative[Int] = transformedLength.apply(stringContainer)
  println(result.value)
}
