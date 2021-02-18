package tailRecursiveList

import caseClass.Factory.Animal.{Animal, Dog}

import scala.annotation.tailrec
import scala.collection.immutable

object ListProblems {
  sealed trait RList[+T]{
    def head: T
    def tail: RList[T]
    def isEmpty: Boolean
    def headOption: Option[T]
    override def toString: String = "[]"
    def ::[S >: T](element: S): RList[S] = new Node(element, this)
    def apply(index : Int): T
    def length :Int
    def reverse :RList[T]
    def ++[S>:T](anotherList:RList[S]):RList[S]
  }

  case object RNil extends RList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: RList[Nothing] = throw new NoSuchElementException
  override def isEmpty: Boolean = true
  override def headOption: Option[Nothing] = None

  override def apply(index: Int): Nothing = throw new NoSuchElementException

  override def length: Int = 0

  override def reverse: RList[Nothing] = RNil

  override def ++[S >: Nothing](anotherList: RList[S]): RList[S] = anotherList
}
//TODO here as we can see that def can be overridden as val
  /*
   TODO
        Overriding Fields
        Fields, as opposed to methods, have a special property that
        they can be overridden in the constructor directly as well.
   */
  case class Node[+T](override val head:T, override  val tail:RList[T]) extends RList[T] {
    override def isEmpty: Boolean = false
    override def headOption: Option[T] = Some(head)
  override def toString: String= {
    @tailrec
    def toStringTailRecursion(remaining: RList[T], result :String):String={
      if(remaining.isEmpty) result
      else if(remaining.tail.isEmpty) s"$result ${remaining.head}"
        // recursice call  is the last expression in code branch basically
      else toStringTailRecursion(remaining.tail,s"$result ${remaining.head}, ")
    }
    "["+ toStringTailRecursion(this,"") + "]"
  }

   override def apply(index : Int): T= {
     /*
   TODO
      List(1,2,3,4,5).apply(index=2)
    tailRecApply(  [1,2,3,4],currentIndex=0)
    again tail recursive call because  if(currentIndexAccumlator==index) condition does not met
    tailRecApply(  [2,3,4],currentIndex=1)
     again tail recursive call because  if(currentIndexAccumlator==index) condition does not met
     tailRecApply(  [3,4],currentIndex=2)
     now this time condition is met we will fetch the head and return
     i.e remaining.head i.e 3 we will return and this tail recursive so recursion will trace back

Complexity is O(min(N,index))
      */
     @tailrec
     def tailRecApply(remaining:RList[T], currentIndexAccumlator:Int):T={
       if(currentIndexAccumlator==index) remaining.head
       else tailRecApply(remaining.tail , currentIndexAccumlator + 1 )
     }
     if (index<0) throw new NoSuchElementException
     else tailRecApply(this,0)
  }
/*
TODO
   [1,2,3,4,5].length = lengthTailRec( [1,2,3,4,5],0)
   this list is not Empty then we will make a recursive call lengthTailRec( [2,3,4,5],1)
   lengthTailRec( [2,3,4,5],1)
   again we will check if this list is Empty which is not
   lengthTailRec( [3,4,5],2)
  again we will check if this list is Empty which is not
   lengthTailRec( [4,5],3)
     again we will check if this list is Empty which is not
  lengthTailRec( [5],4)
 again we will check if this list is Empty which is Empty now i.e tail is empty
 so as we know that it is tail recursive recursion will not trace back
  and will return accumulator from here
  complexity is O(N)
 */
  override def length: Int = {
    @tailrec
    def lengthTailRec(remaining:RList[T], accumulator:Int):Int ={
      if(remaining.isEmpty) accumulator
      else lengthTailRec(remaining.tail , accumulator +1 )
    }
    lengthTailRec(this, 0)
  }
/*
TODO
  [1,2,3,4].reverse = this will  call to
  reverseTailRec([1,2,3,4],RNil)
  now inside reverseTailRec we will check condtion
  that if(remaining.isEmptyList) and it is not empty then again recursive call
   reverseTailRec([2,3,4],[1])
   now inside reverseTailRec we will check condtion
  that if(remaining.isEmptyList) and it is not empty then again recursive call
  reverseTailRec([3,4],[2,1])
  now inside reverseTailRec we will check condtion
  that if(remaining.isEmptyList) and it is not empty then again recursive call
  reverseTailRec([4],[3,2,1])
  now inside reverseTailRec we will check condtion
  that if(remaining.isEmptyList) and it is not empty then again recursive call
  reverseTailRec([],[4,3,2,1])
   now inside reverseTailRec we will check condition
  that if(remaining.isEmptyList) and it is empty this time so we will return accumulator
  Now what is the complexity of this Algorithm
  O(N)
 */
  override def reverse: RList[T] = {
    @tailrec
    def reverseTailRec(remaining:RList[T],accumulator:RList[T]):RList[T] ={
      if(remaining.isEmpty) accumulator
      else reverseTailRec(remaining.tail, remaining.head :: accumulator)
    }
    reverseTailRec(this,RNil)
  }
// TODO this is not stack recursive because here recursive call is not the last thing  here
  // in  head :: (this.tail ++ anotherList) ++ is evaluated on top of ++
  /*
  override def ++[S >: T](anotherList: RList[S]): RList[S] =
    head :: (this.tail ++ anotherList)

   */
  /*
   [1,2,3] ++ [4,5] = concatTailRec([1,2,3], [4,5])
   now inside concatTailRec we will check whether [1,2,3].isEmpty which is false
   now we will make again recursive call to iterate further
   concatTailRec([2,3], [1,4,5])
   now inside concatTailRec we will check whether [2,3].isEmpty which is false
   now we will make again recursive call to iterate further
   concatTailRec([3], [2,1,4,5])
   now inside concatTailRec we will check whether [3].isEmpty which is false
   now we will make again recursive call to iterate further
    concatTailRec([], [3,2,1,4,5])
    now inside concatTailRec we will check whether [].isEmpty which is true
    now we will return accumlator = [3,2,1,4,5]
    But this is wrong order of elements
    to fix this we need to reverse the current list i.e this.reverse
   */
  override def ++[S >: T](anotherList: RList[S]): RList[S] ={
    def concatTailRec(remaining:RList[S], accumlator:RList[S]):RList[S] ={
      if(remaining.isEmpty) accumlator
      else concatTailRec(remaining.tail,remaining.head :: accumlator)
    }
    concatTailRec(this.reverse,anotherList)
  }
}
  object RList{
    def from[T](iterable:Iterable[T]) :RList[T]= {
      @tailrec
      def convertToRListTailRec(remaining:Iterable[T], accumulator: RList[T]):RList[T] = {
        if(remaining.isEmpty) accumulator
        else convertToRListTailRec(remaining.tail, remaining.head :: accumulator)
      }
      convertToRListTailRec(iterable,RNil).reverse
    }
  }
  def main(args: Array[String]): Unit = {
    val listOfIntegers: RList[Int] =  Node(1, new Node(2, new Node(3, RNil)))
    val list: RList[Int] = 1 :: 2 :: 3 :: RNil
    val list1: RList[Int] = 4 :: 5 :: 6 :: RNil
    val iterable: immutable.Seq[Int] = 1 to 10000
    Iterable.apply(2,3,4)
    val aLargeList= RList.from(1 to 10000)
    // TODO This expression is right associative by default in scala
    // TODO RNil.::3.::2.:: 1 == 1 :: 2 :: 3 :: RNil
    println(listOfIntegers)
    def testApi(list:RList[Animal]) = {
      list.::(new Dog)
    }
    val animalList: RList[Dog] = new Dog :: RNil
    testApi(animalList)
    println(list.apply(2))
    println(list.length)
    println("hello")
    println(list.reverse)
    println(RList.from(1 to 10))
    println(aLargeList.length)
    println(aLargeList.apply(8735))
    println(list ++ list1)
  }
}
