package catz

import cats.Semigroup
//Semigroup is a type class that combines the Element of same type
// and what it combines that depends on type class instance
/*
SemiGroup Type class Looks like that
trait Semigroup[@sp(Int, Long, Float, Double) A] {

 Associative operation taking which combines two values.

  def combine(x: A, y: A): A
 */

//lets import the type class
object SemiGroups  extends App {
  // lets import type class instances
  import cats.instances.int._
  // naturalIntSemiGroupTypeClassInstance is an type class instance
  val naturalIntSemiGroupTypeClassInstance: Semigroup[Int] = Semigroup.apply[Int]
  val combineNaturals: Int = naturalIntSemiGroupTypeClassInstance.combine(2,46)
  println(combineNaturals)

  // naturalStringSemiGroupTypeClassInstance is an type class instance
  // import type class instance
  import cats.instances.string._
  val naturalStringSemiGroupTypeClassInstance: Semigroup[String] = Semigroup.apply[String]
  val combineNaturalStrings= naturalStringSemiGroupTypeClassInstance.combine("Jai","Maa")
  println(combineNaturalStrings)


  // TODO : -> use of Semigroup type class we created an API
  //TODO : -> this we can enhance by converting into type Enrichment by making it implicit class
  //TODO : -> General APi for Reduction of any type using type class

  def reduceThings[T](list:List[T])(implicit semiGroup:Semigroup[T]) = {
    list.reduce(semiGroup.combine)
    //
  }

// TODO : -> These are specific APIS over here we dont require combine function

val fx : (Int,Int) => Int = (x,y) => x+ y
  def   reduceInts(list:List[Int]) = list.reduce(fx)

  def   reduceStrings(list:List[String]) = list.reduce(_ + _)
  def sum (x:Int,y:Int): Int= x+y
  List(1,2,3,4).reduce(sum)
    print(reduceInts(List(1,2,3,4)))
  val myList= (1 to 4).toList
  println(reduceThings(myList))
  val numberOptions: List[Option[Int]] = myList.map(Option(_)).toList
  // importing the Semigroup type class instance of Type Option[Int]
  import cats.instances.option._
  println(reduceThings(numberOptions))// it will sum all elements and wrap them in Monad Option
val strings= List("I am" , "starting" ,"to like " , "SemiGroup")

  val stringOption: List[Option[String]] = strings.map(Option(_)).toList
  println(reduceThings(stringOption))// It will concat the list and wrap them Option Monad
  //TODO --------------------------------------------------------------------------------------
  // TODO 1: Exercise make this api to support for Custom Types This Impl

  case class Expense(id:Long, amount:Double)
  // TODO : create your Type class Instance for Expense Type which is custom Type Expense
  implicit val typeClassInstanceExpense = Semigroup.instance[Expense]{
    (e1,e2) => Expense(Math.max(e1.id,e2.id), e1.amount + e2.amount)
  }

}
