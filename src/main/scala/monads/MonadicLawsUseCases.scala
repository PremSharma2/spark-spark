package monads

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

object MonadicLawsUseCases extends App {

  println("==== Testing Monad Laws in Scala ====\n")

  // --- 1. Left Identity Law ---
  def downloadFile(fileName: String): Future[String] = Future {
    s"Content of file: $fileName"
  }

  val fileName = "example.txt"
  val wrappedValue = Future.successful(fileName)
  val f: String => Future[String] = downloadFile

  val leftIdentity = wrappedValue.flatMap(f)
  val rightIdentity = f(fileName)

  val leftIdentityTest = for {
    lhs <- leftIdentity
    rhs <- rightIdentity
  } yield {
    println("== Left Identity Law ==")
    if (lhs == rhs)
      println(s"✔ Holds: $lhs == $rhs\n")
    else
      println(s"✘ Does not hold: $lhs != $rhs\n")
  }

  // --- 2. Right Identity Law ---
  case class User(id: Int, name: String)
  def fetchUserById(id: Int): Future[User] = Future.successful(User(id, s"User_$id"))

  val userId1 = 42
  val monadicValue = fetchUserById(userId1)

  val rightIdentityTest = for {
    lhs <- monadicValue.flatMap(user => Future.successful(user))
    rhs <- monadicValue
  } yield {
    println("== Right Identity Law ==")
    if (lhs == rhs)
      println(s"✔ Holds: $lhs == $rhs\n")
    else
      println(s"✘ Does not hold: $lhs != $rhs\n")
  }

  // --- 3. Associativity Law ---
  case class UserDetails(id: Int, name: String)
  case class AdditionalInfo(id: Int, data: String)

  def getUserDetails(userId: Int): Future[UserDetails] = Future.successful(UserDetails(userId, s"User_$userId"))
  def getAdditionalInfo(details: UserDetails): Future[AdditionalInfo] =
    Future.successful(AdditionalInfo(details.id, s"Additional_data_for_${details.name}"))

  val userId = 1
  val m = Future.successful(userId)
  val f2: Int => Future[UserDetails] = getUserDetails
  val g: UserDetails => Future[AdditionalInfo] = getAdditionalInfo

  val associativityTest = for {
    lhs <- m.flatMap(f2).flatMap(g)
    rhs <- m.flatMap(x => f2(x).flatMap(g))
  } yield {
    println("== Associativity Law ==")
    if (lhs == rhs)
      println(s"✔ Holds: $lhs == $rhs\n")
    else
      println(s"✘ Does not hold: $lhs != $rhs\n")
  }

  // Wait for all tests to complete
  Await.result(Future.sequence(Seq(leftIdentityTest, rightIdentityTest, associativityTest)), 5.seconds)
}
