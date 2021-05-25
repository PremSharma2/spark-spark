package collections.basic.operationAndTransformation

object Map_FlatMap_Filter_for extends App {

  val list = List(1, 2, 3)
  val input = List("hello-world", "scala", "prem")
  /*
  TODO
     Tests whether a predicate holds for at least one element of this traversable collection.
     Note: may not terminate for infinite-sized collections.
     this can be useful when we want to terminate the iteration here
     on this condition and return the result
   */
  val rExists: Boolean = input.exists(_.matches("hello-world"))
  /*
  TODO
    Finds the first element which yields the largest value measured by function f
   */
  val maxby: String = input.maxBy(x => x.length())
  println(input.maxBy(x => x.length()))
  println(list.head)
  println(list.headOption)
  println(list.tail)
  val l = List(1, 2, 3, 4)
  val l1 = List(5, 6, 7, 8)
  val alist = List(2, 3, 5, 7)
  /*
   TODO
      A copy of the general sequence with an element prepended.
    Note that :-ending operators are right associative (see example).
     A mnemonic for +: vs. :+ is: the COLon goes on the COLlection side.
    Also, the original general sequence is not modified, so you will want to capture the result.
    Example:
      scala> val x = List(1)
      x: List[Int] = List(1)
      scala> val y = 2 +: x
      y: List[Int] = List(2, 1)

   */
  val prepended = 1 +: alist // List(1,2,3,5,7)
  val prepen = 0 :: alist // this Right associative basically i.e alist :: 0
  val associtivoperatorlist = 1 :: 2 :: 3 :: Nil
  alist.::(0)
  val appended = alist :+ 9
  println(appended)
  println(l ++ l1)

  //List(List(1, 2, 3, 4), 5, 6, 7, 8)
  //Adds an element at the beginning of this list
  // you can pass list or any value
  //1 :: List(2, 3) = List(2, 3).::(1) = List(1, 2, 3)
  val listOfIntegerAndList: List[Any] = l :: l1
  println(l :: l1)

  println(l.::(0))

  val test: Seq[Int] = (1 to 5).toList // # List(1, 2, 3, 4, 5)
  (1 until 5).toList //# List(1, 2, 3, 4)

  (1 to 10 by 2).toList // # List(1, 3, 5, 7, 9) remove all elements divisible by 2
  (1 until 10 by 2).toList // # List(1, 3, 5, 7, 9)
  (1 to 10).by(2).toList // # List(1, 3, 5, 7, 9)
  //compiler will rewrite this like this l1 :: l now l will be prepended
  /*
   TODO
      def :::[B >: A](prefix: List[B]): List[B]
       Adds the elements of a given list i.e this in front of this prefix list.
    Example:
    List(1, 2) ::: List(3, 4) = List(3, 4).:::(List(1, 2)) = List(1, 2, 3, 4)
   */
  val listOfList: List[Int] = l ::: l1
  println(l ::: l1)
  /*
   TODO
       Applies a function f to all elements of this iterable collection.
   */

  list.foreach(println(_))
  // map
  //TODO override final def map[B, That](f: A => B)(implicit bf: CanBuildFrom[List[A], B, That])
  println(list.map(_ + 1))
  println(list.map(x => x + 1))
  //filter
  /*
    TODO
        Selects all elements of this traversable collection which satisfy a predicate.
   */
  println(list.filter(x => x % 2 == 0))
  println(list.filter(_ % 2 == 0))
  //flatmap
  // ETw pattern
  val transform: Int => (Int, Int) = x => (x, x + 1)
  //ETW
  val toPair = (x: Int) => List(transform.apply(x))
  //TODO def flatMap[B, A](f: A => List[B])
  println(list.flatMap(toPair))
  println(list.map(transform))
  val numbers = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  //iteration logic 
  val combinations: Seq[String] = numbers.
    flatMap(n => chars.map(c => "" + c + n))

  println(combinations)
  val forcomprehension: Seq[String] =
    for {
      n: Int <- numbers if n % 2 == 0 // with if filter
      c: Char <- chars
    } yield "" + c + n
  println(forcomprehension)

  // syntax overload
  list.map {
    //pattern matching using partial function
    case x => x * 2
  }
  // dropRight
  val m1 = List(1, 1, 3, 3, 3, 5, 4, 5, 2)
  // Applying dropRight method
  //It returns all the elements of the list except the last n elements.
  //List(1, 1, 3, 3, 3, 5)
  // i.e it will drop last 3 elements
  val res = m1.dropRight(3)

  // Displays output
  println(res)

  /*
  drop(n)	Return all elements after the first n elements
   */
  println(m1.drop(2))
  //Method Definition: def dropWhile(p: (A) => Boolean): List[A]
  // drops all elements which satisfy the predicate
  //Return Type: It returns all the elements of the list except the dropped ones.
  // Creating a list
  val m2 = List(1, 3, 5, 4, 2)

  // Applying dropWhile method
  val res1 = m1.dropWhile(x => {
    x % 2 != 0
  })

  // Displays output
  println(res)

  /*
  Method Definition : def find(p: (A) => Boolean): Option[A]

   Return Type :It returns an Option value containing the first element of the stated collection
   that satisfies the used predicate else returns None if none exists.
   */
/*
TODO
 An IndexedSeq indicates that random access of elements is efficient,
  such as accessing an Array element as arr(5000).
 By default, specifying that you want an IndexedSeq with Scala 2.10.x creates a Vector:

scala> val x = IndexedSeq(1,2,3)
 */
  // Creating an Iterator
  val iter: Iterator[Int] = Iterator.apply(2, 4, 5, 1, 13)

  // Applying find method
  val result: Option[Int] = iter.find(_ > 1)

  // Displays output
  println(result)
  /*
  init	All elements except the last one
   */
  val initList: Seq[Int] = m2.init
  println(initList)

  /*
  intersect(s)	Return the intersection of the list and another sequence s
   */

  val intersection: Seq[Int] = m1.intersect(m2)
  println(intersection)

  /*
  lastOption	The last element as an Option
   */

  val lastOption: Option[Int] = intersection.lastOption
  /*
  takeWhile(p)	The first subset of elements that matches the predicate p
   */
  val takeWhileSeq = Seq(2, 4, 6, 8)
  val takenWhile: Seq[Int] = takeWhileSeq.takeWhile(x => {
    x % 2 == 0
  })
  println(lastOption.get)
  println(takenWhile)
}