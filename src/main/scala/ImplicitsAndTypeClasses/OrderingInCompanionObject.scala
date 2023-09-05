package ImplicitsAndTypeClasses

object OrderingInCompanionObject  extends App {

  case class Emp(name:String, age:Int)
  val personList= List.apply(
    Emp("Amy",34),
    Emp("John",33),
    Emp("Steve",5),
    Emp("Bold",2)
  )

  object Emp{
    implicit val alphabeticOrdering :Ordering[Emp] = Ordering.fromLessThan(
      (a,b) => a.name.compareTo(b.name) < 0)
  }
  println(personList.sorted)
}
