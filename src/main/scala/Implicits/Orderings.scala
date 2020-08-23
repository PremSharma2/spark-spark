package Implicits

import Implicits.OrganisingImplicits.Person

trait Orderings {


  object AlphabeticNameOrdering{
    implicit val alphabeticOrdering :Ordering[Person] = Ordering.fromLessThan(
      (a,b) => a.name.compareTo(b.name) < 0)
  }
  object AgeOrdering{
    implicit val ageOrdering :Ordering[Person] = Ordering.fromLessThan(
      (a,b) => a.age.compareTo(b.age) < 0)
  }

}
