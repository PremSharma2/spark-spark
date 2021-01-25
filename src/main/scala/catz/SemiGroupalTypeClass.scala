package catz

import java.util.concurrent.Executors

import cats.Monad
import cats.data.Validated

import scala.concurrent.{ExecutionContext, Future}

object SemiGroupalTypeClass {
//TODO type class Semigroupal
  // which gives product method
  // Monad also extends Semigroupal product in monad comes form Semigroupal

  trait MySemiGroupal[F[_]]{
    def product [A,B](fa:F[A],fb:F[B]):F[(A,B)]
  }

   import cats.Semigroupal
  import cats.instances.option._

  val optionSemigroupalTypeClassInstance: Semigroupal[Option] = Semigroupal.apply[Option]
  val aTupledValue: Option[(Int, Int)] = optionSemigroupalTypeClassInstance
                                             .product(Some(2),Some(3))

  val noneTupled: Option[(Int, Nothing)] = optionSemigroupalTypeClassInstance
                        .product(Some(2),None)
  // Lets Test it for Future
  import cats.instances.future._

  implicit val ec:ExecutionContext = ExecutionContext.
    fromExecutorService(Executors.newFixedThreadPool(2))
  val aSemigroupalFuturetypeClassInstance: Semigroupal[Future] =
    Semigroupal.apply[Future]
    val aTupledFuture: Future[(Int, Int)] = aSemigroupalFuturetypeClassInstance
                               .product(Future(3),Future(4))

  import cats.instances.list._
  val alistSemigroupaltypeclassInstance: Semigroupal[List] = Semigroupal.apply[List]
  val tupledList: List[(Int, String)] = alistSemigroupaltypeclassInstance
                     .product(List(1,2),List("a","b"))


  /*
    TODO Exercise Lts generalize this API using monad in pure FP way
   */
  def productWithMonads[F[_],A,B](fa:F[A],fb:F[B])(implicit monad:Monad[F]): F[(A, B)] = {
    monad.flatMap(fa)(a => monad.map(fb)(b => (a,b)))
  }
  // TODO or we can do like that by using extension methods
/*
implicit class  Ops[F[_], C] extends scala.AnyRef {
    type TypeClassType <: cats.FlatMap[F]
    val typeClassInstance : Ops.this.TypeClassType
    def self : F[C]
    def flatMap[B](f : C => F[B]) : F[B] = { /* compiled code */ }
 */
  import cats.syntax.functor._ // for implicit map
  import cats.syntax.flatMap._ // for implicit flatMap
  def productWithMonadsAnExtensionMethods[F[_],A,B](fa:F[A],fb:F[B])(implicit monad:Monad[F]): F[(A, B)] =
   fa.flatMap(a => fb.map(b => (a,b)))

  def productWithMonadsAnExtensionMethodsfor[F[_],A,B](fa:F[A],fb:F[B])(implicit monad:Monad[F]): F[(A, B)] ={
    for{
      a <- fa
      b <- fb
    } yield (a,b)
  }
  // TODO Semigroupal implementation in terms of Monads
  //  Monad extends Semigroupal the product def comes from Semigroupal
  trait MyMonad[M[_]] extends MySemiGroupal[M] {
    def pure[A](value :A):M[A]
    //ETW pattern
    def flatMap[A,B](fa:M[A])(f: A=> M[B]):M[B]
    def map[A, B](bag: M[A])(f: A => B): M[B]
    def product [A,B](fa:M[A],fb:M[B]):M[(A,B)] =
      flatMap(fa)(a => map(fb)(b=> (a,b)))
  }
/*
 TODO
   Note: As we have proved that Monads extends Semigroupal and product of two Monads
   also when we imported the cats.instances.list._ // Monad[List] bcz monad is semigroupal
   and Monad list runs product in terms of for comprehension
    that is why we got cartesian product
   But  this cartesian product is achived using for comprehension i.e using map and flatmap
  and map and flatMap obeys the so called  monads  laws that is
   monad laws enforce to  follows a particular sequence in operation or execution
   So sometimes we might need to combine values without imposing a particular sequence in operation
   so Semigroupal can used where sequencing is not required
   like Validated


 */
  //import cats.data.Validated
  //  //import cats.instances.list._
  /*
  TODO
     It got combined using the combine method of  Semigroup[List]
     because Validated[List[String],Int] and validated requires Semigroup[List]
     instance to combine the values of Invalid(List)
     I mean Semigroupal/Monad type class instance of type
      Validated[List[String],Int] has implemented product diffrently not influneced
      by monadic Laws so it will give correct ans
   */
  type ErrorsOr[T] = Validated[List[String],T]
  val validatedSemigroupaltypeclassinstance: Semigroupal[ErrorsOr] =
    Semigroupal.apply[ErrorsOr]// it requires an implicit instance of Semigroup[List[_]]
  val invalidCombination: ErrorsOr[(Nothing, Nothing)] = validatedSemigroupaltypeclassinstance
       .product(Validated.invalid(List("Invalid value"))
      ,Validated.invalid(List("Second-Invalid-value")))

// TODO Now this product of two Either monads using semigroupal
  // will get short circuit because product of these two Semigroupal or Monads
    // will be implemented internally via for comprehension i.e map and flatMap
  // and that will follow monadic laws as well show it will shortcircuit
  // and it will not result in cartesian product
  type EitherOrError[T] = Either[List[String],T]
  import cats.instances.either._ // implicit Monad[Either]
  val eitherSemigroupaltypeclassInstance: Semigroupal[EitherOrError] =
    Semigroupal.apply[EitherOrError]
    val anEitherCombination = eitherSemigroupaltypeclassInstance.product(
      Left(List("Something-Wrong","Exception-Occured")),
      Left(List("Key Not found Exception"))
    )

val zipListSemigroupal :Semigroupal[List] = new Semigroupal[List] {
  override def product[A, B](listA: List[A], listB: List[B]): List[(A, B)] = {
    listA.zip(listB)
  }
}

  def main(args: Array[String]): Unit = {

    def abc [F[_],A] (fa:F[A]) :String= "hello"
    abc(List(1,2,3,4))
    println(tupledList)
    println(invalidCombination)
    println(anEitherCombination)
    println(zipListSemigroupal.product(List(1,2),List(3,4)))
  }
}
