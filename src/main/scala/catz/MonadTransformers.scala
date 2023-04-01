package catz

import cats.implicits.catsStdInstancesForFuture

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}

// option transformer
import cats.data.OptionT
import cats.instances.list._ // fetch an implicit OptionT[List]
import cats.instances.future._
import cats.instances.future._
object MonadTransformers  extends App {

  /*
   TODO
        Right, the way monad transformers work is to help you on
         working with a "inner" monad
       that is being "transformed" by a "outer" monad.
       So F[Option[A]] can be turned into a OptionT[F, A] (where F is any monad),
       which is much easier to work with.

      TODo For example : -> Nested Monad List[Option[Int]] F is replaced here by List
       so List is outer Monad here
       When we have nested Monad and we want to iterate the values in nested  monad
        and apply any function over it i.e we are summing all the option values here
        normally we need to unwrap all the option values sum them up and rewrap these
         if we want to apply the sum function over this monad without unwraping
          then we have /monad transformers
          Scala provides API as Monad Transformer to handle nested monads
          final case class OptionT[F[_], A](value: F[Option[A]])
          OptionT is for where nested monad is Option basically
   */
  /*

  }

     def pure[F[_]]: PurePartiallyApplied[F] = new PurePartiallyApplied[F]
   */
  // Use case and need of Monad Transformers
  def sumAllOptions(values : List[Option[Int]]):Int ={1}

    // TODO MyMonad[M[_]]
    //TODO As we can see that companion object of the OptionT
    // accepts the higherkinded type
    // (value: F[Option[A]]  i.e List[Option[Int]]
    // OptionT has map and flatMap as well so for comprehension can be used here
    /* TODO : OptionT map impl
         def map[B](f: A => B)(implicit F: Functor[F]): OptionT[F, B] =
        OptionT(F.map(value)(_.map(f)))
     */
    /*
    TODO : OptionT flatMap impl
      def flatMap[B](f: A => OptionT[F, B])(implicit F: Monad[F]): OptionT[F, B] =
      // here value =  F[Option[A]] i.e List[Option[Int]]
      OptionT(F.flatMap(this.value)(_.fold(F.pure[Option[B]](None))(f)))

     def flatMapF[B](f: A => F[Option[B]])(implicit F: Monad[F]): OptionT[F, B] =
      OptionT(F.flatMap(value)(_.fold(F.pure[Option[B]](None))(f)))
     */
    // TODO : final case class OptionT[F[_], A](value: F[Option[A]])

    import cats.data.OptionT
    import cats.instances.list._ //TODO : -> fetch an implicit type-class instance for  Monad , functor type classes
    // it actually means that its a List[Option[Int]]
    /*
     TODO
        final case class OptionT[F[_], A](value: F[Option[A]])
        object OptionT{
         def apply(value: F[Option[A]]):OptionT
         }
         For this case
         TODO
           object OptionT{
           def apply(value: List[Option[A]]):OptionT
           }
     */
  // It is a wrapper over List[Option[Int]]
  //TODO use of for comprehension here
  val optionList: List[Option[Int]] = List(Option(1), Option(2), Option(3))
    val listOfOptions: OptionT[List ,Int] =OptionT.apply(optionList)
  /*
  TODO
        def foldLeft[B](b: B)(f: (B, A) => B)(implicit F: Foldable[F]): B =
        F.compose(Foldable[Option]).foldLeft(value, b)(f)
   */
    val sum: Int =listOfOptions.foldLeft(0)((a, b) => a + b)
  /*
  TODO
        def map[B](f: A => B)(implicit F: Functor[F]): OptionT[F, B] =
         OptionT( F.map(value)(_.map(f)) )
   */
     val rs: OptionT[List, Int] = listOfOptions.map(_*1)

  /*
  TODO
       def flatMap[B](f: A => OptionT[F, B])(implicit F: Monad[F]): OptionT[F, B] =
    flatMapF(a => f(a).value)

 TODO
  def flatMapF[B](f: A => F[Option[B]])(implicit F: Monad[F]): OptionT[F, B] =
    OptionT(  F.flatMap(value)( b => value.fold( F.pure[Option[B]](None) )(f))   )

    this is Option fold method
    final def fold[B](ifEmpty: => B)(f: A => B): B =
    if (isEmpty) ifEmpty else f(this.get)
   */
     val rs2= listOfOptions.flatMap(a => OptionT(List.apply(Option(a+1))))
    val listOfOptionChars: OptionT[List ,Char] =OptionT.
                     apply(List(Option('a'), Option('b'), Option('c'),Option.empty[Char]))
    val listOfTuples: OptionT[List, (Int, Char)] = {
      for {
         char <- listOfOptionChars
          int <-  listOfOptions
      } yield (int,char)

    }
  /*
   def map[B](f: A => B)(implicit F: Functor[List]): OptionT[F, B] =
        OptionT(F.map(value)(_.map(f)))
   */
  listOfOptions.map(_+1)

  println(listOfTuples.value)

  //TODO Most famous Monad Transformer is Either transformer it is used to work with
  //  List[Either[String,Int]] this kind of nested monads
   import cats.data.EitherT
  implicit val ec: ExecutionContext = ExecutionContext.
    fromExecutorService(Executors.newFixedThreadPool(2))
 val listOfEither: List[Either[String, Int]] = List(Left("Somethin Wrong"),Right(43))
  val futureOfEither: EitherT[Future, String, Int] = EitherT.right(Future(45)) // wrap over Future(Right(45))

  // it is wrapper over   List[Either[String,Int]] nested monad
  //final case class EitherT[F[_], A, B](value: F[Either[A, B]])
  // its like
final case class MyT[F[_], Int](value: F[Int])
  val x: MyT[List, Int] =MyT.apply(List(1,2,3))

