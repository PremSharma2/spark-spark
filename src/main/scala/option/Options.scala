package option

import scala.util.control.Exception.allCatch

/*
 * use of options and handling failures
 *
 *
 */
object Options extends App {

  val myfirstOption: Option[Int] = Some(4)
  val noOption: Option[Int] = None
  val orElse: Int = myfirstOption.getOrElse(3)
  println(myfirstOption.getOrElse("3"))
  println(myfirstOption.get)
  println(noOption getOrElse(3))
  def unsafe(): String = null
  //An Option factory which creates Some(x) if the argument is not null,
  // and None if it is null.
  val result: Option[String] = Option.apply(unsafe)
  // similar to above but
  val res: Option[String] =allCatch.opt(unsafe)
  //println(result.get)
  // chained methods
  def backupMethod: String = "A valid Result"
  val chainedresult: Option[String] = Option(unsafe) orElse(Option(backupMethod))
  println(chainedresult.get)
  // design safe api using syntatic sugar
  def betterUnsafeMethod(): Option[String] = None
  def betterBackUpMethod(): Option[String] = Some("Valid result")
  val betterchianedResult: Option[String] = betterUnsafeMethod orElse betterBackUpMethod
    val betterSafeFunction: () => Option[String] = betterBackUpMethod _
  betterSafeFunction.apply()
   val betterResult: Option[String] =
     betterUnsafeMethod orElse betterBackUpMethod
  val getOrelse=betterUnsafeMethod() getOrElse("Backup-Result-GetOrElse")
  println(betterUnsafeMethod() getOrElse("Backup-Result-GetOrElse"))
  println(betterUnsafeMethod)
  println(betterchianedResult.get)
  //function
  println(myfirstOption.isEmpty)
  println(myfirstOption.get)
  //map,filter,flatMap option monad
  println(myfirstOption.map(_ * 2))
  //Returns this scala.Option
  // if it is nonempty and applying the predicate p to this scala
  //Option's value returns true. Otherwise, return None. 
  println(myfirstOption.filter(x => x > 2))
  val transformX : Int => Int = (x) => x*10
  // ETW pattern
  println(myfirstOption.flatMap(x => Option(transformX(x))))
  val emptyOption : Option[Int] = Some(0)
  // As Option is monad so it implements ETW pattern
  println(noOption.flatMap(x => Option(transformX(x))))
  // pattern match in Option
/*
 Mapping over option depending on condition:
Returns a scala.Some containing the result of applying pf to this
scala.Option's contained value,
if this option is nonempty and partial function is defined for that value.
4


I have two vals, a condition and an option.
 Note that condition is a simple boolean, not depending on the option's value.
 i.e some external condition
 */
  val condition = true
  val optionMatch: Option[Int] = myfirstOption.collect {
    case x if x%2==0 => transformX(x)
  }.orElse(None)
}