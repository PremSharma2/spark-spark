package functional.programming

/*
 *
 *
 * List implementation with Functional Programming Flavour,with Transformer and predicate replacing the inbuilt functional interfaces
 */

abstract class MyList[+A] {
  /*
   *
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
  def flat_Map[B](transformer: A => MyList[B]): MyList[B]
  def filter(predicate: A => Boolean): MyList[A]
  def +[B >: A](list: MyList[B]): MyList[B]
  //hofs
  def foreach( f: A => Unit): Unit
}
/*
 *
 * No one can create such object like Nothing in Scala

  Basically it is used to,

 Give signal of abnormal termination, like while throwing an Error from the application
  To denote empty Collection
  so as nothing substitute for anything in scala similar ways EmptyList object  substitutes to all EmptyList Collection
 */
case object EmptyList extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException
  def tail: MyList[Nothing] = throw new NoSuchElementException
  def isEmptyList: Boolean = true
  def addElement[B >: Nothing](element: B): MyList[B] = new Node(element, EmptyList)
  def printListElements: String = ""
  def map[B](transformer: Nothing => B): MyList[B] = EmptyList
  def flat_Map[B](transformer: Nothing => MyList[B]): MyList[B] = EmptyList
  def filter(predicate: Nothing => Boolean): MyList[Nothing] = EmptyList
  def +[B >: Nothing](list: MyList[B]): MyList[B] = list
  //hofs
  def foreach( f: Nothing => Unit): Unit= ()
}
case class Node[+A](h: A, t: MyList[A]) extends MyList[A] {
  def head: A = return h
  def tail: MyList[A] = return t
  def isEmptyList: Boolean = return false
  def addElement[B >: A](element: B): MyList[B] = new Node(element, this)
  def printListElements: String = {
    if (t.isEmptyList) "" + h
    else {
      h + "" + t.printListElements
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

  def filter(predicate: A => Boolean): MyList[A] = {
    if (predicate.apply(h)) new Node(h, t.filter(predicate))
    else
      t.filter(predicate)
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
   *
   *new Node(trasform(this.h), this.tail.map(tx))
   */
  def map[B](transformer: A => B): MyList[B] = {
    new Node(transformer.apply(h), t.map(transformer))
  }

  /*
   * [1,2] + [3,4,5]
   * =new Node(1, [2] + [3,4,5])
   * =new Node (1, new Node(2,EmptyList + [3,4,5]))
   * =new Node(1, new cons(2, [3,4,5])
   * or
   * =new cons(1,new Node(2,new Node(3,new Node(4,new Node(5,EmptyList)))))
   *
   */
  def +[B >: A](list: MyList[B]): MyList[B] = new Node(h, t + list)

  /*for eg here let say transformer take int and returns List[Int] i.e a role of flatmap it flatens
   * [1,2].flat_Map (n => [n,n+1])
   * [1,2] + [2].flatmap(n => [n,n+1])
   * [1,2] + [2,3]
   * [1,2] + [2,3] + EmptyList.flat_Map(n => [n,n+1])
   * [1,2] + [2,3] + EmptyList
   * [1,2,2,3]
   *
   *
   *tx.transform(this.h) ++ this.tail.flatMap(tx)
   */

  def flat_Map[B](transformer: A => MyList[B]): MyList[B] = {
    transformer.apply(h) + t.flat_Map(transformer)
  }
  
  //hofs
   def foreach( f: A => Unit): Unit= {
     f(h)
     t.foreach(f)
   }
}

object Listest extends App {

  val listOfIntegers: MyList[Int] = new Node(1, new Node(2, new Node(3, EmptyList)))
  val anotherListOfIntegers: MyList[Int] = new Node(4, new Node(5, new Node(6, EmptyList)))
  val listOfString: MyList[String] = new Node("Hello", new Node("Scala", EmptyList))
  println(listOfIntegers.toString())
  println(listOfString.toString())
  println(listOfIntegers.map(new Function1[Int, Int] {

    override def apply(elem: Int): Int = elem * 2

  }).toString())

  println(listOfIntegers.filter(new Function1[Int, Boolean] {

    override def apply(element: Int): Boolean = element % 2 == 0
  }).toString())

  println((listOfIntegers + anotherListOfIntegers).toString())
  println(listOfIntegers.flat_Map(new Function1[Int, MyList[Int]] {

    override def apply(elem: Int): MyList[Int] = {
      new Node(elem, new Node(elem + 1, EmptyList))
    }
  }).toString())
  
  
}
