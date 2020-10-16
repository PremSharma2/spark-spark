package patterns

object AdvancedPatternMatching  extends App {

  val numbers=List(1)
  val numbers1=List(1,2)

  val description= numbers match {
      // This is called :: Infix Pattern
      // here tail is EmptyList
    case head ::  Nil => println(s"the only element is head $head ")

  }

  val description1: Unit = numbers1 match {
    // This is called :: Infix Pattern
    case head ::  tail => println(s"the only element is head $head  and tail is ${tail.head}")

  }
  println(description1)
 // to understand above pattren match we need to understand the below fillowing mentioned code

  class Person(val name:String, val age:Int)
  // as this is not case class so it will not available for pattern match
  object Person{

    def unapply(person:Person): Option[(String,Int)]= Some((person.name,person.age))

    def unapply(age: Int): Option[String] = Some(if (age <21) "minor" else "major")
  }
  val bob= new Person("bob",33)

  val greeting: String = bob match {
      //Here compiler is looking for unapply method with name
    // Person with return type Option[(String,Int)]
    case Person(name,age) => s"Hi my name is $name and my age is $age"
    case _ => "Not matchable Person"
  }

  val legalStatus: String = bob.age match{
    case Person(status) =>s"My legal status is $status"

  }
  println(greeting)
  println(legalStatus)


  //Exercise onPattern MAtching

//if we have to match pattern on condition basis
  // then we have to do with custom Singleton object rather than
  /*
  using conditional  statements in match condtions
   */
  val n:Int=45
  // This is not the correct way to pattern match the
  val matchpattern: String = n match {
    case x if x<10 => s"$x is single Digit"
    case x if x%2==0 => s"$x is even number"
    case _ => "no prpoery"
  }
  
  object even{
    def unapply(arg: Int): Option[Boolean] = if (arg%2==0) Some(true) else None
  }

  object singledigit{
    def unapply(arg: Int): Option[Boolean] = if (arg > -10 && arg<10 ) Some(true) else None
  }
// its like if if and else conditions implementation via pattern matching
  val matchpattern1: String = n match {
    case singledigit(myflag) => s" is single Digit : and value returned from unapply is $myflag"
    case even(_) => s" is even number"
    case _ => "no prpoery"
  }
  println(matchpattern)
  println(matchpattern1)

  object even1{

    def unapply(arg: Int): Boolean = arg%2==0
  }

  object singledigit1{
    def unapply(arg: Int): Boolean = arg > -10 && arg<10
  }
// Here we have removed the argument parameter
// from pattern match because here return of unapply method
  // is Boolean not the Option[Boolean]
  val matchWithOutOption=48
  val matchable2: String = matchWithOutOption match {
    case singledigit1() => s" is single Digit"
    case even1() => s" is even number"
    case _ => "no prpoery"
  }
  println(matchable2)
}
