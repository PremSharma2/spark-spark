package concurrency

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object ConcurrencyNewFeatureInScala extends App {


  sealed trait Weather

  case object Sunny extends Weather

  case object Cloudy extends Weather

  case object Rainy extends Weather

  case object Windy extends Weather

  case object Snowy extends Weather

  case object Foggy extends Weather

  /*
  Let’s start with a simple example.
    Imagine we want to implement a forecast service returning the weather,
  given a valid date.
  First of all, we need some objects that allow us to model the weather:

 So far, so good. Now to write our service.
 Our forecast service should return not a Weather directly,
  but a Future[Weather].
  Indeed, the weather service will use an external HTTP service to get the weather information.
  Calls to external services through the internet can have a very high cost in terms of I/O,
  waiting for a response for many milliseconds (or even seconds!).


   As we are prudent developers,
   we don’t want to waste our threads’ time waiting for HTTP responses.
   So, we leave the call to an ExecutionContext that wraps the computation
   result inside a Future.
   For the sake of simplicity, we give a trivial implementation of the HTTP client:




   */

  implicit val ec: ExecutionContext = ExecutionContext.
    fromExecutorService(Executors.newFixedThreadPool(2))

  class HttpClient {
    def get(url: String) =
      if (url.contains("2020-10-18"))
        Future(Sunny)
      else if (url.contains("2020-10-19"))
        Future(Windy)
      else {
        Future.apply{
          throw new RuntimeException
        }
      }
  }

  /*
3. How to Recover a Future
3.1. Recovering with a Synchronous Computation
The first attempt to develop the forecast service is very dumb. Indeed,
 it simply calls the HttpClient.get method:
 */

  class WeatherForecastService(val http: HttpClient) {
    def forecast(date: String): Future[Weather] =
      http.get(s"http://weather.now/rome?when=$date")
  }

  /*
  However, the problem with this version of the WeatherForecastService
  is that it leaves all the responsibility to deal with the Failure value to the client.
  But, a client of a forecast service might only retrieve the weather information
   and not handle any errors.

It would be nice if the service returned the previous retrieved value to the client
 in case of failure. So, let’s add an attribute to the service storing the previous forecast:

var lastWeatherValue: Weather = Sunny
We don’t focus on the fact that the lastWeatherValue is mutable and can lead to race conditions in a concurrent environment.

Therefore, the main question is: How can we recover from a Future completed with a Failure
instance? Fortunately, Scala 2.12 introduced the transform method to the Future API:

TODO
   def transform[S](f: (Try[T]) ⇒ Try[S]): Future[S]
   Basically, the transform method creates a new Future by applying the specified function
   to the result of this Future.
   Indeed, with a function that accepts a Try value as input,
   we can handle both a Future completed successfully and a Future completed exceptionally.
 Moreover, the input function returns another Try value,
 which means that we can decide to recover from a Failure,
 mapping the Future value in a new value,
 or we can decide to simply leave the failure state.

Returning to our scenario,
we can recover from a network error returning the previous retrieved forecast:

   */

  // TODO synchrnous call
  def forecast(date: String, http: HttpClient): Future[Weather] = {
    http.get(s"http://weather.now/rome?when=$date")
      .transform {
        case Success(result) =>
          val retrieved = result
          var lastWeatherValue = retrieved
          Try(retrieved)
        case Failure(exception) =>
          println(s"Something went wrong, ${exception.getMessage}")
          Try(Sunny)
      }
  }
    /*
 TODO
  What if we need to recover using a value coming from an asynchronous computation that
  returns a new Future? Again, we are fortunate because the Future API gives us the
    transformWith method:
  def transformWith[S](f: Try[T] => Future[S]): Future[S]
  The transformWith method creates a new Future by applying the specified function,
  which produces a Future, to the result of current Future.
  Ultimately, the method works like a flatMap on the Future type,
  which works both in a Future completed successfully and a Future completed exceptionally.
    In our scenario,
    Note:
  we can use the transformWith method by retrieving a forecast from a different fallback service
  in case of error:
*/
    def forecast(date: String,http: HttpClient, fallbackUrl: String): Future[Weather] = {
      http.get(s"http://weather.now/rome?when=$date")
        .transformWith {
          case Success(result) =>
            val retrieved = result
            var lastWeatherValue = retrieved
            Future(retrieved)
          case Failure(exception) =>
            println(s"Something went wrong, ${exception.getMessage}")
            http.get(fallbackUrl).map(_ => Sunny)
        }
    }

/*
Before Scala 2.12, the transform method had a different signature:

def transform[S](s: (T) ⇒ S, f: (Throwable) ⇒ Throwable): Future[S]
The above method takes two functions as input that allows transforming
a completed Future and a Future completed exceptionally.
 So, there was no way to turn a failure into a value different from an exception.
  We need to find a workaround.

Indeed, the workaround existed, and it was calling first the map method to deal
 with the happy path and then calling the recover method.
 The latter allows us to recover a Future that completed exceptionally,
  turning it into something different from an exception.

Let’s implement our first version of the forecast method using the tools
 that older versions of Scala give us:

def forecastUsingMapAndRecover(date: String): Future[Weather] =
  http.get(s"http://weather.now/rome?when=$date")
    .map { result =>
      val retrieved = Weather(result)
      lastWeatherValue = retrieved
      retrieved
    }
    .recover {
      case e: Exception =>
        println(s"Something went wrong, ${e.getMessage}")
        lastWeatherValue
    }
 */

  def forecastUsingMapAndRecover(date: String, http: HttpClient): Future[Weather] =
    http.get(s"http://weather.now/rome?when=$date")
      .map { result =>
        val retrieved = result
        retrieved
      }
      .recover {
        case e: Exception =>
          println(s"Something went wrong, ${e.getMessage}")
          Sunny
      }

  def forecastUsingFlatMapAndRecoverWith(date: String,http: HttpClient, fallbackUrl: String): Future[Weather] =
    http.get(s"http://weather.now/rome?when=$date")
      .flatMap { result =>
        val retrieved = result
        Future(retrieved)
      }
      .recoverWith {
        case e: Exception =>
          println(s"Something went wrong, ${e.getMessage}")
          http.get(fallbackUrl).map(_ => Sunny)
      }
}