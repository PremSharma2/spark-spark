package exceptionHandling

import scala.util.{Failure, Success, Try}

//TODO : -> Exception handling using Scala  in pure Functional  way
object HandlingFailure extends App {
  //todo: ->  Try is parent interface for both of them
  //todo :-> Success and Failure  are  case classes so it has apply method
  //TODO Actually Try is Monad in Pure functional way we will handle exception
  val aSuccess: Success[Int] =Success.apply(3)
  //TODO : success for Nothing represented by Unit
  //val aSuccessforNothing:Success[Nothing] = Success(())
  val aFailure: Failure[Nothing] =
    Failure.apply(new RuntimeException("Exception thrown due to failure"))
  println(aSuccess)
  println(aFailure)

  //TODO : -> Expression returning nothing type
  val nothing :Nothing = throw new RuntimeException("No String For You Buster")


  //TODO it shows that Nothing is sub class of every class
  //todo :-> Try will wrap it into Success or Failure
  def unsafeMethod : String= nothing
  /*
TODO
 trait Try{}
  object Try{
  def apply[T](r: => T): Try[T] =
    try Success(r) catch {
      case NonFatal(e) => Failure(e)
    }
   */
  //TODO :-> here is is clearly predicted that Try is covariant in nature
  val potentialFailures: Try[String] =Try.apply(unsafeMethod) // Try[Nothing]

  //todo:->  checking whether Failure object is there or Success object is there
  println(potentialFailures.isFailure)

  //TODO example which proves the handling Exceptions pure Functionally
  val functionalhandleOfException: Try[Int] =potentialFailures.map(_ => 2)
  //syntactic sugar
  val anotherPotentialFailure: String = Try.apply {
    // code that will throw exception will come here
    unsafeMethod

  }match {
    case Success(_) => "Pass"
    case Failure(value) => s"Failure $value "
  }
  // utilities
  println(potentialFailures.isSuccess)
  //orElse
  def backupMethod: String = "A valid backup Result"

  // TODO : -> if it is failure then return Try with Success with backup method
  val fallbackTry: Try[String] = Try.apply(unsafeMethod) orElse Try.apply(backupMethod)
  println(fallbackTry.isSuccess)

  def betterUnsafeMethod():Try[String]= Failure.apply(new RuntimeException("Failure occurred,throwing an exception"))

  def betterBackUpMethod() : Try[String]= Success.apply("Method successfully executed : Returning a Valid Result")

  //TODO :-> Returns this Try if it's a Success or the given default argument if this is a Failure which is actually success

  val betterFallBack: Try[String] = betterUnsafeMethod orElse betterBackUpMethod

   println(betterFallBack.isSuccess)

  //Try as Monad Exercise
 val monad1: Try[Int] = Try(nothing) orElse (Try(2))
  val monad2: Try[Int] = Try(2) orElse (Try(3))
  //TODO As we know that Try is also monad so we can use for comprehension

  val z: Try[Int] = for {
    a <- monad1
    b <- monad2
    if(a%2==0)
  } yield a * b

  val answer = z.getOrElse(0) * 2
  println(answer)
  println(anotherPotentialFailure)
 
}