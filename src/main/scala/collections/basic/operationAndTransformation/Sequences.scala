package collections.basic.operationAndTransformation

import scala.collection.IndexedSeq
import scala.util.Random

/*
TODO
 Its an important note,
 I use Seq in the following examples to keep things simple,
 but in your code you should be more precise and use IndexedSeq or
 LinearSeq where appropriate. As the Seq class Scaladoc states:
 “Seq has two principal subtraits,
TODO
     IndexedSeq and LinearSeq,
     which give different guarantees for performance.
     An IndexedSeq provides fast random-access of elements and a fast length operation.
  A LinearSeq provides fast access only to the first element via head,
  but also has a fast tail operation.”
TODO
 Also, please see the “Seq is not Vector” section at the end of this post,
  because as that name implies,
 Seq behaves differently than Vector in almost all of these examples.
 */

object Sequences extends App {

  val asequnce: Seq[Int] = Seq.apply(1, 4, 3, 2)

  println(asequnce)

  println(asequnce.reverse)
  println(asequnce(2))
  println(asequnce ++ Seq(7, 6, 5))
  println(asequnce.sorted)
  //Ranges
  val arange: Seq[Int] = 1 to 10

  arange.foreach(x => println(x))

 val seq1: Seq[Int] = 1 to 5
  //# List(1, 2, 3, 4, 5)
  (1 until 5)               // # List(1, 2, 3, 4)

  (1 to 10 by 2)            // # List(1, 3, 5, 7, 9)
  (1 until 10 by 2)         // # List(1, 3, 5, 7, 9)
  (1 to 10).by(2)           // # List(1, 3, 5, 7, 9)

  ('d' to 'h')               //# List(d, e, f, g, h)
  ('d' until 'h')           // # List(d, e, f, g)

  ('a' to 'f').by(2)        // # List(a, c, e)

/*
TODO
    Range is a datastructures that represents an ordered collection of numbers.
    We can create a Range for positive, negative, and decimal values.
    We can also iterate over a Range
    using most of the collection methods like map, filter, and foreach. Let’s see how we can create ranges.
    We can create a Range by providing the start and end values to the Range.inclusive method.
    Let’s see how we can create a range of integers from 1 to 10:
    Also Range is Indexed Sequence so it  has all Seq methods
 */

  val rangeIncl: Seq[Int] = Range.inclusive(1,10)
  rangeIncl.toList equals List(1,2,3,4,5,6,7,8,9,10)

  //# todo : -> range method of Sequence
  /*
TODO
    /**
       range method Produces a collection containing equally spaced values in some integer interval.
   *   start the start value of the collection
   *   end   the end value of the collection (the first value NOT contained)
   *   step  the difference between successive elements of the collection (must be positive or negative)
   *  @return a collection with values `start, start + step, ...` up to, but excluding `end`
   */
   */
  Seq.range(1, 3)                  //# List(1, 2)
  Seq.range(1, 6, 2)               //# List(1, 3, 5)
/*
TODO
     Range with custom step i.e by using by keyWord
     When we create a Range, the default step value is 1. That means,
     between any two consecutive values in a range,
    the difference will always be 1.
    We can also create a range with a different step value using the keyword by.
    For example, we can create a range of odd numbers from 1 to 100 using:
 */

  val oddRange: Range = 1 to 100 by 2
  //TODO Range has head and last defs defined
  oddRange.head equals  1
  oddRange.last equals 99
//The variable reverseRange will contain even numbers from 20 to 2, in descending order.
  val reverseRange = 20 to 1 by -2
  reverseRange.head equals 20
  reverseRange.last equals 2

  val doubleRange:IndexedSeq[Double] = 1.0 to 2.0 by 0.2
  val decimalRange = BigDecimal(1.0) to BigDecimal(2.0) by 0.2
  decimalRange equals  List(1.0,1.2,1.4,1.6,1.8,2.0)
//Notice that IndexedSeq gives you a Vector and LinearSeq gives you a List:
  //println(x,y,z)
  import scala.collection._
/*
 TODO
     Vector is a general-purpose, immutable data structure.
      It provides random access and updates in effectively constant time,
       as well as very fast append and prepend. Because vectors strike
       a good balance between fast random selections and fast random functional updates,
       they are currently the default implementation of immutable indexed sequences.
   By default, specifying that you want an IndexedSeq with Scala 2.10.x creates a Vector:
   Vector is a persistent data structure using structural sharing.
   This technique makes the append/prepend operation almost as fast as any mutable data structure.
   When we add a new element by appending or prepending,
   it modifies the structure using structure sharing and returns the result as a new Vector.

scala> val x = IndexedSeq(1,2,3)
 */
  val vector: IndexedSeq[Int] = Vector.apply(1, 2, 3)

  // todo : -> we call it LinkList
  val nums1: scala.collection.LinearSeq[Int] = List(1, 2, 3)


  //# append
  val v1 = Seq(4,5,6)             // # List(4, 5, 6)
  val v2 = v1 :+ 7                 //# append operator List(4, 5, 6, 7)
  val v3 = v2 ++ Seq(8,9)         // # List(4, 5, 6, 7, 8, 9)

