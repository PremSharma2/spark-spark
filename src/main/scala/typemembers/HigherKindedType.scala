package typemembers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
object HigherKindedType  extends App {

  trait AHigherKindedType[F[_]] // This is called Higher Kinded Type

  // Lets Explain Higher Kinded Type By Monad  example

  trait ListMonad[T]{
    // ETW pattern
    def flatMap[B](fx: T => B): ListMonad[B]
  }


  trait OptionMonad[T]{
    // ETW pattern
    def flatMap[B](fx: T => B):OptionMonad[B]
  }
  trait FutureMonad[T]{
    // ETW pattern
    def flatMap[B](fx: T => B): FutureMonad[B]
  }
  def multiply[A,B](listA:List[A],listB:List[B]): List[(A,B)] = {
     for{
      a <- listA
      b <- listB
    } yield (a,b)

  }
// now intresting thing here is that we can apply this For Comprehension to any Monad



  def multiply[A,B](optionA:Option[A],optionB:Option[B]): Option[(A,B)] = {
    for{
      a <- optionA
      b <- optionB
    } yield (a,b)

  }

  def multiply[A,B](futureA:Future[A],futureB:Future[B]): Future[(A,B)] = {
    for{
      a <- futureA
      b <- futureB
    } yield (a,b)

  }
  // Imp Question When we want to design an API where All types of monads Will be there
  // then this can be only Possible With higherKinded Types
  //F[_] this here Represents that it will accept any Kind of Monad so all kinds of monads will be present
  // i.e List,future,Option etc etc...
  trait SuperMonad[F[_],A]{
    def flatMap[B](fx: A => F[B]):F[B]
  }
  // Its like abstraction Of List of integers
  class MonadList extends SuperMonad[List,Int] {
    override def flatMap[B](fx: Int => List[B]): List[B] = ???
  }
  val monadList= new MonadList
  monadList.flatMap(x => List(x,x+1))
 // monadList.flatMap(List(_+1))
}
