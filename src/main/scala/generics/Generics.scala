package generics

import generics.Transformer1.MyTransformer

//always use covariance when you have collection of things,Hence We are making Covariant List
trait MyGenericList[+A] {
  /*
   *
   * head: first element of list
   * tail: remainder of list
   * isEmpty:Is this list empty
   * add(int)=> return new List with this element added
   * tostring:string representation of the list
   */

  def head: A

  def tail: MyGenericList[A]

  def isEmpty: Boolean

  //it is a consumer
  def add[B >: A](element: B): MyGenericList[B]

  def printElements: String

  override def toString: String = "[" + printElements + "]"

  //iTS  a double contravariant hence it will become Covariant
  def map[B](transformer: MyTransformer[A, B]): MyGenericList[B]

  def flatMap[B](transformer: MyTransformer[A, MyGenericList[B]]): MyGenericList[B]

  def filter(predicate: MyPredicate[A]): MyGenericList[A]

  def ++[B >: A](list: MyGenericList[B]): MyGenericList[B]
}

/*
 *
 * No one can create such object like Nothing in Scala

  Basically it is used to,

 Give signal of abnormal termination, like while throwing an Error from the application
  To denote empty Collection
  so as nothing substitute for anything in scala similar ways EmptyList object  substitutes to all Empty Collection
 */
object EmptyList extends MyGenericList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: MyGenericList[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  //Here also we enforced type restriction at compile time that element being aded must be
  // of type Nothing i.e both should have same type
  // but herecatch is that Nothing is sub type of Everything and we wnat to make it covarient list
  def add[B >: Nothing](element: B): MyGenericList[B] = new Node(element, EmptyList)

  def printElements: String = ""

  def map[B](transformer: MyTransformer[Nothing, B]): MyGenericList[B] = EmptyList

  def flatMap[B](transformer: MyTransformer[Nothing, MyGenericList[B]]): MyGenericList[B] = EmptyList

  def filter(predicate: MyPredicate[Nothing]): MyGenericList[Nothing] = EmptyList

  def ++[B >: Nothing](list: MyGenericList[B]): MyGenericList[B] = list
}

class Node[+A](override val head: A, override val tail: MyGenericList[A]) extends MyGenericList[A] {

  def isEmpty: Boolean = return false

  def add[B >: A](element: B): MyGenericList[B] = new Node(element, this)

  def printElements: String = {
    if (tail.isEmpty) "" + head
    else {
      head + "," + tail.printElements
    }
  }

  /*
   *
   *
   * [1,2,3].filter(n%2 ==0) =
   * [2,3].filter(n%2==0)=
   * new Cons(2,[3].filter(n%2==0))=
   * =  new Cons(2,Empty.filter(n%2==0)
   * =new Cons(2,Empty)
   */


  def filter(predicate: MyPredicate[A]): MyGenericList[A] = {
    if (predicate.test(this.head))
      new Node(this.head, this.tail.filter(predicate))
    else
      tail.filter(predicate)
  }

  /*
   * [1,2,3].map(n*2)
   * =new Node(2, [2,3].map(n*2)))
   *
   * =new Node(2, new Node(4, [3].map(n*2)))
   * =new Node(2, new Node(4, new Node(6,Empty.map(n*2)))
   *
   * =new Node(2, new Node(4, new Node(6,Empty)))
   *
   *
   *
   */
  def map[B](transformer: MyTransformer[A, B]): MyGenericList[B] = {
    new Node(transformer.transform(this.head), tail.map(transformer))
  }

  /*
   * [1,2] ++ [3,4,5]
   * =new Node(1, [2] ++ [3,4,5])
   * =new Node (1, new Node(2,Empty ++ [3,4,5]))
   * =new Node(1, new Node(2, [3,4,5])
   * or
   * =new Node(1,new Node(2,new Node(3,new Node(4,new Node(5,Empty)))))
   *
   */
  def ++[B >: A](list: MyGenericList[B]): MyGenericList[B] = new Node(head, this.tail ++ list)

  /*for eg here let say transformer take int and returns List[Int] i.e a role of flatmap it flatens
   * [1,2].flatMap (n => [n,n+1])
   * this.head.transform(1)= [1,2]
   * [1,2] ++ [2].flatmap(n => [n,n+1])
   * [2,3] ++ Empty.flatMap(n => [n,n+1])
   *  Empty
   * now recursion traces back
   * [2,3] ++ Empty
   * [1,2] ++ [2,3]
   * [1,2] ++ [2,3] here final list by using recursion is [2,3] now we will add these two
   * now using ++ function these two list will be concatenated
   * [1,2,2,3]
   *
   *
   *
   */

  def flatMap[B](transformer: MyTransformer[A, MyGenericList[B]]): MyGenericList[B] = {
    transformer.transform(this.head) ++ this.tail.flatMap(transformer)
  }
}

object Listest extends App {
  //val emptytail=EmptyList.tail
  // println(emptytail)
  def process(list: MyGenericList[AnyVal]) = ()

  val listOfIntegers: MyGenericList[Int] =
    new Node(1, new Node(2, new Node(3, EmptyList)))

  val anotherListOfIntegers: MyGenericList[Int] =
    new Node(4, new Node(5, new Node(6, EmptyList)))
  val listOfString: MyGenericList[String] =
    new Node("Hello", new Node("Scala", EmptyList))

  println(listOfIntegers.toString())
  println(listOfString.toString())

  println(listOfIntegers.map[Int](new MyTransformer[Int, Int] {

    override def transform(elem: Int): Int = elem * 2

  }).toString())

  println(listOfIntegers.filter(new MyPredicate[Int] {

    override def test(element: Int): Boolean = element % 2 == 0
  }).toString())

  println((listOfIntegers ++ anotherListOfIntegers).toString())
  println(listOfIntegers.flatMap(new MyTransformer[Int, MyGenericList[Int]] {

    override def transform(elem: Int): MyGenericList[Int] = {
      new Node(elem, new Node(elem + 1, EmptyList))
    }
  }).toString())

  process(listOfIntegers)
}
