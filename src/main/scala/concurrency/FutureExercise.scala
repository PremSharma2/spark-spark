package concurrency
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

object FutureExercise extends App {
  case class Profile(id:String , name:String){
    def poke(anotherProfile:Profile): Unit ={
      println(s"${this.name} poking another Person ${anotherProfile.name}")
    }

  }
  // all utility functionality goes here in singleton object
object SocialNetwork{
  val names = Map(

    "fb.id.1.zuck" -> "Mark" ,
    "fb.id.1.Bill" -> "Bill-Gates",
    "fb.id.1.dummy" -> "Dummy"
  )
  val friends= Map(
    "fb.id.1.zuck" -> "fb.id.1.Bill"
  )
  val random = new Random
  //Api
  def toFetchProfile(id :String) : Future[Profile] = Future.apply{
    // it simulates fetching from the database
    Thread.sleep(300)
    Profile(id,names.apply(id)  )
  }
  def fetchBestFriend(profile: Profile) : Future[Profile] = Future{

    Thread.sleep(random.nextInt(300))
    val bestFriendId: String =   friends.apply(profile.id)
    Profile(bestFriendId,names.apply(bestFriendId))
  }
}
val mark: Future[Profile] = SocialNetwork.toFetchProfile("fb.id.1.zuck")

  val result: Unit =mark onComplete{
    case Success(markProfile) => {
      val bill: Future[Profile] = SocialNetwork.fetchBestFriend(markProfile)
      bill onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(exception)   => exception.printStackTrace()
      }
    }
    case Failure(exception) => exception.printStackTrace()
  }
  Thread.sleep(1000)


}
