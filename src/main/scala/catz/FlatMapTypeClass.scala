package catz

import cats.{Applicative, Apply}

object FlatMapTypeClass {
//TODO : FlatMap type class
  // TODO : Note : -> Apply extends Semigroupal and Functors
trait MyFlatMap[M[_]] extends Apply[M]{
  def flatMap[A,B](fa:M[A])(f: A=> M[B]):M[B]
  //TODO : implementation of ap in-terms of map and FlatMap
  // Here we have implemented in-terms if flat map and Flatmap
  // bcz we have both of these available, where as in Semigroupal
  // we did not have the map and flatMap
  def ap [A,B] (functionWrapper :M[A => B])(wa:M[A]): M[B] =
    flatMap(wa)(a => map(functionWrapper)(fx=> fx(a)))
  //         |  |        /                 \     \/
  //         |  |    M[A=>B]                A=>B  B
  //         |  |    \_____                ____/
  //       M[A] A =>            M[B]
}
  // TODO : structure of Monad looks like that and this is what cats API follows
  trait MyMonad[M[_]] extends Applicative[M] with MyFlatMap[M]{
   // def pure[A](value :A):M[A] pure comes from Applicatives lets extend Applicatives
    //ETW pattern
    // TODO : this flatMap method is not fundamental to Monad
    // TODO : but it is fundmental to type class FlatMap so we have to remove it form here
    // and lets Monad extend flatMap
    // def flatMap[A,B](fa:M[A])(f: A=> M[B]):M[B]
    // TODO :-> as we know that Applicatives extends Functor so map comes form Functor
    // TODO : so we need to mark it override def because monad implements flatMap
    //  in-terms of map
    override def map [A,B](fa:M[A])(f: A=> B):M[B] =
      flatMap(fa)(x => pure(f(x)))
  }
}
