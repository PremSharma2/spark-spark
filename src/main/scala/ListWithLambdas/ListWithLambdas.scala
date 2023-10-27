package ListWithLambdas

import scala.annotation.tailrec

/*
 *
 *TODO
 * List implementation with Functional Programming Flavour,
 * with Transformer and predicate replacing the inbuilt functional interfaces
 * we will use functional Types
 */

trait MyList[+A] {
  /*
   *TODO
   * head: first element of list
   * tail: remainder of list
   * isEmptyList:Is this list empty
   * addElement(int)=> return new List with this element addElemented
   * tostring:string representation of the list
   */

  def head: A

  def tail: MyList[A]

  def isEmptyList: Boolean

  def addElement[B >: A](element: B): MyList[B]

  def printListElements: String

  override def toString: String = "[" + printListElements + "]"

  //these are called higher order functions
  def map[B](transformer: A => B): MyList[B]

  def flatMap[B](transformer: A => MyList[B]): MyList[B]

  def filter(predicate: A => Boolean): MyList[A]

  def ++ [B >: A](list: MyList[B]): MyList[B]

  //hofs
  def foreach(function: A => Unit): Unit

  def sort(comparator: (A, A) => Int): MyList[A]

  def zip[B, C](list: MyList[B], zipfunction: (A, B) => C): MyList[C]

  def fold[B](start: B)(operator: (B, A) => B): B

  def sum[B >: A](implicit num: Numeric[B]): B
}

/*
 *
 * No one can create such object like Nothing in Scala

  Basically it is used to,
TODO
 Give signal of abnormal termination, like while throwing an Error from the application
  To denote empty Collection
  so as nothing substitute for anything in scala similar
  *  ways Nil represents Singleton object  substitutes to all EmptyList Collection
  * case object i have used for pattern matching
 */
case object Nil extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: MyList[Nothing] = throw new NoSuchElementException

  def isEmptyList: Boolean = true

  def addElement[B >: Nothing](element: B): MyList[B] =  Node(element, Nil)

  def printListElements: String = ""

  def map[B](transformer: Nothing => B): MyList[B] = Nil

  def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Nil

  def filter(predicate: Nothing => Boolean): MyList[Nothing] = Nil

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

  //hofs
  def foreach(function: Nothing => Unit): Unit = ()

  def sort(comparator: (Nothing, Nothing) => Int): MyList[Nothing] = Nil

  def zip[B, C](list: MyList[B], zipfunction: (Nothing, B) => C): MyList[C] =
    if (!list.isEmptyList) throw new RuntimeException("List do not have the same length")
    else Nil

  def fold[B](start: B)(operator: (B, Nothing) => B): B = start

  override def sum[B >: Nothing](implicit num: Numeric[B]): B = num.zero
}

