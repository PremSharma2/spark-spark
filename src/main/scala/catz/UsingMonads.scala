package catz

import cats.Monad
object UsingMonads extends App {
import cats.instances.list._
  //def apply[F[_]](implicit instance : cats.Monad[F]) :instance
  val monadListtypeClassInstance: Monad[List] = Monad.apply[List]
  val list: List[Int] =monadListtypeClassInstance.pure(2)
  val transformedList: List[Int] = monadListtypeClassInstance.flatMap(list)(x =>List(x,x+1))

  //TODO : Right Hand type is desirable value and the Left hand type is undesirable value
  // TODO because either returns either Left or Right case class instance
  /*
  TODO
     Represents a value of one of two possible types (a disjoint union.)
    Instances of Either are either an instance of Left or Right.
    A common use of Either is as an alternative to Option for dealing with possible missing values.
   In this usage, None is replaced with a Left which can contain useful information.
   Right takes the place of Some.
    Convention dictates that Left is used for failure and Right is used for success.

    TODO
      val in = Console.readLine("Type Either a string or an Int: ")
     val result: Either[String,Int] = try {
      Right(in.toInt)
       } catch {
      case e: Exception =>
        Left(in)

  final case class Left[+A, +B](a: A) extends Either[A, B] {
  def isLeft = true
  def isRight = false
}


final case class Right[+A, +B](b: B) extends Either[A, B] {
  def isLeft = false
  def isRight = true
}
     }
   */
  val aManualEither: Either[String,Int] = Right(42)
  //TODO: Most general use case we usually see is by using type alias here

  type LoadingOr[T] = Either[String,T]
  type ErrorOr[T] = Either[Throwable,T]

  //TODO this syntax LoadingOr[T] looks like monad yes Either is Monad
  // TODO We can import the type class instances for Either
   import cats.instances.either._
  /*
  TODO
    implicit def catsStdInstancesForEither[A]: MonadError[Either[A, *], A] with Traverse[Either[A, *]] =
    new MonadError[Either[A, *], A] with Traverse[Either[A, *]] {
      def pure[B](b: B): Either[A, B] = Right(b)

      TODO: flatMap Impl
         def flatMap[B, C](fa: Either[A, B])(f: B => Either[A, C]): Either[A, C] =
          fa.flatMap(f)
   */
  val eitherMonadTypeClassInstance: Monad[LoadingOr] = Monad.apply[LoadingOr]
  val anEither: LoadingOr[Int] = eitherMonadTypeClassInstance.pure(42) // Loading[Int]==Right(45)
  val aTransformedEither: LoadingOr[Int] =
    eitherMonadTypeClassInstance.
       flatMap(anEither)(n => if (n%2==0) Right(42)
                          else Left("Loading Not Desirable Value of Either"))

  // TODO :Exercise for Custom types
  case class OrderStatus(orderID: Long , status:String)
  // TODO: Here this API here will return OrderStatus
  //  or will return String value stating status not found

  def getOrderStatus(ordreId:Long):LoadingOr[OrderStatus] = Right(OrderStatus(101,"ReadytoShip"))

  def trackLocation(orderStatus:OrderStatus): LoadingOr[String] =
    if(orderStatus.orderID>1000) Left("Not available yet !!!!")
    else Right("Amsterdam")

  //TODO: to combine these APIS here best way is flatMap ....i.e o/p of one is i/p of another
  //TODO : considering a fact that we are dealing with monads
  //TODO trackLocation is function we used to implement ETW pattern for monads
val orderLocation= eitherMonadTypeClassInstance.
           flatMap(getOrderStatus(ordreId = 101))(trackLocation)

  //TODO  : To use for comprehension we need to import the extension methods
  val orderStatus: LoadingOr[OrderStatus] = getOrderStatus(101)
  /*
 TODO:  Using implicit type Enrichment by using implicit class Ops[F[_]]
   */
  /*
  implicit class Ops[F[_], C] extends scala.AnyRef {
    type TypeClassType <: cats.FlatMap[F]
    val typeClassInstance : Ops.this.TypeClassType
    def self : F[C]
    def flatMap[B](f : scala.Function1[C, F[B]]) : F[B] = { /* compiled code */ }
   */
  /*
  TODO
   implicit final def catsSyntaxFlatMapOps[F[_]: FlatMap, A](fa: F[A]): FlatMapOps[F, A] =
    new FlatMapOps[F, A](fa)
    -----------------------------------------------------------------------------------
    TODO
      final class FlatMapOps[F[_], A](private val fa: F[A]) extends AnyVal {
     def flatMap=[B](f: A => F[B])(implicit F: FlatMap[F]): F[B] = F.flatMap(fa)(f)

    you write this and compiler wil transform this into as shown below eg
    getOrderStatus(ordreId = 101).flatMap(os => trackLocation(os))
  TODO : compiler re writes our code Like this
   eg : new FlatMapOps[Either, OrderStatus](fa).flatMap(f: OrderStatus => Either[B])(implicit F: FlatMap[Either])

 TODO    ----------------------type class instance for monad-------------------------------
     implicit def catsStdInstancesForEither[A]: MonadError[Either[A, *], A] with Traverse[Either[A, *]] =
    new MonadError[Either[A, *], A] with Traverse[Either[A, *]] {
      def pure[B](b: B): Either[A, B] = Right(b)

      TODO: flatMap Impl
         def flatMap[B, C](fa: Either[A, B])(f: B => Either[A, C]): Either[A, C] =
          fa.flatMap(f)
   */
