package partialFunctions

object PartialFunctions extends App {

  val aFunction: Int => Int = (x: Int) => x + 1 //Function1[Int,Int]

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 43
    else if (x == 2) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val anicerFussyFunction: Int => Int = (x: Int) => x match {

    case 1 => 42
    case 2 => 43
    case 3 => 999
  }
  //Hence this anicerFussyFunction is a partial function because it is applicable
  // only to subset of Int values {1,2,5}
  //[1,2,5] => Int
  // it is same as above that we gave implementation of Function1[Int,Int] using lambda
  val aPartialFunction: PartialFunction[Int, Int] = {
    //these all are called partial function values
    case 1 => 42
    case 2 => 43
    case 3 => 999
  }
  println(aPartialFunction(2))
  // Partial Function are based on the pattern matching hence it will throw a matching error
  //println(aPartialFunction(53))
  // it will tell that whether the partial function is applicable to the the given integer value
  println(aPartialFunction.isDefinedAt(34))
  //a partial function can be converted into total function like a Function1[Int,Option[Int]]
  // using a lift method  x:Int=> Some(int) or Function Type  Int => Option[Int]
  val lifted: Int => Option[Int] = aPartialFunction.lift
  println(lifted(3))
  println(lifted(34))
  //Chaining of PArtial Functions
  // if first partial function is failed to for the given out of range input you gave
  // then u can chain the other partial function which works on that range
  // so it will fall back to another partial function
  /*aPartialFunction.orElse[Int, Int] = {
    case 34 => 67
  }*/
  // Partial function extends Normal Function
  // Hence we can write Total Function with Single Argument or
  // Normal Function Like Partial Function Syntax
  //Because Partial Function are subTypes of Normal Functions
  val aTotalFuntion: Int => Int = {
    case 1 => 99
  }
  // Both are Same because as we aware that Partial Function works on pattern match
  //Although is not partial function but because as we are aware that Partial Function Extends Function
  // Then they also can be used for the same purpose if we want to
  val aTotalFuntion1: Int => Int = x => x match {
    case 1 => 99
  }
  //HoFs also accepts Partial Functions
  val MappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 43
    case 3 => 999
  }
  /*
  {
    case 1 => 42
    case 2 => 43
    case 3 => 999
  }
  Here in the map we supplied the partial Function
  Note:PArtial Function can only have one parameter Type

   */
}