  //# prepend
  val v4 = 3 +: v3                 //# prepend List(3, 4, 5, 6, 7, 8, 9)
  val v5 = Seq(1,2) ++: v4         //# append operation List(1, 2, 3, 4, 5, 6, 7, 8, 9)


  //TODO test to show Vector is better then List for prepend and append operations

  def appendPrependSeq(seq:Seq[Int], f: (Seq[Int], Int) => Seq[Int], it:Int): (Seq[Int], Double) ={
    val begin = System.currentTimeMillis
    var modifiedSeq: Seq[Int] = seq
    for(j <- 0 until it) modifiedSeq = f.apply(modifiedSeq, j)
    val elapsedTime = System.currentTimeMillis - begin
     (modifiedSeq, elapsedTime)
  }

  val vec:Vector[Int] = Vector()
  val lst:List[Int] = List()

  val numElements = 10000
  val appendTimeRatio = appendPrependSeq(lst, (a,b) => a:+b, numElements)._2/appendPrependSeq(vec, (a,b) => a:+b, numElements)._2
  val prependTimeRatio = appendPrependSeq(vec, (a,b) => b+:a, numElements)._2/appendPrependSeq(lst, (a,b) => b+:a, numElements)._2

  println("Append test with %s elements, Vector is ~ %s times faster than List".format(numElements, appendTimeRatio))
  println("Prepend test with %s elements, List is ~ %s times faster than Vector".format(numElements, prependTimeRatio))

  //TODO  Random Access and Random Updates
  /*
TODO
    This is where the Vector shines really well compared to any other immutable collections.
    Thanks to its implementation using a trie data structure,
    accessing an element at any position requires traversing a few levels and branches in the tree.
    Let’s run some experiments to see the actual performance benefits in action:
   */
  def randomAccessSeq(seq:Seq[Int], it:Int): (Double) ={
    val begin = System.currentTimeMillis
    for  {
      j <- 0 until it
      val idx = Random.nextInt(it)
      val elem = seq(idx)
    }elem
    val elapsedTime = System.currentTimeMillis - begin
     elapsedTime
  }

  val numElements1 = 10000
  val vec1:Vector[Int] = (1 to numElements).toVector
  val lst1:List[Int] = (1 to numElements).toList

  val randomAccessTimeRatio = randomAccessSeq(lst, numElements)/randomAccessSeq(vec, numElements)
  println("Random access test with %s elements, Vector is ~ %s times faster than List".format(numElements, randomAccessTimeRatio))


  //TODO Head/Tail Access
  /*
TODO
    Another widespread operation is head/tail element access. As we can guess,
    accessing the first and last elements from an indexed tree data structure are both high-speed operations.
    Let’s see another example to see how Vector performs in such scenarios:
   */

  def headTailAccessSeq(seq:Seq[Int], f: (Seq[Int]) => Int): (Int, Double) ={
    val begin = System.currentTimeMillis
    val headOrTail: Int = f(seq)
    val elapsedTime = System.currentTimeMillis - begin
     (headOrTail, elapsedTime)
  }

  val numElements2 = 1000000
  val vec2:Vector[Int] = (1 to numElements).toVector
  val lst2:List[Int] = (1 to numElements).toList

  println(" Vector of %s elements took %s for head access".format(numElements, headTailAccessSeq(vec, (seq) => seq.head)._2 ))
  println(" List of %s elements took %s for head access".format(numElements, headTailAccessSeq(lst, (seq) => seq.head)._2 ))

  println(" Vector of %s elements took %s for tail access".format(numElements, headTailAccessSeq(vec, (seq) => seq.last)._2 ))
  println(" List of %s elements took %s for tail access".format(numElements, headTailAccessSeq(lst, (seq) => seq.last)._2 ))

  //tODO Iteration
  /*
TODO
    Some algorithms require iterating through all elements in the entire collection.
    Although traversing all elements in a Vector is slightly more complicated than a linked list,
     the performance does not show any significant difference
   */
  def calculateSumSeq(seq:Seq[Int]): (Int, Double) ={
    val begin = System.currentTimeMillis
    var total = 0
    for (elem <- seq) {
      total = total + elem
    }
    val elapsedTime = System.currentTimeMillis - begin
    return (total, elapsedTime)
  }

  val numElements4 = 10000000
  val vec4:Vector[Int] = (1 to numElements).toVector
  val lst4:List[Int] = (1 to numElements).toList

  println(" Vector iteration of %s elements took %s milliseconds".format(numElements, calculateSumSeq(vec)._2))
  println(" List iteration of %s elements took %s milliseconds".format(numElements, calculateSumSeq(lst)._2))
/*
TODO
     Summary:->
     Lists don’t have randomised element access:
     every time we need to go through the list
     until we reach the element at the index we are looking for.
     Although both Seq and Vector behaved quite well,
     our winner for this round is Seq.
     None that the default implementation for Seq for Scala 2.11 is based on LinkedLists,
    while Vectors are implemented using a bit-map with
   a factor branching of 32 (a weird structure that can be seen as a tree with up to 32 children nodes).

   TODO Use Cases:
TODO
 List: Useful when you frequently prepend elements,
 don't need random access, and the list isn't too large.
TODO
 Vector: Suitable for cases where you need efficient random access, insertion,
 and modification of elements, especially for larger collections.

 */
}