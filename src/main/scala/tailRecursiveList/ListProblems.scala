package tailRecursiveList

import caseClass.Factory.Animal.{Animal, Dog}

import scala.annotation.tailrec
import scala.collection.immutable

object ListProblems {
  sealed trait RList[+T]{
    /**
     * easy Impls
     * @return
     */
    def head: T
    def tail: RList[T]
    def isEmpty: Boolean
    def headOption: Option[T]
    override def toString: String = "[]"
    // todo prepend operator
    def ::[S >: T](element: S): RList[S] = new Node(element, this)
    def apply(index : Int): T
    def length :Int
    def reverse :RList[T]
    def ++[S>:T](anotherList:RList[S]):RList[S]
    def removeAt(index:Int):RList[T]
    def map[S](f: T => S):RList[S]
    //ETW pattern
    def flatmap[S](f: T => RList[S]):RList[S]
    def filter(f: T => Boolean):RList[T]

    /**
     * Medium and difficult Impls
     */
    def rle:RList[(T,Int)]
    // duplicate
    def duplicateEach(k:Int):RList[T]
  }

  /**
   * For two reasons:
   * 1) we need to return the type Nothing, and the only expressions returning Nothing are throwing exceptions
   * 2) if there's no element to return, what can we do?
   */
  case object RNil extends RList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: RList[Nothing] = throw new NoSuchElementException
  override def isEmpty: Boolean = true
  override def headOption: Option[Nothing] = None

  override def apply(index: Int): Nothing = throw new NoSuchElementException

  override def length: Int = 0

  override def reverse: RList[Nothing] = RNil

  override def ++[S >: Nothing](anotherList: RList[S]): RList[S] = anotherList

    override def removeAt(index: Int): RList[Nothing] = RNil

    override def map[S](f: Nothing => S): RList[S] = RNil

    override def flatmap[S](f: Nothing => RList[S]): RList[S] = RNil

    override def filter(f: Nothing => Boolean): RList[Nothing] = RNil

