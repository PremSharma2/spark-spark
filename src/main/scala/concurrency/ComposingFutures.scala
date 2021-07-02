package concurrency

object ComposingFutures extends App {


  import java.math.BigInteger
  import java.net.URL
  import java.security.MessageDigest

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future

  type Name = String
  type Email = String
  type Password = String
  type Avatar = URL

  case class User(name: Name, email: Email, password: Password, avatar: Avatar)

  def notExist(email: Email): Future[Boolean] = Future {
    Thread.sleep(100)
    true
  }
  def md5hash(str: String): String =
    new BigInteger(1,
      MessageDigest
        .getInstance("MD5")
        .digest(str.getBytes)
    ).toString(16)
  def avatar(email: Email): Future[Avatar] = Future {
    Thread.sleep(200)
    new Avatar("http://avatar.example.com/user/23k520f23f4.png")
  }
  def createUser(name: Name, email: Email, password: Password): Future[User] =
    for {
      _ <- notExist(email)
      avatar <- avatar(email)
      hashedPassword = md5hash(password)
    } yield User(name, email, hashedPassword, avatar)
}
