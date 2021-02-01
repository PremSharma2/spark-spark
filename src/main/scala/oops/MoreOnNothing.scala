package oops

object MoreOnNothing  extends App {

  // if i defined my class like this
  class MyClass// scala implicitly add extends AnyRef

// this throw new NoSuchElementException Expression is applicable for Nothing as well as Int
  // This means that this Expression return a type which is a super type or which is applicable
  // to the Desired return type or  For the Exception thrown
  // throw expression always return
  //Nothing is not  !=Null  !=Unit !=anything at all
  // Nothing is actually type of NothingNess
  // or we can say that Nothing is special sub type for anytype actually
  //
  def gimmeANumber():Int = throw new NoSuchElementException
  // Null is also treated like Nothing
  def gimmeARference ():MyClass = null
//  def gimmeARference1 ():MyClass = Nothing
  def afunctonAboutNothing(a: Nothing):Int= 45
  // as we know that the any expression can return nothing is Exception throw new NoSuchElementException
  afunctonAboutNothing(throw new NoSuchElementException)
  // Nothing is very useful in Generics
  trait MyGenericList[+A] {

  }
  object EmptyList extends MyGenericList[Nothing]
  // now here it is clear that MyGenericList[Nothing] can be supplied to MyGenericList[String]
  // is acting like nothing as in above cases
  val listOfstrings :MyGenericList[String]= EmptyList

}
