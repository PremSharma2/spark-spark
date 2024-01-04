package grades

object Grades {


  sealed trait Grades

  case object A extends Grades

  case object B extends Grades

  case object C extends Grades

  case object FAIL extends Grades


}
