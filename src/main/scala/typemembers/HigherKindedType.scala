package typemembers

import scala.language.higherKinds

object HigherKindedType  extends App {


  trait AHigherKindedType[F[_], T] // This is called Higher Kinded Type

  // Lets Explain Higher Kinded Type By Monad  example
/*
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
// now interesting thing here is that we can apply this For Comprehension to any Monad



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

 */
  // Imp Question When we want to design an API where All types of monads Will be there
  // then this can be only Possible With higherKinded Types
  //F[_] this here Represents that it will accept any Kind of Monad so all kinds of monads will be present
  // i.e List,future,Option etc etc...
  // Here [Monad[_],A] here it represent that this SuperMonad will take input as Monad and A is the type of Monad
  trait Monad[F[_],A]{ // it is also a Type class over HigherKinded Type
    def flatMap[B](fx: A => F[B]):F[B]
    def map[B](fx: A => B):F[B]
  }

  // Its like abstraction Of List of integers
  //This API is to Handle ListMonad
  //This is basically Wrapper over List Monad or any kind of Monad
 implicit class ListMonad1[A](list:List[A]) extends Monad[List,A] {
    override def flatMap[B](fx: A => List[B]): List[B] = list.flatMap(fx)

    override def map[B](fx: A => B): List[B] = list.map(fx)
  }


// Another Type Of monad i.e Option Monad will reuse the same same Wrapper
implicit  class OptionMonad1[A](option:Option[A]) extends Monad[Option,A] {
    override def flatMap[B](fx: A => Option[B]): Option[B] = option.flatMap(fx)

    override def map[B](fx: A => B): Option[B] = option.map(fx)
  }


  val monadList= new ListMonad1(List(1,2,3))
  val result: Seq[Int] =monadList.flatMap(x => List(x,x+1))
  val result1: Seq[Int] =monadList.map( _+1 )

 // monadList.flatMap(List(_+1))
  // now as we are building a common API for all types of monads
// here we will add type for Monad also
  // We can read like this as well as
  // ma is Container of type  Monad which is of type A
  // To handle Monad of different kind we added type for Monad F[_]
  // output of this method is F[(A,B)] which is ListMonad of (Int,String) i.e tuple
  def multiply[F[_],A,B](implicit ma:Monad[F,A], mb:Monad[F,B]):F[(A,B)] = {
    for{
      a <- ma
      b <- mb
    } yield (a,b)



    /*
 todo
    compiler will transform this For comprehension into map and flatmap
    ma.flatMap(a=> mb.map(b => (a,b)))
    so when we call ma.flatMap(fx) where fx= a=> mb.map(b => (a,b))
     inside flatMap we will call a.flatMap which will give a the original value
     */
  }


  // Lets Test This For ListMonad container We created to Handle ListMonad
 println(multiply(monadList,new ListMonad1[String](List("a","b","c")) ))
  println(multiply(new OptionMonad1[Int](Some(1)),new OptionMonad1[String](Some("Scala"))))
  println(multiply(List(1,2),List("a","b","c")))
  println(multiply(Some(1),Some("Scala")))
}
