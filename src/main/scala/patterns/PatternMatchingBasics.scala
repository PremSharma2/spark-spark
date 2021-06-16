package patterns

import scala.util.Random

object PatternMatchingBasics {
val random= new Random
  val x= random.nextInt(10)
  // TODO as we know that every thing in scala is Expression
  //  so this is called pattern match expression
  //TODO value of this expression is bind to variable called description
  //TODO patterns here are represented by the case statements
  val description = x match {
    case 1 => "number one"
    case 2 => "number 2"
    case 3 => "number 3"
    case _ => "Wild card "
  }

  /*
  TODO
      First property of pattern matching is the decomposing the values using case class
   */
  case class Person(name:String,age:Int)

  /*
  TODO Pattern matching with Algebraic data types
   */
sealed trait Animal
  case class  Dog(breed:String) extends Animal
  case class  Cat(breed:String) extends Animal
  def main(args: Array[String]): Unit = {
  println(x)
  println(description)
  val bob = Person("bob",23)
    val greeting = bob match {
      case Person(name, age) => s"hi my name is $name and my age is $age years old"
      case _ => "I dont know who i am"
    }


// TODO if age<21 this is called guard
    val guardedGreeting = bob match {
      case Person(name, age) if age<21  => s"hi my name is $name and my age is $age years old"
      case _ => "I don't know who i am"
    }
    println(greeting)
  }
val animal :Animal = Dog("Rotwoiler")
  animal match {
    case Dog(breed) => s"This breed is $breed"
  }
}
