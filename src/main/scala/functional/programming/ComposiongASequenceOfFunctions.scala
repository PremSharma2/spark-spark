package functional.programming

object ComposiongASequenceOfFunctions  extends App {


  def f1: Int => Int = _ + 2
  def f2: Int => Int = _ * 3
  def f3: Int => Int = _ / 2

  def weakenRestrictions[A, B](f: A => B): Option[Any] => Option[Any] = {
      case Some(x) => x match {
      case a: A => Some(f(a))
      case _ => None
    }
    case _ => None
  }

  val weakFunctions: List[Option[Any] => Option[Any]] = List(f1, f2, f3).map(weakenRestrictions)
  val weakComposition: Option[Any] => Option[Any] = weakFunctions.reduce(_ andThen _)

  // Prints Some(5.0)

}
