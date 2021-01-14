package catz.datamanipulation

import cats.Id
import cats.data.Kleisli

object Readers extends App {

  /*
  TODO
   This conf file has all info for all the layers
   - configuration File => initial data structure
   - a DB layer i.e Repository
   - an http layer i.e Rest End point
   - a business logic layer  service layer
   */
  // this we will read from properties file
case class Configuration(dbUserName:String, dbPassword:String , host:String, port:Int , nThreads:Int , emailReplyto:String)

// TODO Repository layer Dao
  case class DbConnection(userName:String,password:String){
    def getOrderStatus(orderID:Long):String = "dispatched" // it select * from Order and return status of order

    def getLastOrderId(userName:String):Long= 101
  }

  case class HttpService(host:String, port:Int){
    def start():Unit = println("Server started")// this service will start the server
  }

  // now boot strapping the application
  // we need to read the configuration form file
  val configuration= Configuration("prem","sharma" , "localhost" , 1234 , 2 , "prem.kaushik@outlook.com")
  // now lets introduce Reader data processor API which is used to handle these situation i.e it will compose the functions
  import cats.data.Reader
  // This API has one input and one output here as we can see that
  // Reader apply method takes function which consumes the input and
  // generate the desired output it is wrapper over function
  //def apply[A, B](f: A => Id[B]): Reader[A, B] = ReaderT[Id, A, B].apply(f)
  //TODO here A is input to function B is o/p of function
  // and ID which is an identity type is final o/p of reader
  // type Reader[-A, B] = ReaderT[Id, A, B]
  //  where Id = Id[B] =B
  // type ReaderT[F[_], -A, B] = Kleisli[F, A, B]
  //  final case class Kleisli[F[_], -A, B](run: A => F[B])
  val dbReader: Reader[Configuration,DbConnection] = Reader.apply{
    conf => DbConnection(conf.dbUserName, conf.dbPassword)
  }
   val funtion1: Configuration => Id[DbConnection] = dbReader.run
  // Actually the mechanics here is that we have to derive the Output DbConnection
  // from the input Configuration via this function here we passed to apply
  // to fetch the derived output from the Reader we will call run on dbReader
// now this run method will run the function we passed in Reader apply method
  // hence readers are wrapper over function here we also can compose the function
  //TODO here Id is identity type i.e Id[B] =B
  // TODO here dbReader.run.apply(configuration) is dbreader.f.apply(configuration)
  // TODO we passed input to the function f is configuration
  val connection: Id[DbConnection] = dbReader.run.apply(configuration)

  //TODO: -> there is catch here what if we want transform this o/p to some other form
  // for that we have map function here available which will transform the o/p1 to o/p2
  // and other-way around is that the using map we will compose the functions
/*
def map[C](f: B => C)(implicit F: Functor[F]): Kleisli[F, A, C] =
    Kleisli(a => F.map(run(a))(f))
    TODO
        here run: A => F[B]
        so in map when  a => F.map(run(a))(f) we can replace run(a)
        with F[B]  a => F.map(F[B])(f)
        this F[B] here represents higherkindedtype but here it is Identity type
 */
  /*
  val dbReader: Reader[Configuration,DbConnection] = Reader.apply{
    conf => DbConnection(conf.dbUserName, conf.dbPassword)
  }
   */
  val myOrderStatusReader : Reader[Configuration,String] = dbReader.
           map(conn => conn.getOrderStatus(101))
// the flow will be like that here first orginal function will run i.e original run
// and we will get dbReader
  // and then dbReader map will run like this a => F.map(run(a))(f)
  val orderStatus: Id[String] = myOrderStatusReader.run(configuration)
  /*
  TODO
      This pattern goes like this
      1 you create the initial data structure
      2 you create a Reader which specifies  how that data structure will be manipulated initially
      3 you can then map or Flatmap the reader to produce derived information
      4 When you need the final piece of information you call the run on the reader
         with the initial data structure
         5 i.e the composed function will run at the end which is a composition of more then functions
   */
  //TODO Exercise
  /*
   val dbReader: Reader[Configuration,DbConnection] = Reader.apply{
    conf => DbConnection(conf.dbUserName, conf.dbPassword)
  }
   */
def getLastOrderStatus(userName:String): String = {
  val usersLastOrderIdReader: Reader[ Configuration, Long] =   dbReader.
    map(_.getLastOrderId(userName))

    /*
    def map[C](f: B => C)(implicit F: Functor[F]): Kleisli[F, A, C] =
    Kleisli(a => F.map(run(a))((a:A)=> f(a))

    def flatMap[C, AA <: A](f: B => Kleisli[F, AA, C])(implicit F: FlatMap[F]): Kleisli[F, AA, C] =
    Kleisli.shift(a => F.flatMap[B, C](run(a))((b: B) => f.apply(b).run.apply(a)))
     */
    val f: Long => Kleisli[Id, Configuration, String] = {
      (lastOrderID:Long) => dbReader.map(conn => conn.getOrderStatus(lastOrderID))
    }
    //val usersLastOrderIdReader: Reader[ Configuration, Long]
// g = a => F.flatMap[B, C](run(a))((b: B) => f.apply(b).run.apply(a))
    val usersLastOrderStatusReader:  Reader[ Configuration, String] =
     usersLastOrderIdReader.flatMap(f)
     val fx: Configuration => Id[String] = usersLastOrderStatusReader.run
  usersLastOrderStatusReader.run(configuration)
}
  println(getLastOrderStatus("Prem"))

  // TODO using for comprehension

  def getLastOrderStatusModified(userName:String): String = {

    val usersOrderForReader: Reader[ Configuration, String] =for{
      lastOrderID <- dbReader.map(_.getLastOrderId(userName))
      lastOrderStatus <- dbReader.map(_.getOrderStatus(lastOrderID))
    } yield lastOrderStatus
    usersOrderForReader.run(configuration)
  }
  // TODO Exercise
  case class EmailService(emailReplyTo:String){
    def sendEmail(address:String , contents:String) = s"Sending Email to $address: with Contents :$contents"
  }
  def emailUser(userName:String,userEmail:String):String = {
    // fetch the status of their last order
    // email them with the Email service
// creation of Initial datastructure

    /*
   val dbReader: Reader[Configuration,DbConnection] = Reader.apply{
    conf => DbConnection(conf.dbUserName, conf.dbPassword)
  }
   */
    val emailServiceReader: Reader[Configuration,EmailService] = Reader.apply{
      conf => EmailService(conf.emailReplyto)
    }
    val emailReader:Reader[Configuration,String] = for{
      lastOrderID <- dbReader.map(_.getLastOrderId(userName))
      lastOrderStatus <- dbReader.map(_.getOrderStatus(lastOrderID))
      emailService <- emailServiceReader
    } yield emailService.sendEmail(
      userEmail, s"your last order has the status: -> $lastOrderStatus")
    emailReader.run(configuration)
  }
  println(emailUser("prem","prem.kaushik@outlook.com"))
}
