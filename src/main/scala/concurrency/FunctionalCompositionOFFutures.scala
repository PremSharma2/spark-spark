package concurrency
import scala.concurrent.ExecutionContext.Implicits.global
import concurrency.FutureExercise.{Profile, SocialNetwork, mark}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object FunctionalCompositionOFFutures  extends  App {
  val mark: Future[Profile] = SocialNetwork.toFetchProfile("fb.id.1.zuck")
  //def map[S](f: T => S)(implicit executor: ExecutionContext): Future[S]
  val nameOnTheWall: Future[String] = mark.map(profile => profile.name)
  val marksBestFriend: Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  //val result: Future[Future[Profile]] = mark.map(profile => SocialNetwork.fetchBestFriend(profile))
  val zuckBestFriendRestricted= marksBestFriend.filter(profile => profile.name.startsWith("z"))
 for{
    mark: Profile <- SocialNetwork.toFetchProfile("fb.id.1.zuck")
    bill: Profile <- SocialNetwork.fetchBestFriend(mark)
  }  mark poke bill
Thread.sleep(1000)
  val recoverPArtialFunction: PartialFunction[Throwable, Unit] = {
    case  e: Throwable => Profile("fb.id.0-dummy","Foreveralone")

  }
  val recoverWithPartialFunction: PartialFunction[Throwable, Future[Profile]] = {
    case  e: Throwable => SocialNetwork.toFetchProfile("fb.id.1.zuck")

  }
  val aProfileNoMatterWhat: Future[Any] = SocialNetwork
    .toFetchProfile("unknwon-Id") .
     recover(recoverPArtialFunction)
  // always use recoverWith when you want to fallback with actual existing object in database
  val aFetchedProfileNoMatterWhat: Future[Profile] = SocialNetwork.
    toFetchProfile("unknwon-Id").
    recoverWith(recoverWithPartialFunction)
  // or we can use this
  val fallbackResult= SocialNetwork.toFetchProfile("unknwon-Id").
    fallbackTo(SocialNetwork.toFetchProfile("fb.id.1.zuck"))
}