case class Node[+A](override val head: A, override val tail: MyList[A]) extends MyList[A] {
  def isEmptyList: Boolean = false

  def addElement[B >: A](element: B): MyList[B] =  Node(element, this)

  def printListElements: String = {
    if (tail.isEmptyList) "" + head
    else {
      head + "," + tail.printListElements
    }
  }

  /*
     *
   *
   * [1,2,3].filter(n%2 ==0) =
   * [2,3].filter(n%2==0)=
   * new Node(2,[3].filter(n%2==0))=
   * = new new Node(2,EmptyList.filter(n%2==0)
   * =new Node(2,EmptyList)
   */
  /*here no need to heck the compiler bcz
   we are using Function1 which is already have input arguent as contravarient
    Here as we can see that -T1 as contravarient
  trait Function1[ -T1,+R]

   *
   */
  def filter(predicate: A => Boolean): MyList[A] = {
    if (predicate.apply(this.head)) Node(head, tail.filter(predicate))
    else tail.filter(predicate)

  }

  /*
   * [1,2,3].map(n*2)
   * =new Node(2, [2,3].map(n*2)))
   *
   * =new Node(2, new Node(4, [3].map(n*2)))
   * =new Node(2, new Node(4, new Node(6,EmptyList.map(n*2)))
   *
   * =new Node(2, new Node(4, new Node(6,EmptyList)))
   *
   *Also
   * here no need to heck the compiler bcz we are using Function1 which is already have input arguent as contravarient
    Here as we can see that -T1 as contravarient
    trait Function1[ -T1,+R]
   *
   */
  def map[B](transformer: A => B): MyList[B] = {
    Node(transformer.apply(head), tail.map(transformer))
  }

  /*
   * [1,2] + [3,4,5]
   * =new Node(1, [2] + [3,4,5])
   * =new Node (1, new Node(2,EmptyList + [3,4,5]))
   * =new Node(1, new cons(2, [3,4,5])
   * or
   * =new cons(1,new Node(2,new Node(3,new Node(4,new Node(5,EmptyList)))))
   *
   *
   */
  def ++ [B >: A](list: MyList[B]): MyList[B] = Node(head, this.tail ++ list)

  /*for eg here let say transformer take int and returns List[Int] i.e a role of flatmap it flatens
   * [1,2].flatMap (n => [n,n+1])
   * [1,2] + [2].flatMap(n => [n,n+1])
   * [1,2] + [2,3]
   * [1,2] + [2,3] + EmptyList.flatMap(n => [n,n+1])
   * [1,2] + [2,3] + EmptyList
   * [1,2,2,3]
   *
   *
   *
   */

  def flatMap[B](transformer: A => MyList[B]): MyList[B] = {
    transformer.apply(head) ++ tail.flatMap(transformer)
  }

  //hofs
  def foreach(function: A => Unit): Unit = {
    function.apply(head)
    // recursive call for looping
    tail.foreach(function)
  }

  //hof
  /*args=1 ,Empty
   * new Node(1,Empty)
   * args =2,[1]
   * new Node(1,Node(2,Empty))
   *
   * args =3 , [1,2]
   * new Node(2,new Node(3,Empty)))
   *
   *
   *
   */


  /*
   * Here also no need to manage the generic stuff bcz we are using Function which takes two argumet and both are at Contravarient
   * trait Function2[-T1, -T2, +R] extends AnyRef
   *
   * eg:
   *  object Main extends Application {
   val max = (x: Int, y: Int) => if (x < y) y else x

   val anonfun2 = new Function2[Int, Int, Int] {
     def apply(x: Int, y: Int): Int = if (x < y) y else x
   }
   assert(max(0, 1) == anonfun2(0, 1))
 }
   */
  /*
  3 [] = [3]
  2[3] = [2,3]

  4 [2,3] = [2,3,4]




   */
  def sort(comparator: (A, A) => Int): MyList[A] = {
    println("Inside sort")

    def insert(x: A, sortedList: MyList[A]): MyList[A] = {
      println("inside insert")
      println(" value of x is" + x + "\t" + "value of sortedList" + sortedList)
      //println("value of x is := " + x)
      if (sortedList.isEmptyList) Node(x, Nil)
      else if (comparator.apply(x, sortedList.head) <= 0) Node(x, sortedList)
      else Node(sortedList.head, insert(x, sortedList.tail))
    }

    println("head is " + head + "\t" + "tail is" + tail)
    val sortedTail = this.tail.sort(comparator)
    //println(h)
    println("before calling insert --head is :=" + head + "\t" + "sortedTail is : =" + sortedTail)

    val list = insert(head, sortedTail)
    println("returned list" + list)
    println("sorted-tail" + sortedTail)
    list
  }

  def zip[B, C](list: MyList[B], zipfunction: (A, B) => C): MyList[C] =
    if (list.isEmptyList) throw new RuntimeException("List do not have the same length")
    else {
      Node(zipfunction(this.head, list.head), this.tail.zip(list. tail, zipfunction))
    }

  /*
  * [1,2,3].fold(0)(+)=
  * [2,3].fold(1)(+)=
  * [3].fold(3)(+)=
  * [].fold(6)(+)=
  * =6 is the final output
  *
  */

  def fold[B](start: B)(operator: (B, A) => B): B = {
    @tailrec
    def foldTailRec(remaining: MyList[A], accumulator: B): B = {
      if (remaining.isEmptyList) accumulator
      else foldTailRec(remaining.tail, operator.apply(accumulator, this.head))
    }

    foldTailRec(this, start)
  }

  override def sum[B >: A](implicit num: Numeric[B]): B = fold(num.zero)(num.plus)
}

object Listest extends App {

  val listOfIntegers: MyList[Int] = new Node(4, new Node(2, new Node(3, Nil)))
  val clonelistOfIntegers: MyList[Int] = new Node(4, new Node(2, new Node(3, Nil)))
  val anotherListOfIntegers: MyList[Int] = new Node(4, new Node(5, new Node(6, Nil)))
  val listOfString: MyList[String] = new Node("Hello", new Node("Scala", Nil))
  println(listOfIntegers.toString())
  println(listOfString.toString())
  println(listOfIntegers.map(elem => elem * 2).toString())

  println(listOfIntegers.filter(elem => elem % 2 == 0).toString())

  println((listOfIntegers ++ anotherListOfIntegers).toString())
  println(listOfIntegers.flatMap(elem =>  Node(elem,  Node(elem + 1, Nil))).toString())
  listOfIntegers.foreach(elem => println(elem))

  println(clonelistOfIntegers.sort((x, y) => x - y))
  val zipValue: MyList[(Int, Int)] =anotherListOfIntegers.zip(listOfIntegers, (x: Int, y: Int) => (x,y))
  println(anotherListOfIntegers.zip(listOfIntegers, (x: Int, y: Int) => (x,y)))
  println((listOfIntegers.fold(0)(_ + _)))
  //for comprehension
  val combinations: MyList[String] = for {
    n <- listOfIntegers
    string <- listOfString
  } yield n + "-" + string + "\t"

  println(combinations)

}
