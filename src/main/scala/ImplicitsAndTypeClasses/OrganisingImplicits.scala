package ImplicitsAndTypeClasses

object OrganisingImplicits  extends App {
//implicit val reversedOrdering :Ordering[Int] = Ordering.fromLessThan(_ > _)

  implicit def normalOrdering :Ordering[Int] =
    Ordering
      .fromLessThan(_ < _)

  println(List(1,2,3).sorted)

/*
TODO
 potential implicit values are :
 - var/val
 -objects
 -accesor method = defs with no paraenthesis

 */

//Exercise

  //ascending order
implicit val ageOrdering :Ordering[Person] =
  Ordering.fromLessThan(
  (a,b) => a.age.compareTo(b.age) < 0)

  // todo: both are same
 // implicit val ageOrdering1: Ordering[Person] = Ordering.by(_.age)


  case class Person(name:String, age:Int)
  //override def apply[A](xs: A*): List[A]
  val personList= List.apply(
    Person("Amy",34),
    Person("John",33),
    Person("Steve",5),
    Person("Bold",2)
  )

  /*
  object Person{
    implicit val alphabeticOrdering :Ordering[Person] = Ordering.fromLessThan(
      (a,b) => a.name.compareTo(b.name) < 0)
  }
  */

//def sorted[B >: A](implicit ord: Ordering[B]): List
  //println(personList.sorted)
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

  //Best Practices
  /*
  If there is only one good value which will be used most of the cases
  then we should define that in the Companion Object of the Entity or case class
  and remaining should be define in local scope or Normal Scope
  But we if have Multiple good implicit values then we should define then in a diffrent pacakge
  or different object and should import them
  i.e as follows or we can put them in different Object as well

   */
/*
object AlphabeticNameOrdering{
  implicit val alphabeticOrdering :Ordering[Person] = Ordering.fromLessThan(
    (a,b) => a.name.compareTo(b.name) < 0)
}
  object AgeOrdering{
    implicit val ageOrdering :Ordering[Person] = Ordering.fromLessThan(
      (a,b) => a.age.compareTo(b.age) < 0)
  }

 */

  println(personList.sorted)

}