  //TODO final case class EitherT[F[_], A, B](value: F[Either[A, B]])

  val listOfEiterT: EitherT[List, String, Int] =
    EitherT.apply(listOfEither)
  // it is wrapper over   Future[Either[String,Int]] nested monad
  val futureOfEiterT: EitherT[Future, String, Int] =
    EitherT.apply(Future.apply[Either[String,Int]](Right(42)))

  //val rs1 = futureOfEiter.map(_*1)


  //TODO--------------------------------------------------------------------------------------------------------------------------



/*
  TODO : Exercise
      We have multi machine cluster for your businesses which will receive a traffic
      surge following a media  appearance , We measure bandwidths in Units
      i.e amount of traffic in units
      We want to allocate Two of our servers to cope with traffic spike
      We know the current capacity for each server and we know we will hold the
      traffic if sum of bandwidths is > 250
      Note : -> bigger the Unit value larger the bandwidth you will have
      And also we know that total traffic coming is 250 units
 */

  val bandwidthsConfigOfServer = Map(
    "server1.com.EMEA" ->50,
    "server2.com.APAC" ->300,
    "server3.com.USA" ->170
  )
  // it is used  for   Future[Either[String,Int]] nested monad
  ////final case class EitherT[F[_], A, B](value: F[Either[A, B]])
  type AsyncResponseOFThread[T] = EitherT[Future, String, T]  // wrapper over Future[Either[String, T]]

 val dataBaseThread= Future.apply[Either[String,Int]](Left("Server Not Reachable!!!!"))

  val dbThread: Future[Either[String, Int]] =
    Future.successful(Left("Server Not Reachable!!!!"))

  //TODO bandwidth API here Int is bandwidth of server
  def getBandwidth(server:String): AsyncResponseOFThread[Int] ={
    bandwidthsConfigOfServer.get(server) match {
        //Future[Either[String,Int]]
        // trying access the database
      case None => EitherT.left(Future(s"Server $server unreachable"))
      case Some(b) => EitherT.right(Future(b))

    }
  }

  // TODO 1
  // hint: call getBandwidth twice, and combine the results

  // will return //Future[Either[String,Boolean]]
  def canStandWithSurge(s1:String,s2:String):AsyncResponseOFThread[Boolean] ={
    for{
      band1 <- getBandwidth(s1)
      band2 <- getBandwidth(s2)
    } yield band1 + band2 > 250
    // Future[Either[String, Boolean]]
  }
 /*
  def transform[C, D](f: Either[A, B] => Either[C, D])(implicit F: Functor[F]): EitherT[F, C, D] =
    EitherT(F.map(value)(f))
  */

  // TODO
  // hint: call canWithstandSurge + transform
  // TODO Here we are processing the Async Response of the Thread

  def generateTrafficSpikeReport(s1:String,s2:String):AsyncResponseOFThread[String] ={
    // transform will transform Future[Either[String,Boolean]] to
    // Future[Either[String,String]]
    // def transform[C, D](f: Either[A, B] => Either[C, D])(implicit F: Functor[F]): EitherT[F, C, D] =
    //    EitherT(F.map(value)(f))
    canStandWithSurge(s1,s2) .transform{
      case Left(reason) => Left(s"Servers $s1 and $s2 CANNOT cope with the incoming spike: $reason")
      case Right(false) => Left(s"Servers $s1 and $s2 CANNOT cope with the incoming spike: not enough total bandwidth")
      case Right(true) => Right(s"Servers $s1 and $s2 can cope with the incoming spike NO PROBLEM!")

    }
  }
  val result: Future[Either[String, String]] =generateTrafficSpikeReport("server1.com.EMEA","server2.com.APAC").value
  result.foreach(println)

// TODO ----------modified code---------------------------------------------------------
//   val futureOfEiterT: EitherT[Future, String, Int] = EitherT.
//     apply(Future.apply[Either[String,Int]](Right(42)))
  val futureOfEiter: EitherT[Future, String, Int] = EitherT.right(Future(43))
// both statements are same actually here
//val dataBaseThread1= Future.apply[Either[String,Int]](Left("Server Not Reachable!!!!"))
  // both are same
val dataBaseThread1= EitherT.left(Future("Server Not Reachable!!!!"))
  def getBandWidthAPIEnhanced(server:String): AsyncResponseOFThread[Int] ={
    bandwidthsConfigOfServer.get(server) match {
      //Future[Either[String,Int]]
      // trying access the database

      case None => EitherT.left(Future("Server Not Reachable!!!!"))
      //Future[Either[String,Int]]
      case Some(value) => EitherT.right(Future(value))

    }
  }

  def canStandWithSurgeModified(s1:String,s2:String):AsyncResponseOFThread[Boolean] ={
    for{
      band1 <- getBandWidthAPIEnhanced(s1)
      band2 <- getBandWidthAPIEnhanced(s2)
    } yield band1 + band2 > 250

  }

  def generateTrafficSpikeReportEnhanced(s1:String,s2:String):AsyncResponseOFThread[String] ={
    // transform will transform Future[Either[String,Boolean]] to Future[Either[String,String]]
    canStandWithSurgeModified(s1,s2) .transform{
      case Left(undesirableValue) => Left("Server s1 and s2 cannot cope with the incoming spike")
      case Right(false) =>  Left("Server s1 and s2 cannot cope with the incoming spike: not enough total bandwidth")
      case Right(true) => Right("Server s1 and s2 can cope with the incoming spike")

    }
  }
  val resultFuture: Future[Either[String, String]] =generateTrafficSpikeReport("server1.com.EMEA","server2.com.APAC").value
  result.foreach(println)

}
