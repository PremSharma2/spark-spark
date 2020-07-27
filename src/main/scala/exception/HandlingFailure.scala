package exception

import scala.util.Success
import scala.util.Failure
import scala.util.Try
import java.util.Random


object HandlingFailure extends App {
  
  //Success and Failure  are  case classes so it has apply method 
  val aSuccess=Success.apply(3)
  val aFailure=Failure.apply(new RuntimeException("Exception thrown due to failure"))
  println(aSuccess)
  println(aFailure)
  def unsafeMethod() : String= throw new RuntimeException("No String For You Buster")
  val potentialFailuer=Try.apply(unsafeMethod)
  println(potentialFailuer)
  //syntactic sugar
  val anotherPotentialFailure= Try.apply {
    // code that will throw exception will come here
    unsafeMethod()
    
  }
  // utilities
  println(potentialFailuer.isSuccess)
  //orElse
  def backupMethod: String = "A valid backup Result"
  // if it is failure then return Try with Success
  val fallbackTry: Try[String] = Try.apply(unsafeMethod).orElse(Try.apply(backupMethod))
  println(fallbackTry)
  def betterUnsafeMethod():Try[String]=Failure.apply(new RuntimeException("Failure ocuured,throwing an exception"))
  def betterBackUpMethod() : Try[String]= Success.apply("Method succesfully executed : Returning a Valid Result")
  val betterFallBack: Try[String] = betterUnsafeMethod orElse betterBackUpMethod
   println(betterFallBack.isSuccess)
 
}