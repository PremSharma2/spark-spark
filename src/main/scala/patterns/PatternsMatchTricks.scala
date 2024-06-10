package patterns

import patterns.Patterns.Person
import tailRecursiveList.ListProblems.{Node, RList, RNil}

import scala.annotation.tailrec

object PatternsMatchTricks extends App {

  val numbersList= List(1,2,3,4,42)
  val mustHavethree= numbersList match {
    case List(_,_,3,_) => "List has 3rd element 3 "
    case _ => " "
  }

  val startsWithOne= numbersList match {
    case 1 :: tail => s"List Starts with 1 and tail is $tail"
  }
  // TODO This pattern for list is very imp

  def process(aList: List[Int]): String ={

    val matchedresult: String =aList match {
      case Nil => "List Is Empty we should return Nil from here rather string"
      case head :: tail => s"list starts with $head,tail is $tail"
    }

    matchedresult
  }

  val donotCareAboutTheRest = numbersList match {
    case List(_,2,_*) => "I only care about the second element in the list"
  }
  // case List(1,2,_) :+42 this patterns suggest that 42 sits at the End of the List
  val mustendWithMeaningOfLife= numbersList match {
    case List(1,2,_) :+42 => "List starts with 1 and ends with 42 "
  }
  //  case List(1,_*) :+42 it is a combination of var and infix pattern
  val mustEndWithMeaningOfLife = numbersList match {
    case List(1,_*) :+42 => "I dont care the how long the list is" +
      " I just want it should end with 42 "
  }
  // match a type not value in pattern match

  def gimmeAvalue():Any =45
  //  case _: String  This pattern match is for Type Check
  val gimmeAType = gimmeAvalue() match {
    case _: String => "String is returned from the gimmeValueMethod"
  }

  def requestMoreInfo(p:Person):String = s"The Person is  ${p.name}"
  val bob = Person.apply("Bob", 20)
  val bobInfo = bob match {
    case Person(name, age) => s"$name info : ${requestMoreInfo(Person(name,age)) }"

  }
  // if you want to bind the bob to some parameter s that we can use later
// person object is kept in p now so need create new object of person
  val bobInfo1: String = bob match {
    case p @ Person(name, _) => s"$name info : ${requestMoreInfo(p) }"

  }
  // This is powerful pattern matching as it is
  // as it matches the condition in pattern match

val aNumber = 42
val ordinalNumber=aNumber match {
  case 1 => "first"
  case 2 => "second "
  case 3 => "third"
  case n if n%10==1 => n + "st"
  case n if n%10==2 => n + "d"
  case n if n%10==1 => n + "rd"
}
// alternative pattern i.e i
  // if we want list should contain either
  // of this value at particular position or that value

  val myOptimalList= numbersList match {
    case List(1,_*) => "I like this List"
    case List(_,_,3,_*) => "I like this List"
    case _ => "I hate This List"

  }


val mylist= 1::2::3::4::RNil
  mylist match {
    case RNil | Node(0,_) => " compound pattern"
  }


  // TODO if guards
  mylist match {
    case Node(head,Node(specailSecondElement,_)) if (specailSecondElement%2==0) =>
  }

  numbersList match {
    case listOfStrings:List[String] => "a list of strings "
    case listOfInt:List[Int] => "a list of Integer "
    case _ =>
  }
// Nice way to do this or optimized way to this
/*
  val myOptimizedPatternMatch= numbersList match {
    case List(1,_*) | case List(_,_,3,_*) => "I like this List"
    case _ => "I hate This List"

  }


 */

  //---------------pattern match Exercise List-----------------------------
//Note: all Seq or List related exercise will be done via infix pattern

  def incrementByOne(s: Seq[Int]): Seq[Int] = {
   val result= s match {
      case Nil => Nil
      case _ => incrementRec(s.reverse).reverse
    }
    result
  }

  // Seq(3,2,1) - 4,2,1
  //Seq(9, 9, 9) - (1,0,0,0)
  def incrementRec(seq: Seq[Int]): Seq[Int] = {
   val tempResult: Seq[Int] = seq match {
      case Nil => Seq(1)
      case head :: tail if head < 9 => Seq(head + 1) ++ tail
      // Seq(4) ++ Seq(2,1)
      case _ :: tail => Seq(0) ++ incrementRec(tail) // Seq(0) ++ Seq(0) ++ Seq(0) ++ Seq(1)
    }
    tempResult
  }

  // Todo tail rec implementation
  def incrementRec1(seq: RList[Int]): RList[Int]= {
    @tailrec
    def tailRecIncr(remaining: RList[Int], accumlator: RList[Int]): RList[Int] = {
      if (remaining.isEmpty) accumlator
      else if (remaining.head == 9 && remaining.tail.isEmpty) 1 :: 0 :: accumlator
      else if (remaining.head < 9) tailRecIncr(remaining.tail, remaining.head + 1 :: accumlator)
      else tailRecIncr(remaining.tail, 0 :: accumlator)
    }
    tailRecIncr(seq,RNil)
  }

  // pattern match exercise

  def listToString(list: List[String]): String = list match {
    case head :: tail => head + " " + listToString(tail)
    case Nil => ""
  }
  // pattern match Exercise 3: Largest size element in List
   @tailrec
  def max(xs: List[Int]): Option[Int] = xs match {
    case Nil => None
    case List(x: Int) => Some(x)
    case firstHead :: secondElement :: tail => max( (if (firstHead > secondElement) firstHead else secondElement) :: tail )

  }


  def sum(list: List[Int]): Int = list match {
    case Nil => 0
    case n :: rest => n + sum(rest)
  }


  def multiply(list: List[Int]): Int = list match {
    case Nil => 1
    case n :: rest => n * multiply(rest)
  }
}

