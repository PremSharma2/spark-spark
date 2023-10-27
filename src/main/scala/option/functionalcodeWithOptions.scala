package option

object functionalcodeWithOptions extends App {
  /*
  TODO
    To recap:
     1:map turns a list into another, where for every element in the original list,
     the supplied function is apply
    2:flatMap runs a function on every element on the list,
     resulting in many mini-lists;
     these lists are then concatenated into the final result
    3:filter keeps the elements which satisfy the boolean predicate passed as argument
     Because we have map and flatMap, we can run for-comprehensions on lists:


   */

  val combinedLists = for {
    num <- List(1, 2, 3)
    char <- List('a', 'b')
  } yield s"$num-$char"
  // List(1,2,3).flatMap(num => List('a','b').map(char => s"$num-$char"))
  // [1-a, 1-b, 2-a, 2-b, 3-a, 3-b]

  /*
TODO
    Understanding Options
    The easiest way to understand a Scala Option is to
    imagine it as a list with at most one value, that is,
    it either contains a single value, or nothing at all.
   */
  val anOption: Option[Int] = Option(42) // an option that contains a single value: 42

  /*
 TODO
   case object None extends Option[Nothing]
   because Empty returns None
  def isEmpty = true
  def get = throw new NoSuchElementException("None.get")
   */
  val anEmptyOption: Option[Int] = Option.empty // an option that doesn't contain any value
  /*
TODO
    First, let’s talk about the possible cases of an Option:
   an empty Option containing no value
  an Option containing a single value
   */
  val anOption_v2: Option[Int] = Some(42) // same as Option(42)
  val anEmptyOption_v2: Option[Int] = None // same as Option.empty
  /*
TODO
    Much like lists monad, Options can be transformed with map, flatMap and filter.
    The map function transforms an Option by returning a new Option containing
    nothing, if the original Option was empty
    the transformed element of the original Option contained a value
   */
  val aTransformedOption: Option[Int] = anOption.map(x => x * 10) // Some(420)

  /*
TODO
    The flatMap method is a bit more difficult,
    but it resembles the list flatMap method —
    the function we pass as argument turns an element into another Option:
    However, in this case, the flatMap method doesn’t have the meaning of
    concatenation (as was the case for lists),
    because here there’s either a single value, or nothing.
    For Option, the flatMap method is a way of chaining
    computations that may either produce a value or nothing. This general idea of sequencing
   */
  val aTransformedOption_v2: Option[Int] = anOption.flatMap(x => Option(x * 10)) // Some(420)

  /*
TODO
    Finally, the filter method retains the original value of the Option
    if it passes a predicate, or it discards it otherwise,
    turning the original Option into None.
    Of course, if the original Option was empty to begin with,
    there’s nothing to filter, hence nothing to process.
   */
  val aFilteredOption: Option[Int] = anOption.filter(x => x > 100) // None

  /*
TODO
    Now, because we have map and flatMap, we also have for-comprehensions unlocked for Option:
   */
  val combinedOptions = for {
    num <- Option(42)
    str <- Option("Scala")
  } yield s"$str is the meaning of life, aka $num"

  /*
TODO
   The isEmpty method checks whether an Option contains nothing.
   The getOrElse method returns either the value inside the option,
  or the default value which we pass as argument.
   Finally, the orElse method returns the original Option if non-empty
   or the other option otherwise.
   There’s also a get method on Option, which returns the value inside,
   but beware: if the Option is empty, the method will crash!
   */
  val checkEmpty: Boolean = anOption.isEmpty
  val innerValue: Int = anOption.getOrElse(99)
  val aChainedOption: Option[Int] = anEmptyOption.orElse(anOption)

/*
TODO
  Why We Need Options in Scala
  At this point, you might be thinking why on Earth
  we need this weird data structure which resembles a list,
  but it only contains a single value or nothing. Why can’t we just use a list?
  The reason why Options are useful is not the same why Lists are useful.
  Options are not used to “store” values — as your intuition clearly can tell,
  we can use lists to store as many values as we like.
  The utility of Options is not in data storage
  Options are used to describe the possible absence of a value.
  If, like most of us, your experience is mostly with the “mainstream” languages like Java or C,
  you’ve probably been dealing with the absence of values by using the null reference.
  Because null is a proper replacement for every type,
  at the slightest suspicion that an expression might return null,
  we need to write defensive code so that we stay away from the dreaded NullPointerException.

 */
  //TODO : // implementation not important, assume that for some code path the function returns null
def unsafeMethod(arg: Int): String = null
  // defensive code
  val potentialValue = unsafeMethod(44)
  val myRealResult =
    if (potentialValue == null)
      "ERROR"
    else
      potentialValue.toUpperCase()

  /*
TODO
    However, in real life we deal not just with one value, but with many.
    Assume we want to combine two such calls to unsafeMethod.
    How would we do it? Defensively, of course:
   */
  val callToExternalService = unsafeMethod(55)
  val combinedResult =
    if (potentialValue == null)
      if (callToExternalService == null)
        "ERROR 1"
      else
        "ERROR 2"
    else
      if (callToExternalService == null)
        "ERROR 3"
      else
        potentialValue.toUpperCase() + callToExternalService.toUpperCase()

/*
TODO
   The for-comprehension, because it’s expressed in terms of map and flatMap,
   can take care automatically to check whether the Options inside are empty or not,
   and return the appropriate value,
   without us checking everything ourselves. This code offers many benefits:
 */
  val betterCombinedResult: Option[String] = for {
    firstValue <- Option(unsafeMethod(44))
    secondValue <- Option(unsafeMethod(55))
  } yield firstValue.toUpperCase() + secondValue.toUpperCase()

  val finalValue = betterCombinedResult.getOrElse("ERROR")

}
