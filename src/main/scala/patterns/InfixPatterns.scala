package patterns

import patterns.AdvancedPAtternMatching.Person

object InfixPatterns extends App {

  case class Or[A, B](a: A, b: B)

  val either = Or(2, "two")
  val matchPattern = either match {
    case Or(number, string) => s" Number $number is Written As $string"

  }
// both are same but this is called Infix Pattern in Pattern matching
 val matchPattern1= either match {
    case number Or string => s" Number $number is Written As $string"

  }
  println(matchPattern)
  println(matchPattern1)
  /*
  The return type of an unapply should be chosen as follows:

If it is just a test, return a Boolean. For instance case even().
If it returns a single sub-value of type T, return an Option[T].
If you want to return several sub-values T1,...,Tn, group them in an optional tuple Option[(T1,...,Tn)].
Sometimes, the number of values to extract isn’t fixed and we would like to return
an arbitrary number of values,
 depending on the input. For this use case,
 you can define extractors with an unapplySeq method which returns an Option[Seq[T]].
  Common examples of these patterns include deconstructing a List using :
  case List(x, y, z) =>
  and decomposing a String using a regular expression Regex,
  such as case case List(1,_*) => s"starting with 1"
   */

  // Decomposing Sequences using Pattern Match
  //1: vararg Pattern
  val numbers=List(1,2,3,4)
  val vararg= numbers match {
      //Here standard technique for unapply a list will not work
    // here bcz we don't know about the arguments
      // Hence this will not be successful here
    case List(1,_*) => s"starting with 1"
  }

// Now we need a new technique which is called unapply a seq
  abstract class MyList[+A]{
  def head: A= ???
  def tail:MyList[A] = ???
}

  case object Empty extends MyList[Nothing]

  case class Node[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      // this is good example that we can loop through the seq via recursion and transform the
      // output of recursion using map

      else unapplySeq(list.tail).map(seq => list.head +: seq)
  }

  val myList: MyList[Int] = new Node[Int](1, new Node[Int](2, new Node[Int](3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => s"starting With 1 and 2"
    case _ => s"Something Else"
  }
  println(decomposed)

  // Custom Return Types For unapply
  abstract class Wrapper[T] {
    def isEmpty: Boolean

    def get: T

  }

  object personWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false

      override def get: String = person.name
    }
  }

  val bob = new Person("bob", 33)
  println(bob match {
    case personWrapper(n) => s"This Person Name is $n"
    case _ => s"An Allien"
  })
  // Take Away with this exercise is that Unapply pattern return type does not need to be
  // necessarily Option[_] Also
  //But could be  anything Which implements these two below given  Methods Like Wrapper[T]
  //  which implements these two methods
  /*
   override def isEmpty: Boolean = false

      override def get: String = person.name
   */
}
