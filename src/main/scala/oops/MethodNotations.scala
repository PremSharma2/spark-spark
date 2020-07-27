package oops

object MethodNotations extends App {

  class Person(val name: String, val favMovie: String, val age: Int = 0) {
    def likes(movie: String): Boolean = movie == this.favMovie
    def hangoutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"
    // Lets use uniary_ prefix tomake a method a in scala for object
    def unary_! : String = s"$name , what the heck?!"
    def unary_+ : Person = new Person(this.name, this.favMovie, age + 1)
    def isAlive: Boolean = true
    def apply: String = s"hi my name is $name,I like $favMovie"
    def + (nickName: String): Person = new Person(s"$name ($nickName)", favMovie)
    def learns(thing: String): String = s"$name is learning $thing"
    def learnsScala: String = this learns "Scala"
    def apply(n: Int): String = s"$name watches $favMovie $n times"
  }
  val mary = new Person("mary", "Inception")
  println(mary.likes("Inception"))
  //Infix notation
  //this is called infixnotation or operator notation or syntactic sugar and it only works with method having single parameter
  println(mary likes "Inception")

  val tom = new Person("Tom", "FightClub")
  println(mary hangoutWith tom)
  // all operators in scala are methods
  // scala supports immutability i.e simple mathematics scala supports z=x+y and z is always new value
  val a= 1.+(2)
  println(a)
  
  //uniary_ prefix operations in scala
  val x = -1 // equivalet with 1.uniary_-
  val y = 1.unary_-
  val z= 1.unary_+
  //uniary_ prefix only works with - + ~ !
  println(mary unary_!)
  //both are same
  println(!mary)
  val c= mary.unary_+.age
  println(c)
  //post fix notation works when the method does not have any argument
  println(mary.isAlive)
  //or
  println(mary isAlive)
  // to call apply method
  println(mary)
  println(mary apply)
  println((mary + "the rockstar" apply))

  
  //postfix notation
  println(mary learnsScala)
  println(mary(10))
}