    /**
     * Medium and difficult Impls
     */
    override def rle: RList[(Nothing, Int)] = RNil
// duplicate each element a number of times in each row
    override def duplicateEach(k: Int): RList[Nothing] = RNil
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
    def toStringTailRecursion(remaining: RList[T], accumlator :String):String={
      if(remaining.isEmpty) accumlator
      else if(remaining.tail.isEmpty) s"$accumlator ${remaining.head}"
        // recursice call  is the last expression in code branch basically
      else toStringTailRecursion(remaining.tail,s"$accumlator ${remaining.head}, ")
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
    Complexity is: O(min(N,index))
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
    Complexity:As we know that complexity is directly proportional to the
    length of traversing list
    So if the length of the current list or traversing list   is N
    and  the length of the another list is M
    O(2N) OR O(M+N)

   */
  override def ++[S >: T](anotherList: RList[S]): RList[S] ={
    def concatTailRec(remaining:RList[S], accumlator:RList[S]):RList[S] ={
      if(remaining.isEmpty) accumlator
      else concatTailRec(remaining.tail,remaining.head :: accumlator)
    }
    concatTailRec(this.reverse,anotherList)
  }
// stack recursive implementation
  /*
  override def removeAt(index: Int): RList[T] =
    if(index ==0) tail
    else head :: tail.removeAt(index- 1)

   */
  // tail recursive implementation
  /*
  [1,2,3,4,5].removeAt(2) = this will call to
  removeAtTailRec([1,2,3,4,5], 0, [])
  now inside this def we will check whether index==currentIndex and its not
so we will jump over and make recursive call
= removeAtTailRec([2,3,4,5], 1, [1])
again we will check teh same condition index==currentIndex which is not true
so we will jump over and make recursive call
= removeAtTailRec([3,4,5], 2, [2,1])
again we will check teh same condition index==currentIndex which is  true
now we will reverse the predecessor list i.e [1,2] ad concatinate with tail of remaining
[1,2] ++ [4,5]
Complexity is O(N) N is the length of the current list
   */
  override def removeAt(index: Int): RList[T] = {
    @tailrec
    def removeAtTailRec(remaining:RList[T], currentIndex:Int, predecessor:RList[T]):RList[T] ={
     if(currentIndex == index) predecessor.reverse ++ remaining.tail
     else if (remaining.isEmpty) predecessor.reverse
      else removeAtTailRec(remaining.tail, currentIndex+1 , remaining.head:: predecessor)
    }
    removeAtTailRec(this,0,RNil)
  }
// stack recursive implementation
  //override def map[S](f: T => S): RList[S] = f.apply(this.head) :: this.tail.map(f)
 // tail recursive impl
  /*
   [1,2,3].map(x=>x+1) = this will call to mapTailRec
  mapTailRec([2,3],f(1)::accumulator = [2] )
  again mapTailRec([3],f(2)::accumulator = [3,2] )
  again mapTailRec([],f(3)::accumulator = [6,3,2] )
  now this time  remaining is Empty so we will return accumulator
  i.e [6,3,2].reverse
  Comp
   */
  override def map[S](f: T => S): RList[S] = {
    @tailrec
    def mapTailRec(remaining:RList[T],accumulator:RList[S]):RList[S] ={
        if(remaining.isEmpty) accumulator.reverse
        else mapTailRec(remaining.tail,f(remaining.head):: accumulator)
    }
    mapTailRec(this,RNil)
  }
  //TODO stack recursive impl
 // override def flatmap[S](f: T => RList[S]): RList[S] = f.apply(this.head) ++ tail.flatmap(f)
  /*
    [1,2,3].flatMap(x => [x, 2*x]) = that will call to
    flatmapTailRec([1,2,3],[])
    inside this we will check if(remaining.isEmpty) accumulator here its not empty
    hence we will make again tail recursive call
    flatmapTailRec([2,3],f(1)= [1,2].reverse=[2,1] ++ [] = [2,1])
inside this we will check if(remaining.isEmpty) accumulator here its not empty
hence we will make again tail recursive call
    flatmapTailRec([3],f(2)= [2,4].reverse= [4,2] ++ [2,1] =[4,2,2,1])
    inside this we will check if(remaining.isEmpty) accumulator here its not empty
hence we will make again tail recursive call
flatmapTailRec([],f(3)= [3,6].reverse= [6,3] ++ [4,2,2,1] = )
inside this we will check if(remaining.isEmpty) accumulator and its empty now
so we will return accumulator [6,3,4,2,2,1].reverse
[1,2,2,4,3,6]
Complexity is here
O(sum of all the  lengths of f(x) = Z) i.e f(x) will produce n list and we are concatenating them
++ has complexity O(M+N)
so it will be O(Sum of all M+N)
O(z^2)
Let's say in a simple case you have n lists of size k each.
First operation is k + k = 2k
Second is 2k + k = 3k
Third is 3k + k = 4k
and so on, n times.
The total complexity strictly speaking is 2k + 3k + 4k + ... + n*k =
k * (2 + 3 + ... + n) = k * ( n * (n + 1) / 2 - 1) = O(k * n^2)
\ /
Now because I defined that Z quantity as the sum of the lengths of the list, we have Z = k * n, so the complexity is O(Z^2)
   */
 override def flatmap[S](f: T => RList[S]): RList[S] = {
   @tailrec
    def flatmapTailRec(remaining:RList[T], accumulator:RList[S]):RList[S]={
      if(remaining.isEmpty) accumulator.reverse
      else flatmapTailRec(remaining.tail , f.apply(remaining.head).reverse ++ accumulator)
    }
   flatmapTailRec(this,RNil)
 }
/*
 [1,2,3,4].filter(_%2==0) = this will make a call to
 filterTailRec([1,2,3,4],[]) inside that we will check if(remaining.isEmpty) which is false
 then we will check whether current head passes the predicate  else if (predicate.apply(remaining.head)) which fails to pass
 then we will make recursive call filterTailRec([2,3,4],[])
 inside that we will check if(remaining.isEmpty) which is false
 then we will check whether current head passes the predicate  else if (predicate.apply(remaining.head)) which actually passes
  then we will make recursive call filterTailRec([3,4],[2])
   inside that we will check if(remaining.isEmpty) which is false
 then we will check whether current head passes the predicate  else if (predicate.apply(remaining.head)) which fails
 then we will make recursive call filterTailRec([4],[2])

 inside that we will check if(remaining.isEmpty) which is false
 then we will check whether current head passes the predicate  else if (predicate.apply(remaining.head)) which actually passes
  filterTailRec([],[4,2])
 inside that we will check if(remaining.isEmpty) which is true then we wil return accumlator.reverse
 [2,4]
 */
  override def filter(predicate: T => Boolean): RList[T] = {
    @tailrec
    def filterTailRec(remaining:RList[T], accumulator:RList[T]):RList[T] ={
      if(remaining.isEmpty) accumulator.reverse
      else if (predicate.apply(remaining.head)) filterTailRec(remaining.tail, remaining.head:: accumulator)
      else filterTailRec(remaining.tail,accumulator)
    }
    filterTailRec(this,RNil)
  }

  /**
   * Medium and difficult Impls
   *
   */
    /*
      TODO
         [1,1,1,2,2,3,4,4,4,5].rle= that will call to
         rleTailRec([1,1,2,2,3,4,4,4,5],(1,1),[])
         inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
         then we will check else if(remaining.isEmpty) currentTuple :: accumulator
         else if (remaining.head == currentTuple._1) this is  the case
        rleTailRec([1,2,2,3,4,4,4,5],(1,2),[])

        TODO
           rleTailRec([1,2,2,3,4,4,4,5],(1,2),[])
         inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
         then we will check else if(remaining.isEmpty) currentTuple :: accumulator
         else if (remaining.head == currentTuple._1) this  the case
         rleTailRec([2,2,3,4,4,4,5],(1,3),[])

        TODO
         rleTailRec([2,2,3,4,4,4,5],(1,3),[])
         inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
         then we will check else if(remaining.isEmpty) currentTuple :: accumulator
         else if (remaining.head == currentTuple._1) this is not  the case
         will go else mode
         rleTailRec([2,3,4,4,4,5],(2,1),[(1,3)])

       TODO
          rleTailRec([2,2,3,4,4,4,5],(2,1),[(1,3)])
         inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
         then we will check else if(remaining.isEmpty) currentTuple :: accumulator
         else if (remaining.head == currentTuple._1) this  the case
         rleTailRec([3,4,4,4,5],(2,2),[(1,3)])

        TODO
         rleTailRec([3,4,4,4,5],(2,2),[(1,3)])
         inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
         then we will check else if(remaining.isEmpty) currentTuple :: accumulator
         else if (remaining.head == currentTuple._1) this is not the case
         we will go in else mode
         rleTailRec([4,4,5],(4,1),[(1,3),(2,2),(3,1)])

         TODO
             rleTailRec([4,4,5],(4,1),[(1,3),(2,2),(3,1)])
          inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
          then we will check else if(remaining.isEmpty) currentTuple :: accumulator
          else if (remaining.head == currentTuple._1) this is   the case
          rleTailRec([4,5],(4,2),[(1,3),(2,2),(3,1)])


           TODO
             rleTailRec([4,5],(4,2),[(1,3),(2,2),(3,1)])
            inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
            then we will check else if(remaining.isEmpty) currentTuple :: accumulator this is not the case
            else if (remaining.head == currentTuple._1) this is   the case
            rleTailRec([5],(4,3),[(1,3),(2,2),(3,1)])

           TODO
             rleTailRec([5],(4,3),[(1,3),(2,2),(3,1)])
            inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
            then we will check else if(remaining.isEmpty) currentTuple :: accumulator
              else if (remaining.head == currentTuple._1) this is  not the case
              will go to else mode
            rleTailRec([],(5,1),[(1,3),(2,2),(3,1),(4,3)])

         TODO
          rleTailRec([],(5,1),[(1,3),(2,2),(3,1),(4,3)])
            inside this method we will check if(remaining.isEmpty && currentTuple._2==0) and it is false
            then we will check else if(remaining.isEmpty) currentTuple :: accumulator and this time this is the case
               [(1,3),(2,2),(3,1),(4,3),(5,1)]
            after reversing the accumlator we will get this
     */
  override def rle: RList[(T, Int)] = {
    @tailrec
    def rleTailRec(remaining:RList[T], currentTuple:(T,Int),accumulator:RList[(T, Int)]  ) : RList[(T, Int)]={
      if(remaining.isEmpty && currentTuple._2==0) accumulator
      else if(remaining.isEmpty) currentTuple :: accumulator
      else if (remaining.head == currentTuple._1)
        rleTailRec(remaining.tail,currentTuple.copy(_2=currentTuple._2+1) ,  accumulator)
      else rleTailRec(remaining.tail,(remaining.head,1), currentTuple::accumulator)
    }
    rleTailRec(this.tail,(this.head,1),RNil)
  }
/*
  [1,2].duplicateEach(3) =
  duplicateEachTailRec([2],1,0,[])
       if(remaining.isEmpty && nDuplications==k) accumulator
       else if(remaining.isEmpty) duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement:: accumulator)
       else if(nDuplications == k) duplicateEachTailRec(remaining.tail,remaining.head,0,accumulator)
       none of the conditions are met so we will go to the else branch
       duplicateEachTailRec([2],1,1,[1,])
       if(remaining.isEmpty && nDuplications==k) accumulator
       else if(remaining.isEmpty) duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement:: accumulator)
       else if(nDuplications == k) duplicateEachTailRec(remaining.tail,remaining.head,0,accumulator)
       none of the conditions are met so we will go to the else branch
        duplicateEachTailRec([2],1,2,[1,1])

       if(remaining.isEmpty && nDuplications==k) accumulator
       else if(remaining.isEmpty) duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement:: accumulator)
       else if(nDuplications == k) duplicateEachTailRec(remaining.tail,remaining.head,0,accumulator)
       none of the conditions are met so we will go to the else branch
        duplicateEachTailRec([2],1,3,[1,1,1])

         else if(nDuplications == k) duplicateEachTailRec(remaining.tail,remaining.head,0,accumulator)
         this condition matches
          duplicateEachTailRec([],2,0,[1,1,1])

          if(remaining.isEmpty && nDuplications==k) accumulator
        else if(remaining.isEmpty) duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement:: accumulator)
       duplicateEachTailRec([],2,1,[1,1,1,2])

        if(remaining.isEmpty && nDuplications==k) accumulator
       else if(remaining.isEmpty) duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement:: accumulator)
        duplicateEachTailRec([],2,2,[1,1,1,2,2])

        if(remaining.isEmpty && nDuplications==k) accumulator
       else if(remaining.isEmpty) duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement:: accumulator)
        duplicateEachTailRec([],2,3,[1,1,1,2,2,2])
         if(remaining.isEmpty && nDuplications==k) accumulator
         hence o/p is [1,1,1,2,2,2]
         Complexity is O(N*K) because N*K is the dimension of the resultant list
         here N is the length of Current list and K is the number of times we need to duplicate
 */
  override def duplicateEach(k: Int): RList[T] = {
    @tailrec
    def duplicateEachTailRec(remaining:RList[T],currentElement:T,nDuplications:Int, accumulator:RList[T]):RList[T] ={
       if(remaining.isEmpty && nDuplications==k) accumulator.reverse
       else if(remaining.isEmpty) duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement:: accumulator)
       else if(nDuplications == k) duplicateEachTailRec(remaining.tail,remaining.head,0,accumulator)
       else duplicateEachTailRec(remaining,currentElement,nDuplications+1,currentElement::accumulator)
    }
    duplicateEachTailRec(this.tail,this.head,0,RNil)
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
    val list3: RList[Int] = 1 :: 1 :: 1 :: 2 :: 3 :: 3 :: 4 :: 5 :: 5 :: 5 :: RNil
    val listz= list ++ list1
    val iterable: immutable.Seq[Int] = 1 to 10000
    Iterable.apply(2,3,4)
    val aLargeList= RList.from(1 to 10000)
    // TODO This expression is right associative by default in scala
    // TODO RNil.::3.::2.:: 1 == 1 :: 2 :: 3 :: RNil
    println(listOfIntegers)
    def testEasyFunctions: Unit ={
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
      println(listz.removeAt(4))
      println(list.map(_+1))
      val time= System.currentTimeMillis
      val x= aLargeList.flatmap(x => x :: (2*x)::RNil)
      println(System.currentTimeMillis()-time)
      println(aLargeList.filter(_%2==0))
      println(list3.rle)
    }
    def testMedium(): Unit ={
      println(list.duplicateEach(3))
    }
    testEasyFunctions
    testMedium()
  }
}
