package adts

object ADT {


  // product type
  case class WeatherForecastRequest(lattitude: Double, longitude: Double)
  // in terms of function  way it looks like
  //(Double,Double) => WeatherForecastRequest
  // type WeatherForecastRequest = Double X Double it is cartesian product
  // of tuple this type of cartesian product  is called Product data type

  // hybrid types
  sealed trait WeatherForecastResponse // Sum type

  case class Valid(weather: WeatherForecastRequest) extends WeatherForecastResponse // product type

  case class InValid(error: WeatherError, description: String) extends WeatherForecastResponse // product type

  // advantages
  // illegal state are not representable
  // highly composable
  // just data not functionalty
  sealed trait WeatherError // Sum type

  case object NotAvailableError extends WeatherError


  /**
   * EXERCISE 3
   * Hybrid type
   * Using enums, construct a `Currency` type,
   * whose cases can hold currency-specific values, for
   * USD, EURO, and other currencies.
   */

  sealed trait Currency

  object Currency {
    final case class USD(dollars: Int, cents: Int) extends Currency
  }


  object enum_utilities {
    sealed trait Card

    object Card {
      final case class Clubs(points: Int) extends Card

      final case class Diamonds(points: Int) extends Card

      final case class Spades(points: Int) extends Card

      final case class Hearts(points: Int) extends Card
    }

    Card.Clubs(10) match {
      case Card.Clubs(10) => println("Matched!")
      case _ => println("Did not match!")
    }
  }
}
