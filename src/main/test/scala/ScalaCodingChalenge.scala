import PriceBasket.{breads, soups}

import scala.math.min

object ScalaCodingChalenge extends App {

/*


2. Given two functions f1 and f2, implement f3 by composing f1 and f2
val f1: (Int, Int) => Int = (a, b) => a + b
val f2: Int => String = _.toString
val f3: (Int, Int) => String = ???
 */

  // Q2
  val f1: (Int, Int) => Int = (a, b) => a + b
  val f2: Int => String = _.toString
  val f3: (Int, Int) => String = (x,y) => f2 (f1(x,y))
  println(f3.apply(10,20))

  /*
which represents the number 123, write a function to increment it by one without converting
types. Your function should produce the expected result for the
following test cases:
Nil => Nil
Seq(0) => Seq(1)
Seq(1, 2, 3) => Seq(1, 2, 4)
Seq(9, 9, 9) => Seq(1, 0, 0, 0)*/

  // Q3
  def incrementByOne(s:Seq[Int]): Seq[Int] = {
    s match {
      case Nil => Nil
      case _ => incrementRec(s.reverse).reverse
    }
  }

  // Seq(3,2,1) - 4,2,1
  def incrementRec(seq: Seq[Int]): Seq[Int] = {
    seq match {
      case Nil => Seq(1)
      case head :: tail if head < 9 => Seq(head + 1) ++ tail
      case _ :: tail => Seq(0) ++ incrementRec(tail) // Seq(0) + Seq(0) + Seq(0) + Seq(1)
    }
  }

  println(incrementByOne(Nil))
  println(incrementByOne(Seq(0)))
  println(incrementByOne(Seq(1,2,3)))
  println(incrementByOne(Seq(9,9,9)))
  val seq: Seq[Int] =(1 to min(2, 6 / 2)).toSeq

  /* Q7
  for an address book? What endpoints will it have (feel free to provide sample curl requests)?
  How would you handle errors?

  POST /addressbook/ to create new address book entry.
  GET /addressbook/<id>/ to retrieve specific address book entry.
  PUT /addressbook/<id>/ to update specific address book entry.
  DELETE /addressbook/<id>/ to delete specific address book entry.

  curl -v -X POST http://localhost:8080/addressbook -H "Content-Type: application/json" -d '{"id":
    123, "name":"Prem", "email":"prem.kaushik@_.co.uk", "adddress" : "UK"}'

  curl -v -X GET http://localhost:8080/addressbook/123

  curl -v -X GET http://localhost:8080/addressbook/1000
  {"error" : "Customer with id=1000 does not exist"}

  curl -v -X PUT http://localhost:8080/addressbook/123

  curl -v -X DELETE http://localhost:8080/addressbook/123

  I would perform error handling based on the response code say 200,201,404,customized response codes
  */

  /* 5. Explain what the following code means:
  trait MyAlg[F[_]] {
    def insertItSomewhere(someInt: Int): F[Unit]
    def doSomething(someInt: Int): F[Int]
  }

  We abstracted over all the first order types with one hole, we can define common functions like insertItSomewhere, doSomething between all of them
  You can mentally replace F by List or Option or any other first-order types (Monads)
  It allows us to define functions across a lot of different types in a concise way




  6. Given the trait in Q5,
  create a class `MyProg` abstract in type F that has MyAlg passed to it.
  Implement the following method in the class:
  def checkThenAddIt(someInt: Int) = ???
  It should pass the result of `doSomething` to `insertItSomewhere`.
  Feel free to add external imp

  abstract class MyProg[F[_]] extends MyAlg {
    def checkThenAddIt(someInt: Int) = doSomething(someInt).map(int_value => insertItSomewhere(int_value))
  }

  Q4. Given the following function:
    def f[A](a: A): Future[A]
    Write a function `g` that safely handles calling f. The return type of `g` should be such that
    when f succeeds, g returns something very similar. Feel free to import an external library for
    the return type of g.

    def g[B](b:B):Try[B] = Try{
      f(b)
    }
  */

//Q6:
   import cats.FlatMap
  import cats.implicits._
  trait MyAlg[F[_]] {
    def insertItSomewhere(someInt: Int): F[Unit]
    def doSomething(someInt: Int): F[Int]
  }

  abstract class MyProg[F[_]: FlatMap] extends MyAlg[F] {
    def checkThenAddIt(someInt: Int) = doSomething(someInt).flatMap(int_value => insertItSomewhere(int_value))
  }


}
