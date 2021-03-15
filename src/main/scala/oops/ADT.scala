package oops

object ADT {
   // way of structuring the data basically
  // let say we want to model the weather forecast hierarchy
  sealed  trait Weather // This structure is called Sum type
  //TODO these are the possible constants for Weather
  case object Sunny extends Weather
  case object Windy extends Weather
  case object Rainy extends Weather
  case object Cloudy extends Weather
  // Weather = Sunny || Windy || Rainy || Cloudy
  // these ORS are exclusive ORS so we can use + instead of them
  // Weather = Sunny + Windy + Rainy + Cloudy
  def feeling (weather: Weather):String ={
    weather match {
      case Sunny => "Its Sunny"
      case Windy => "Its Windy"
      case Rainy => "Its Rainy"
      case Cloudy => "Its Cloudy"
    }
  }

  // product type
  case class WeatherForecastRequest(lattitude:Double,longitude:Double)
  // in terms of function  way it looks like
  //(Double,Double) => WeatherForecastRequest
  // type WeatherForecastRequest = Double X Double it is cartesian product
  // of tuple this type of cartesian product  is called Product data type

// hybrid types
  sealed trait  WeatherForecastResponse // Sum type
  case class Valid(weather:WeatherForecastRequest) extends WeatherForecastResponse// product type
  case class InValid(error:WeatherError, description:String) extends WeatherForecastResponse // product type
  // advantages
  // illegal state are not representable
  // highly composable
  // just data not functionalty
  sealed trait WeatherError  // Sum type
  case object NotAvailableError extends WeatherError
}
