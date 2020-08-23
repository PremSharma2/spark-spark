package Implicits

object OrganisingImplicits  extends App {
//implicit val reversedOrdering :Ordering[Int] = Ordering.fromLessThan(_ > _)
//implicit val alphabeticOrdering :Ordering[Person] = Ordering.fromLessThan(
 // (a,b) => a.name.compareTo(b.name) < 0)
  implicit def normalOrdering :Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1,2,3).sorted)
/*
potential implicit values are :
- var/val
-objects
-accesor method = defs with no paraenthesis

 */
//Exercise
  case class Person(name:String, age:Int)
  //override def apply[A](xs: A*): List[A]
  val personList= List.apply(
    Person("Prem",34),
    Person("Veeru",33),
    Person("Gana",5),
    Person("Chaitanya",2)
  )
  object Person{
    implicit val alphabeticOrdering :Ordering[Person] = Ordering.fromLessThan(
      (a,b) => a.name.compareTo(b.name) < 0)
  }
//def sorted[B >: A](implicit ord: Ordering[B]): List
  println(personList.sorted)
  /*
  Implicit Scope
 a - Normal Scope =Local Scope i.e locally will be highest priorty
 b - imported Scope
 c - companions of all type involved in method signature
  for eg
  Precedence of Search: for compiler i.e a,b and c
  for c here is the Explanation:
  sorted[B >: A](implicit ord: Ordering[B]): List
  in this Compiler will look into the List trait
  and then Ordering[B] companion object
  which is there in the method signature
  and at last in all the types involved in method signature
  i.e A and B [B >: A] or any of there Super Type
   */

}
