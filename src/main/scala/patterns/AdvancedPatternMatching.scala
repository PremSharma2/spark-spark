package patterns

object AdvancedPatternMatching  extends App {

  val numbers=List(1)
  val numbers1=List(1,2,3,42)
numbers1 match {
  case List(1,_,_,_) => "extractor"
  case List(1,_*) => "List of arbitary length"
  case 1 :: List(_) => "infix pattern"


}
  val description: Unit = numbers match {
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


  //Exercise onPattern Matching

//if we have to match pattern on condition basis
  // then we have to do with custom Singleton object rather than
  /*
  using conditional  statements in match conditions
   */
  val n:Int=45
  // This is not the correct way to pattern match the
  val matchpattern: String = n match {
    case x if x<10 => s"$x is single Digit"
    case x if x%2==0 => s"$x is even number"
    case _ => "no property matched "
  }

  object even {
    def unapply(arg: Int): Option[Boolean] = if (arg % 2 == 0) Some(true) else None
  }

  object Odd {
    def unapply(arg: Int): Option[Int] = if (arg % 2 != 0) Some(arg) else None
  }

  object SingleDigit {
    def unapply(arg: Int): Option[Unit] = if (arg > -10 && arg < 10) Some(()) else None
  }


  // its like if if and else conditions implementation via pattern matching
  val matchpattern1: String = n match {
    case SingleDigit(_) => s" is single Digit : and value returned from unapply is Unit"
    case even(_) => s" is even number"
    case Odd(number) => s"this is odd number : and value returned from unapply is ${number}"
    case _ => "no property matched "
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

  val pf:  PartialFunction[Int, String] = {
    case singledigit1() => s" is single Digit"
    case even1() => s" is even number"
    case _ => "no prpoery"
  }
  val list: List[String] =List(1,2,3,4,5) map(pf)




  val matchPatternLatest: PartialFunction[Int, String] = {
    case SingleDigit(_) => s"is a single digit"
    case even(_) => s"is an even number"
    case Odd(_) => s"is an odd number"
  }

  // Adding a default case to handle inputs not matched by the PartialFunction
  def applyMatchPattern(n: Int): String = matchPatternLatest.applyOrElse(n, (_: Int) => "no property matched")

  // Testing the function
  val testValues = List(3, 4, 10, 23, -5)
  val results = testValues.map(n => s"$n ${applyMatchPattern(n)}")
  results.foreach(println)
  // Output:
  // 3 is a single digit
  // 4 is an even number
  // 10 is an even number
  // 23 is an odd number
  // -5 is a single digit



  //TODO : Real World Use Case

  case class Transaction(id: String, amount: Double, currency: String)

  object HighValueTransaction {
    def unapply(transaction: Transaction): Option[(String, Double)] = {
      if (transaction.amount > 10000) Some((transaction.id, transaction.amount)) else None
    }
  }

  val transaction = Transaction("TX123", 15000.0, "USD")
  val result = transaction match {
    case HighValueTransaction(id, amount) => s"High value transaction: $id with amount $$amount"
    case _ => "Regular transaction"
  }

  println(result)
  // Output: High value transaction: TX123 with amount $15000.0

}
