package monads

object Monads extends App {
// Our own Try monad
  // here Attempt is a Functor or container here because it contains the Success and Failue
  // Success and Failure are also functors or containers bcz they contain a value here
  //Here Attempt is a contract or interface
  trait Attempt[+A]{
    def flatMap[B](fx: A=> Attempt[B]): Attempt[B]
  }

  object Attempt{
    def apply[A](a: => A): Attempt[A]=
      try{
        Success.apply(a)
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

  now lets take a look for Success

 Success(x).flatMap(f).flatMap(g)
 now we can write this as beacuse Success(x).flatMap(f)= f(x) as we see the Succses flatMap impl
 so result of above operation is f(x).flatMap(g) i.e
Success(x).flatMap(f).flatMap(g)= f(x).flatMap(g)

now lets evaluate on the Right hand side of equation and will
 try to proof both calculates same result

Success(x).flatMap(x => f(x).flatmap(g))
but as we know already we can write this is as
x => f(x).flatmap(g)= f(v).flatMap(g)
so Success(x).flatMap(x => f(x).flatmap(g))=  f(v).flatMap(g)
if f(x) this guy does not throw an exception
hence proved because both equations LHS and RHS gives the same result


   */
  // lets test our monad

  val attempt: Attempt[Nothing] = Attempt{
    throw new RuntimeException("My own monad, yes!")
  }
println(attempt)

}
