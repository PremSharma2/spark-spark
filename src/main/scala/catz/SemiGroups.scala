package catz

import cats.Semigroup
// TODO : -> Semigroup is a type class that combines the Element of same type
// and what it combines that depends on type class instance
/*
TODO : SemiGroup Type class Looks like that

trait Semigroup[@sp(Int, Long, Float, Double) A] {

 Associative operation taking which combines two values.

  def combine(x: A, y: A): A
 */

//lets import the type class
object SemiGroups  extends App {
  // TODO lets import type class instances for Int type
  import cats.instances.int._
  // naturalIntSemiGroupTypeClassInstance is an type class instance
  val naturalIntSemiGroupTypeClassInstance: Semigroup[Int] = Semigroup.apply[Int]

  //TODO calling combine operation on type class instance

  val combineNaturals: Int = naturalIntSemiGroupTypeClassInstance.combine(2,46)
  println(combineNaturals)

  // naturalStringSemiGroupTypeClassInstance is an type class instance
  // TODO import type class instance for the String type
  import cats.instances.string._
  val naturalStringSemiGroupTypeClassInstance: Semigroup[String] = Semigroup.apply[String]
  val combineNaturalStrings= naturalStringSemiGroupTypeClassInstance.combine("Jai","Maa")
  println(combineNaturalStrings)

  // TODO --------------------------------------------------------------------------------

  // TODO : -> use of Semigroup type class we created an API
  //TODO : -> this we can enhance by converting into
  // type Enrichment by making it implicit class
  //TODO : -> General APi for Reduction of any type using type class

  def reduceThings[T](list:List[T])(implicit semiGroup:Semigroup[T]) = {
    list.reduce(semiGroup combine)
    //
  }

// TODO : -> These are specific APIS over here we dont require combine function

val fx : (Int,Int) => Int = (x,y) => x+ y

  def   reduceInts(list:List[Int]) = list.reduce(fx)

  def   reduceStrings(list:List[String]) = list.reduce(_ + _)

  def sum (x:Int,y:Int): Int= x+y

  List(1,2,3,4).reduce(sum)

    print(reduceInts(List(1,2,3,4)))

  // TODO : -> by using Reduce things API

  val myList= (1 to 4).toList

  println(reduceThings(myList))

  // TODO : -> converting List[Int] to List[Option[Int]]

  val numberOptions: List[Option[Int]] = myList.map(Option(_)).toList

  // TODO :-> importing the implicit  Semigroup type class instance of Type Option[Int]
  import cats.instances.option._
  // TODO : -> it will sum all elements of List  and wrap them in Monad Option
  println(reduceThings(numberOptions))
val strings= List("I am" , "starting" ,"to like " , "SemiGroup")

  val stringOption: List[Option[String]] = strings.map(Option(_)).toList
  println(reduceThings(stringOption))// It will concat the list and wrap them Option Monad
  //TODO --------------------------------------------------------------------------------------
  // TODO 1: Exercise make this api to support for Custom Types

  case class Expense(id:Long, amount:Double)
  // TODO : creating the  Type class Instance for Expense Type which is custom Type Expense
  // TODO and marking it implicit
  implicit val typeClassInstanceExpense = Semigroup.instance[Expense]{
    (e1,e2) => Expense(Math.max(e1.id,e2.id), e1.amount + e2.amount)
  }

  val expenses= List(Expense(1,60),Expense(2,70), Expense(3,80), Expense(4,90))

  println(reduceThings(expenses))

  //TODO : -> USe of type Enrichment or Extension Methods from Semi Group , or pimping
  // lets import the correct package
  import cats.implicits.catsSyntaxSemigroup
  /*
  TODO : type Enrichmet by this implcit def catsSyntaxSemigroup
   TODO : It will inject the desired implicit type class instance of type Class SemiGroup
 TODO : i.e 2 will be converted into val enrichment= SemigroupOps(implicit typeclassinstance)(2)
  TODO : then  compiler will call enrichment.|+|(3), hence it is implicitly enriched
  trait SemigroupSyntax {

  implicit final def catsSyntaxSemigroup[A: Semigroup](a: A): SemigroupOps[A] =
    new SemigroupOps[A](a)
}

final class SemigroupOps[A: Semigroup](lhs: A) {
  def |+|(rhs: A): A = macro Ops.binop[A, A]
  def combine(rhs: A): A = macro Ops.binop[A, A]
  def combineN(rhs: Int): A = macro Ops.binop[A, A]
}
   */
  val anIntsumUsingTypeEnrichMent = 2 |+| 3

  val anStringConcatUsingTypeEnrichMent = "JAI" |+| "Mata"

  println(anIntsumUsingTypeEnrichMent)
  println(anStringConcatUsingTypeEnrichMent)
  val aCombinedExpense: Expense = Expense(1,60) |+| Expense(2,40)
  println(aCombinedExpense)
  // TODO: implement reduce things API with |+|
  //TODO : here we passed Lambda combiner function fx : (x,y) => x |+| y
  //TODO : here x is implcitly converted to implict class refrence
  //TODO on top of that refrence we will call |+| method
  def reduceThings1[T](list:List[T])(implicit semiGroup:Semigroup[T]) = {
    list.reduce(_ |+| _)
  }

println(reduceThings1(expenses))

}
