package monads

object Monads extends App {
// Our own Try monad
  trait Attempt[+A]{
    def flatMap[B](fx: A=> Attempt[B]): Attempt[B]
  }

  object Attempt{
    def apply[A](a: => A): Attempt[A]=
      try{
        Success(a)
      }catch {
        case e: Throwable => Failure(e)
      }
  }
  case class Success[+A](value: A) extends Attempt[A]{
    override def flatMap[B](fx: A => Attempt[B]): Attempt[B] =
      try{
        fx.apply(value)
      }catch {
        case e: Throwable => Failure(e)
      }
  }

  case class Failure(ex: Throwable) extends Attempt[Nothing]{
    override def flatMap[B](fx: Nothing => Attempt[B]): Attempt[B] = this
  }
  /*
  Lets Proof all the Monads Laws with our own monads
  1Law: LeftIdentity:

  Attempt(x).flatMap(f) =f(x) // it only make sense for Success case
  Success(x).flatMap(f) = fx.apply(x)  // acc to implementation it is fx.apply(x)
  Hence proved
----------------------------------------------------------------------------------
  2nd Law
  attempt.flatMap(unit) = attempt
  Success(x).flatMap(x => Attempt(x)) = Attempt(x)
  but this expression Success(x).flatMap(x => Attempt(x)) is equivalent to
  fx.apply(x) and that will return Attempt(x)
  Hence Proved

  --------------------------------------------------------------------------------
  3rd Law

  attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
  Lets proof it

  Fail(e).flatMap(f).flatMap(g) =Fail(e)
  now lets evaluate on right hand side

  attempt.flatMap(x => f(x).flatMap(g))
  Fail(e).flatMap(x => f(x).flatMap(g))= Fail(e)

  Fail satisfies the associativity law
  



   */

}
