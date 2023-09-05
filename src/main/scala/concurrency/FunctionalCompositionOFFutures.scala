package concurrency
import concurrency.FutureExercise.{Profile, SocialNetwork}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/*
Based on this, we can assume that we are dealing with a monad.
 Moreover, Future meets all three monad laws. But! Only if we use it
 for pure in-memory computation without side-effects
However, in real life, Future is almost always used for asynchronous IO
 with a web service or a database.
In this case, it might be dangerous to consider Future as a monad.
 */
object FunctionalCompositionOFFutures  extends  App {
  val mark: Future[Profile] = SocialNetwork.toFetchProfile("fb.id.1.zuck")
  //def map[S](f: T => S)(implicit executor: ExecutionContext): Future[S]
  val nameOnTheWall: Future[String] = mark.map(profile => profile.name)
  val marksBestFriend: Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  //val result: Future[Future[Profile]] = mark.map(profile => SocialNetwork.fetchBestFriend(profile))
  val zuckBestFriendRestricted: Future[Profile] = marksBestFriend.
                                            filter(profile => profile.name.startsWith("z"))
  //-----------------------------------------------------------------------------------

 for{
    mark: Profile <- SocialNetwork.toFetchProfile("fb.id.1.zuck")
    bill: Profile <- SocialNetwork.fetchBestFriend(mark)
  }  mark poke bill


Thread.sleep(1000)

  val recoverPartialFunction: PartialFunction[Throwable, Unit] = {
    case  e: Throwable => Profile("fb.id.0-dummy","Forevermore")

  }
  val recoverWithPartialFunction: PartialFunction[Throwable, Future[Profile]] = {
    case  e: Throwable => SocialNetwork.toFetchProfile("fb.id.1.zuck")

  }

  val aProfileNoMatterWhat: Future[Any] = SocialNetwork
    .toFetchProfile("unknwon-Id") .
     recover(recoverPartialFunction)

  // always use recoverWith when you want to fallback with actual existing object in database
  val aFetchedProfileNoMatterWhat: Future[Profile] = SocialNetwork.
    toFetchProfile("unknwon-Id").
    recoverWith(recoverWithPartialFunction)
  // or we can use this
  val fallbackResult= SocialNetwork.toFetchProfile("unknwon-Id").
    fallbackTo(SocialNetwork.toFetchProfile("fb.id.1.zuck"))
}
