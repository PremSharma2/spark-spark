package concurrency
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
/*
Future and Promise are constructs used for synchronizing program execution
in some concurrent programming languages.
 They describe an object that acts as a proxy for a result that is initially unknown,
 usually because the computation of its value is yet incomplete.

 Futures provide a way to reason about performing many operations
 in parallel– in an efficient and non-blocking way

 The properties of Future:

Eagerly evaluated (strict and not lazy), meaning that when the caller of a function
receives a Future reference,
 whatever asynchronous process that should complete it has probably started already.
Memoized (cached), since being eagerly evaluated means that it behaves
like a normal value instead of a function
and the final result needs to be available to all listeners.
 The purpose of the value property is to return that memoized result or None
 if it isn’t complete yet.
 Goes without saying that calling its def value yields a non-deterministic result.
 */
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
