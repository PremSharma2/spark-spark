package adts

object SumTypeADT {

  // way of structuring the data basically
  // let say we want to model the weather forecast hierarchy
  sealed  trait Weather // This structure is called Sum type
  //TODO these are the possible constants for Weather
  object Weather {
    case object Sunny extends Weather

    case object Windy extends Weather

    case object Rainy extends Weather

    case object Cloudy extends Weather

    // Weather = Sunny || Windy || Rainy || Cloudy
    // these ORS are exclusive ORS so we can use + instead of them
    // Weather = Sunny + Windy + Rainy + Cloudy
    //smart Constructor
    def feeling(weather: Weather): String = {
      weather match {
        case Sunny => "Its Sunny"
        case Windy => "Its Windy"
        case Rainy => "Its Rainy"
        case Cloudy => "Its Cloudy"
      }
    }
  }
}
