package monads

object NeedOFMonads extends App {
  /*
     TODO
      You can think of monads as wrappers. You just take an object and wrap it with a monad.
      It’s like wrapping a present; you take a scarf, a good book or a fancy watch,
      and wrap it with some shiny paper and a red ribbon.
      We wrap gifts with shiny paper because they look pretty.
      We wrap objects with monads because monads provide us with the following two operations:
      identity (return in Haskell, unit in Scala)
      bind (>>= in Haskell, flatMap in Scala)
   */
  // this constructor are called  pure or unit in scala because they take one value
  //  and  wrap them in container
  case class SafeValueContainer[+T](private val internalValue:T){
    def get:T = synchronized{
      internalValue
    }
    //ETW pattern implementation in single Shot
    // We can read this like that
    // We extract the original input value to the container SafeValue
    // transform it and wrap that into container so output will be the final
    // Transformed value wrapped in a container
     // in scala these type of transformer functions or methods are called
     // flatMap so lets rename it to flatMap or lets define new method flatMap
    def transform [S] (fx : T => SafeValueContainer[S]) : SafeValueContainer[S] = synchronized{
      fx.apply(internalValue)
    }
     def flatmap [S](fx : T => SafeValueContainer[S]) : SafeValueContainer[S] = synchronized{
       fx.apply(internalValue)
     }
  }

  // We have some external API which has a method
  def giveMeSafeValue[T](value:T):SafeValueContainer[T] = SafeValueContainer.apply(value)
  val safeStringContainer:SafeValueContainer[String] = giveMeSafeValue("Scala is aweosome")
  // now scenerio is we want to process the string is Wrapped in the SafeValue wrapper
  // to process that we need to extract it from the wrapper
  // three steps of ETW pattern here are ass follows:
  //1
  // so i am gonna call the extractor of wrapper
  val wrappedString: String = safeStringContainer.get
  //2
  // now we are going to transform it
  val upperString= wrappedString.toUpperCase
  //3
  // no we need to wrap it again in the container so that someone can again access it
  val upperSafeString: SafeValueContainer[String] = giveMeSafeValue(upperString)
  // this pattern is called ETW (Extract Transform and Wrap)
  // So we can modify this code as we are doing ETW in three diffrent stages
  // we can define a method which will do the ETW in single shot
  //---------------Etw pattern now with Transformer which implement ETW in single shot--------------
// scala will extract the value form Container and will apply the transforming function to this Value
  // And then Wrap it again in Container
 val etwpatternOutput = safeStringContainer.transform(string => SafeValueContainer(string.toUpperCase))
  println(etwpatternOutput)
// now as we have modified the SafeValueContainer this satisfies the Monads condition so it is monad
println(safeStringContainer.flatmap(string => SafeValueContainer(string.toUpperCase)))
}