import cats.syntax.flatMap._
import cats.syntax.functor._
  val orderLocationBetter: LoadingOr[String] = {
    getOrderStatus(ordreId = 101).flatMap(os => trackLocation(os))
  }
  val orderLocationForComprehension= for{
    os <-getOrderStatus(101)
    location <-trackLocation(os)
  } yield location



  val EitherMonad: LoadingOr[OrderStatus] = getOrderStatus(101)

 val transformedEitherMonad: LoadingOr[String] = EitherMonad.flatMap(trackLocation)
//TODO : ---------------------------------------------------------------------------------------


  //TODO: Exercise Connection Service API for web APP
  case class Connection(host:String, port:String)

  val config=Map(
    "host" -> "localhost",
    "port" -> "1.2.1.0"
  )


  //todo : Type class Every Where we Designed HttpService API inter terms of type class
  trait HttpService[M[_]]{
   def  getConnection(config:Map[String,String]): M[Connection]
    def issueRequest(connection:Connection , payload:String): M[String]

  }
  //TODO Provide a real implementation for http service with using Option, Either , Future

  object OptionHttpService extends HttpService[Option]{
    override def getConnection(config: Map[String, String]): Option[Connection] = {
      for{
        host <- config.get("host")
        port <- config.get("port")
      } yield Connection(host,port)

    }

    override def issueRequest(connection: Connection, payload: String): Option[String] =
      if(payload.length>20) None
      else  Some(s"Request {$payload} has been Accepted!!!")

  }
// TODO :Now Lets call the HttpService API here in functional style
  //TODO here we can use flatmap here because we want transform Option[A] monad to Option[B]
  // i.e Option[Connection] to Option[String]
  val optionHttpResponse: Option[String] = OptionHttpService.getConnection(config).flatMap{
    conn => OptionHttpService.issueRequest(conn,"Http-Payload")
  }

println(optionHttpResponse)

// TODO : Using For Comprehension we can also do that
  val responseHttpOptionFor: Option[String] = for{
    conn <- OptionHttpService.getConnection(config)
    response <- OptionHttpService.issueRequest(conn,"Http-Payload")
  } yield response

  println(responseHttpOptionFor)

  // TODO Implement HttpService for Either Monad typeclass instance

  object AggresiveHttpService extends HttpService[ErrorOr]{
    override def getConnection(config: Map[String, String]): ErrorOr[Connection] =
      if(!config.contains("host") || !config.contains("port"))  {
        Left(new RuntimeException("Connection could not be established !!!!"))
      }
      else Right(Connection(config("host"),config("port")))
    override def issueRequest(connection: Connection, payload: String): ErrorOr[String] = {
      if(payload.length>20) Left(new RuntimeException("Invalid Payload!!!"))
      else  Right(s"Request {$payload} has been Accepted!!!")
    }
  }

  //TODO Lets test this HttpService API

  val htpServiceResponse: ErrorOr[String] = for{
    conn <- AggresiveHttpService.getConnection(config)
    response <- AggresiveHttpService.issueRequest(conn,"Http-Payload")
  } yield response
  println(htpServiceResponse)

 // TODO ------------------------------------------------------------------------------------------
  // TODO : Lets design an generic API for HttpService Response which will handle all kinds of Monads

  // TODO it is same like this we did before but more Generalized way

  val orderLocationForComprehension1= for{
    os <-getOrderStatus(101)
    location <-trackLocation(os)
  } yield location


  def httpResponse[M[_]](service:HttpService[M],payload: String)(implicit  monad:Monad[M]): M[String] ={
   val myhttpResponse= for{
      conn <- service.getConnection(config)
      response <- service.issueRequest(conn,payload)
    } yield response
    myhttpResponse
  }
 println(httpResponse(AggresiveHttpService,"Hello Either!!"))
}
