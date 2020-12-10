package catz

import java.util.concurrent.Executors

import monads.MonadicLawsUseCase.User

import scala.concurrent.{ExecutionContext, Future}

object MonadTransformerExercise  extends App {
  implicit val ec: ExecutionContext = ExecutionContext.
    fromExecutorService(Executors.newFixedThreadPool(2))
  case class Address(street :String)
  def findUserById(id: Long): Future[Option[User]] = ???
  def findAddressByUser(user: User): Future[Option[Address]] = ???

  def findAddressByUserId(id: Long): Future[Option[Address]] =
    findUserById(id).flatMap {
      case Some(user) => findAddressByUser(user)
      case None       => Future.successful(None)
    }

}


