package partialFunctions

object PartialFunctions extends App {
// ths function for all values of domain A
  val aFunction: Int => Int = (x: Int) => x + 1 //Function1[Int,Int]

// a function which is applicable on certain set of inputs
  val aFussyFunction: Int => Int = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 43
    else if (x == 2) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  /*
 TODO
     A partial function of type PartialFunction[A, B] is a unary function
     where the domain does not necessarily include all values of type A.
    The function isDefinedAt allows [you] to test dynamically
    if a value is in the domain of the function.”

TODO
   In short, a function is a mapping A => B that
   relates each value of type A to a value of type B–modulo bottom.
   A and B are called domain and codomain, respectively.
   If you’re not a math addict, roughly speaking,
   the domain is the set of all values that you may provide as input to your function,
   while the codomain is the result of the function application to the input,
    that is your function output.
   On the other hand a partial function
   from A to B is not defined for some inputs of type A
   */

  val anicerFussyFunction: Int => Int = {

    case 1 => 42
    case 2 => 43
    case 3 => 999

  }

//todo  Partial Function with pattern  matching
  val anonymousPartialFunction: Int => Int = {
//TODO this function uses anoumous partial function
    case 1 => 42
    case 2 => 43
    case 3 => 999

  }
 // println(anicerFussyFunction(33))
  //Hence this anicerFussyFunction is a partial function because it is applicable
  // only to subset of Int values {1,2,3}
  //[1,2,3] => Int
  // it is same as above that we gave implementation of Function1[Int,Int] using lambda
  // We have Partial Function in scala to implement this kind of functionality
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
  val newPartialFunction: PartialFunction[Int, Int] = {
    //these all are called partial function values
    case 1 => 42
    case 2 => 43
    case 3 => 999
  }

  val chainedfunction: PartialFunction[Int, Int] = {
    case 34 => 67
  }


 val shouldHandleAlldomainValues: PartialFunction[Int, Int] = newPartialFunction orElse chainedfunction

  println(shouldHandleAlldomainValues.apply(34))

  // Partial function extends Function1[]
  // Hence we can write Total Function with Single Argument or
  // Normal Function Like Partial Function Syntax
  //Because Partial Function are subTypes of Normal Functions

  val aTotalFuntion: Int => Int = {
    case 1 => 99
  }

  // Both are Same because as we aware that Partial Function works on pattern match
  //Although i not partial function but because as we are aware that Partial Function Extends Function
  // Then they also can be used for the same purpose if we want to
  // we have Converted match statement to pattern matching anonymous function
  val aTotalFuntion1: Int => Int = {
    case 1 => 99
  }


  val mypf: PartialFunction[Int, Int] =PartialFunction.apply(aTotalFuntion)
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
  Note:Partial Function can only have one parameter Type

   */
  val partialList=List(3,9,99)
  println(partialList.map(newPartialFunction))

  // partial functio using flatMap

  import math.sqrt
  List(-2.0, 0.0, 2.0).flatMap {
    case d if d < 0.0 => List.empty
    case 0.0          => List(0.0)
    case d            => List(sqrt(d), -sqrt(d))
  }


}
