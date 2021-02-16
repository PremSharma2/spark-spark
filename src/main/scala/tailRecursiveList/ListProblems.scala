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
  }

case object RNil extends RList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: RList[Nothing] = throw new NoSuchElementException
  override def isEmptyList: Boolean = true
  override def headOption: Option[Nothing] = None

  override def apply(index: Int): Nothing = throw new NoSuchElementException

  override def length: Int = 0
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

  override def length: Int = {
    @tailrec
    def lengthTailRec(remaining:RList[T], accumulator:Int):Int ={
      if(remaining.isEmptyList) accumulator
      else lengthTailRec(remaining.tail , accumulator +1 )
    }
    lengthTailRec(this, 0)
  }
}
  def main(args: Array[String]): Unit = {
    val listOfIntegers: RList[Int] = new Node(1, new Node(2, new Node(3, RNil)))
    val list: RList[Int] = 1 :: 2 :: 3 :: RNil
    // This expression is right associative by default in scala
    //RNil.::3.::2.:: 1 == 1 :: 2 :: 3 :: RNil
    println(listOfIntegers)
    def testApi(list:RList[Animal]) = {
      list.::(new Dog)
    }
    val animalList: RList[Dog] = new Dog :: RNil
    testApi(animalList)
    println(list.apply(2))
  }
}
