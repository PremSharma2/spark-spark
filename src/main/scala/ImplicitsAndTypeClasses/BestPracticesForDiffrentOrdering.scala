package ImplicitsAndTypeClasses

object BestPracticesForDiffrentOrdering  extends App {

  case class Employee(name:String, age:Int)

// this is the best approach as we discussed that all important sorting
  // logic goes to different singleton object and we can import that out
  object AlphabeticOrdering{
    implicit val alphabeticOrdering :Ordering[Employee] = Ordering.fromLessThan(
      (a,b) => a.name.compareTo(b.name) < 0)
  }
  object AgeOrdering {
    implicit val ageOrdering: Ordering[Employee] = Ordering.fromLessThan(
      (a, b) => a.age.compareTo(b.age) < 0)
  }

  val employees= List.apply(
    Employee("Amy",34),
    Employee("John",33),
    Employee("Steve",5),
    Employee("Bold",2)
  )
  import AlphabeticOrdering.alphabeticOrdering
  println(employees.sorted)
}
