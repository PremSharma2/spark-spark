package monads

import scala.concurrent.ExecutionContext.Implicits.global

object MonadicLawsUseCases  extends App {
  /**
TODO
    Here's a real-time use case in Scala where understanding Monadic
    Left Identity can be crucial: Imagine you're developing an application
    that performs asynchronous operations for file processing.
    You decide to use the Future monad to handle these operations.
    To validate that your monadic operations adhere to the laws,
     you would like to test the Left Identity Law.
  **/

  import scala.concurrent.{Future, ExecutionContext}
  import scala.util.{Success, Failure}
  import scala.concurrent.ExecutionContext.Implicits.global // Importing global execution context

  // Simulates downloading a file and returns the content as a string wrapped in a Future
  def downloadFile(fileName: String): Future[String] = Future {
    // Simulated file content
    s"Content of file: $fileName"
  }


  val fileName = "example.txt"

  // Wraps the value using Future.successful (analogous to `pure` for Future)
  val wrappedValue: Future[String] = Future.successful(fileName)

  // Function to apply, same as downloadFile
  val f: String => Future[String] = downloadFile

  // Using flatMap on the wrapped value
  val leftHandSide: Future[String] = wrappedValue.flatMap(f)

  // Directly applying the function to the plain value
  val rightHandSide: Future[String] = f(fileName)

  // Testing if both sides are equal
  leftHandSide.onComplete {
    case Success(lhsValue) =>
      rightHandSide.onComplete {
        case Success(rhsValue) =>
          if (lhsValue == rhsValue) {
            println(s"Left Identity Law holds: $lhsValue == $rhsValue")
          } else {
            println("Left Identity Law does not hold.")
          }
        case Failure(_) =>
          println("Failed to evaluate right-hand side.")
      }
    case Failure(_) =>
      println("Failed to evaluate left-hand side.")
  }


  //2nd Law

  /**
TODO
     Imagine you are developing an application that fetches
     user data from a database.
      You decide to use Slick,
     a database query and access library for Scala,
     which returns results as Future monads.
     To make sure your database operations are reliable,
     you'd like to validate that they adhere to the Right Identity Law.
     First, let's create a function that simulates fetching a user by ID from the database:
   **/


  import scala.concurrent.{Future, ExecutionContext}
  import scala.util.{Success, Failure}
  import scala.concurrent.ExecutionContext.Implicits.global // Importing global execution context

  case class User(id: Int, name: String)

  // Simulates fetching a user by ID and returns a User wrapped in a Future
  def fetchUserById(id: Int): Future[User] = Future {
    User(id, s"User_$id")
  }

  val userId1 = 42

  // Fetches a user by ID (monadic value)
  val monadicValue: Future[User] = fetchUserById(userId)

  // Function that wraps the value back into a Future (analogous to `pure`)
  val f1: User => Future[User] = Future.successful[User]

  // Using flatMap on the monadic value
  val leftHandSide1: Future[User] = monadicValue.flatMap(user => Future.successful(user))

  // Right-hand side is just the original monadic value
  val rightHandSide1: Future[User] = monadicValue

  // Testing if both sides are equal
  leftHandSide.onComplete {
    case Success(lhsValue) =>
      rightHandSide.onComplete {
        case Success(rhsValue) =>
          if (lhsValue == rhsValue) {
            println(s"Right Identity Law holds: $lhsValue == $rhsValue")
          } else {
            println("Right Identity Law does not hold.")
          }
        case Failure(_) =>
          println("Failed to evaluate right-hand side.")
      }
    case Failure(_) =>
      println("Failed to evaluate left-hand side.")
  }


  /*
  3rd Law
TODO
  Let's consider a situation where you are developing
  a service that interacts with various external APIs.
  You need to get some user details from one API,
  use those details to fetch some additional information from a second API,
  and then save this aggregated data somewhere.
   */

  import scala.concurrent.{Future, ExecutionContext}
  import scala.util.{Success, Failure}
  import scala.concurrent.ExecutionContext.Implicits.global // Importing global execution context

  case class UserDetails(id: Int, name: String)
  case class AdditionalInfo(id: Int, data: String)

  def getUserDetails(userId: Int): Future[UserDetails] = Future {
    UserDetails(userId, s"User_$userId")
  }

  def getAdditionalInfo(details: UserDetails): Future[AdditionalInfo] = Future {
    AdditionalInfo(details.id, s"Additional_data_for_${details.name}")
  }
  val userId = 1

  // Initial monadic value
  val m1: Future[Int] = Future.successful[Int](userId)

  // Functions to apply
  val f2: Int => Future[UserDetails] = getUserDetails
  val g: UserDetails => Future[AdditionalInfo] = getAdditionalInfo

  // Left-hand side: m.flatMap(f).flatMap(g)
  val leftHandSide2: Future[AdditionalInfo] = m1.flatMap(f2).flatMap(g)

  // Right-hand side: m.flatMap(x => f(x).flatMap(g))
  val rightHandSide2: Future[AdditionalInfo] = m1.flatMap(x => f2(x).flatMap(g))

  // Test if both sides are equal
  leftHandSide.onComplete {
    case Success(lhsValue) =>
      rightHandSide.onComplete {
        case Success(rhsValue) =>
          if (lhsValue == rhsValue) {
            println(s"Associativity Law holds: $lhsValue == $rhsValue")
          } else {
            println("Associativity Law does not hold.")
          }
        case Failure(_) =>
          println("Failed to evaluate right-hand side.")
      }
    case Failure(_) =>
      println("Failed to evaluate left-hand side.")
  }

}