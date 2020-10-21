package exception

import scala.util.{Failure, Success, Try}


object HandlingFailure extends App {
  // Try is parent interface for both of them
  //Success and Failure  are  case classes so it has apply method 
  val aSuccess: Success[Int] =Success.apply(3)
  val aFailure: Failure[Nothing] =Failure.apply(new RuntimeException("Exception thrown due to failure"))
  println(aSuccess)
  println(aFailure)
  def unsafeMethod() : String= throw new RuntimeException("No String For You Buster")
  //Try will wrap it into Success or Failure
  val potentialFailuer: Try[String] =Try.apply(unsafeMethod)
  // checking whether Failure object is there or Success object is there
  println(potentialFailuer.isFailure)
  //syntactic sugar
  val anotherPotentialFailure: Try[String] = Try.apply {
    // code that will throw exception will come here
    unsafeMethod()
    
  }
  // utilities
  println(potentialFailuer.isSuccess)
  //orElse
  def backupMethod: String = "A valid backup Result"
  // if it is failure then return Try with Success
  val fallbackTry: Try[String] = Try.apply(unsafeMethod).orElse(Try.apply(backupMethod))
  println(fallbackTry.isSuccess)
  def betterUnsafeMethod():Try[String]=Failure.apply(new RuntimeException("Failure ocuured,throwing an exception"))
  def betterBackUpMethod() : Try[String]= Success.apply("Method succesfully executed : Returning a Valid Result")
  val betterFallBack: Try[String] = betterUnsafeMethod orElse betterBackUpMethod
   println(betterFallBack.isSuccess)

  //Try as Monad Exercise
 val monad1: Try[Int] = Try(1)orElse(Try(2))
  val monad2: Try[Int] = Try(2)orElse(Try(3))
  val z = for {
    a <- monad1
    b <- monad2
  } yield a * b

  val answer = z.getOrElse(0) * 2
  println(answer)
 
}