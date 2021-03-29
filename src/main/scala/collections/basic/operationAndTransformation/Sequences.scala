package collections.basic.operationAndTransformation

/*
TODO
 s an important note,
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
 val seq1: Seq[Int] = (1 to 5)
  //# List(1, 2, 3, 4, 5)
  (1 until 5)               // # List(1, 2, 3, 4)

  (1 to 10 by 2)            // # List(1, 3, 5, 7, 9)
  (1 until 10 by 2)         // # List(1, 3, 5, 7, 9)
  (1 to 10).by(2)           // # List(1, 3, 5, 7, 9)

  ('d' to 'h')               //# List(d, e, f, g, h)
  ('d' until 'h')           // # List(d, e, f, g)

  ('a' to 'f').by(2)        // # List(a, c, e)

  //# range method
  Seq.range(1, 3)                  //# List(1, 2)
  Seq.range(1, 6, 2)               //# List(1, 3, 5)

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
       It is backed by a little endian bit-mapped vector trie with a branching factor of 32.
        Locality is very good, but not contiguous, which is good for very large sequences.

 */
  val vector: IndexedSeq[Int] = Vector.apply(1, 2, 3)

  val nums1: scala.collection.LinearSeq[Int] = List(1, 2, 3)


  //# append
  val v1 = Seq(4,5,6)             // # List(4, 5, 6)
  val v2 = v1 :+ 7                 //# append operator List(4, 5, 6, 7)
  val v3 = v2 ++ Seq(8,9)         // # List(4, 5, 6, 7, 8, 9)

  //# prepend
  val v4 = 3 +: v3                 //# prepend List(3, 4, 5, 6, 7, 8, 9)
  val v5 = Seq(1,2) ++: v4         //# append operation List(1, 2, 3, 4, 5, 6, 7, 8, 9)

}