package option

import java.util.Random
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
  //An Option factory which creates Some(x) if the argument is not null, and None if it is null.
  val result: Option[String] = Option.apply(unsafe)
  //println(result.get)
  // chained methods
  def backupMethod: String = "A valid Result"
  val chainedresult: Option[String] = Option(unsafe()).orElse(Option(backupMethod))
  println(chainedresult.get)
  // design safe api using syntatic sugar
  def betterUnsafeMethod(): Option[String] = None
  def betterBackUpMethod(): Option[String] = Some("Valid result")
  val betterchianedResult: Option[String] = betterUnsafeMethod orElse betterBackUpMethod
  val getOrelse=betterUnsafeMethod() getOrElse("Backup-Result-GetOrElse")
  println(betterUnsafeMethod() getOrElse("Backup-Result-GetOrElse"))
  println(betterUnsafeMethod())
  println(betterchianedResult.get)
  //function
  println(myfirstOption.isEmpty)
  println(myfirstOption.get)
  //map,filter,flatMap
  println(myfirstOption.map(_ * 2))
  //Returns this scala.Option if it is nonempty and applying the predicate p to this scala
  //Option's value returns true. Otherwise, return None. 
  println(myfirstOption.filter(x => x > 2))
  println(myfirstOption.flatMap(x => Option(x * 10)))
  val emptyOption : Option[Int] = Some(0)
  println(noOption.flatMap(x => Option(x * 10)))
}