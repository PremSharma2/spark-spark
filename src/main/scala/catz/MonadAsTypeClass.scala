package catz

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}

object MonadAsTypeClass  extends App {
//TODO: This type class transforms the value A into M[A]
// it takes input as higherKinded Type and perform action named pure and flatMap on it
  //TODO : pure :  For example if A is int then it will transform the value of type int to List[Int]
  // TODO : Like Option(2)
  // TODO :It also transforms the M[A] to M[B] via flatMap
  // TODO : Here f is used to implement ETW pattern
  trait MyMonad[M[_]]{
    def pure[A](value :A):M[A]
  def flatMap[A,B](fa:M[A])(f: A=> M[B]):M[B]
  }
//TODO : Custom Monad type-class instance for type Option

  implicit def optionMonad() =
    new MyMonad[Option] {
      // Define flatMap using Option's flatten method
      override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
        fa.flatMap(f)

      // Reuse this definition from Applicative.it gives the value wrapped into Monad
      override def pure[A](a: A): Option[A] = Option(a)
    }

//TODO : This type class is provided by cats library
  import cats.Monad
  import cats.instances.option._ // implicit Monad[Option] type class instance

  val monadtypeClassInstanceForOption= Monad.apply[Option]
  val option4: Option[Int] =monadtypeClassInstanceForOption.pure(4)
  val f: Int => Option[Int] = x => if (x%4==0) Some(x+1) else None
  val aTransformedMonad= monadtypeClassInstanceForOption.flatMap(option4)(f)

 import cats.instances.list._ //
  val listMonadTypeClassInstance= Monad.apply[List]
  val listMonad= listMonadTypeClassInstance.pure(4)
  val transformedListMonad= listMonad.flatMap(x => List.apply(x,x+1))

  // TODO Monad of Future
  import cats.instances.future._
  implicit val ec: ExecutionContext = ExecutionContext.
    fromExecutorService(Executors.newFixedThreadPool(2))
  val MonadTypeClassInstanceFuture= Monad.apply[Future]
  val futureMonad: Future[Int] = MonadTypeClassInstanceFuture.pure(43)
  val transFormedFutureMonad= futureMonad.flatMap(x=>Future.apply(x+1))

  // TODO : USe of Monad type class is to make General API or rest ENd point
  // TODO : Which Excepts Any kind of Monad i.e Higher Kinded type
  def getPairsList(numbers:List[Int], chars:List[Char]):List[(Int,Char)]={
    numbers.
      flatMap(n => chars.map(char => (n,char)))
  }
  //TODO : Lets Generalize it any kind of monads

  def getPairs[M[_],A,B](bag1:M[A],bag2:M[B])(implicit monad:Monad[M]):M[(A,B)] = {
    monad.flatMap(bag1)(a=> monad.map(bag2)(b=> (a,b)))
  }
  val numbersList= List(1,2,3,4)
  val charsList: List[Char] =List('a','b','c')
  import cats.instances.list._
  println(getPairs(numbersList,charsList))
  val numberOption= Option(2)
  val charOption=Option('c')
  import cats.instances.option._
  println(getPairs(numberOption,charOption))
}
