package tailRecursiveList

import caseClass.Factory.Animal.{Animal, Dog}

import scala.annotation.tailrec

object ListProblems {
  sealed trait RList[+T]{
    def head: T
    def tail: RList[T]
    def isEmptyList: Boolean
    def headOption: Option[T]
    override def toString: String = "[]"
    def ::[S >: T](element: S): RList[S] = new Node(element, this)
    def apply(index : Int): T
    def length :Int
    def reverse :RList[T]
  }

case object RNil extends RList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: RList[Nothing] = throw new NoSuchElementException
  override def isEmptyList: Boolean = true
  override def headOption: Option[Nothing] = None

  override def apply(index: Int): Nothing = throw new NoSuchElementException

  override def length: Int = 0

  override def reverse: RList[Nothing] = RNil
}
//TODO here as we can see that def can be overridden as val
  /*
   TODO
        Overriding Fields
        Fields, as opposed to methods, have a special property that
        they can be overridden in the constructor directly as well.
   */
  case class Node[+T](override val head:T, override  val tail:RList[T]) extends RList[T] {
    override def isEmptyList: Boolean = false
    override def headOption: Option[T] = Some(head)
  override def toString: String= {
    @tailrec
    def toStringTailRecursion(remaining: RList[T], result :String):String={
      if(remaining.isEmptyList) result
      else if(remaining.tail.isEmptyList) s"$result ${remaining.head}"
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
      if(remaining.isEmptyList) accumulator
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
      if(remaining.isEmptyList) accumulator
      else reverseTailRec(remaining.tail, remaining.head :: accumulator)
    }
    reverseTailRec(this,RNil)
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
  }
}
