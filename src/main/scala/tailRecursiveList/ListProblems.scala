package tailRecursiveList

import caseClass.Factory.Animal.{Animal, Dog}

import scala.annotation.tailrec
import scala.util.Random

object ListProblems {

  sealed trait RList[+T] {
    /**
     * easy Impls
     *
     * @return
     */
    def head: T

    def tail: RList[T]

    def isEmpty: Boolean

    def headOption: Option[T]

    override def toString: String = "[]"

    // todo prepend operator
    def ::[S >: T](element: S): RList[S] = new Node(element, this)

    def apply(index: Int): T

    def length: Int

    def reverse: RList[T]

    def ++[S >: T](anotherList: RList[S]): RList[S]

    def removeAt(index: Int): RList[T]

    def map[S](f: T => S): RList[S]

    //ETW pattern
    def flatmap[S](f: T => RList[S]): RList[S]

    def filter(f: T => Boolean): RList[T]

    def distinct[S >: T](ls: RList[S]): RList[S]

    def contains[S >: T](elem: S): Boolean

    /**
     * Medium and difficult Impls
     */
    def rle: RList[(T, Int)]

    // duplicate
    def duplicateEach(k: Int): RList[T]

    //rotation by number of positions to the left
    def rotate(k: Int): RList[T]

    def sample(k: Int): RList[T]

    /**
     * Hard Problems
     *
     * @param ordering
     * @tparam S
     * @return: RList[S]
     */
    // sorting the list in the order defined by Ordering object
    def insertionSort[S >: T](ordering: Ordering[S]): RList[S]

    def mergeSort[S >: T](ordering: Ordering[S]): RList[S]

    def quickSort[S >: T](ordering: Ordering[S]): RList[S]
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

    override def rotate(k: Int): RList[Nothing] = RNil

    override def sample(k: Int): RList[Nothing] = RNil

    /**
     * Hard Problems
     *
     * @param ordering : Ordering[S]
     * @tparam S
     * @return: RList[S]
     */
    override def insertionSort[S >: Nothing](ordering: Ordering[S]): RList[S] = RNil

    override def mergeSort[S >: Nothing](ordering: Ordering[S]): RList[S] = RNil

    override def distinct[S >: Nothing](ls: RList[S]): RList[S] = RNil

    override def contains[S >: Nothing](elem: S): Boolean = false

    override def quickSort[S >: Nothing](ordering: Ordering[S]): RList[S] = RNil
  }

  //TODO here as we can see that def can be overridden as val
  /*
   TODO
        Overriding Fields
        Fields, as opposed to methods, have a special property that
        they can be overridden in the constructor directly as well.
   */
  case class Node[+T](override val head: T, override val tail: RList[T]) extends RList[T] {
    override def isEmpty: Boolean = false

    override def headOption: Option[T] = Some(head)

    override def toString: String = {

      @tailrec
      def toStringTailRecursion(remaining: RList[T], accumlator: String): String = {
        if (remaining.isEmpty) accumlator
        else if (remaining.tail.isEmpty) s"$accumlator ${remaining.head}"
        // recursive call  is the last expression in code branch basically
        else toStringTailRecursion(remaining.tail, s"$accumlator ${remaining.head}, ")
      }

      "[" + toStringTailRecursion(this, "") + "]"
    }

    override def apply(index: Int): T = {
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
      def tailRecApply(remaining: RList[T], currentIndexAccumlator: Int): T = {
        if (currentIndexAccumlator == index) remaining.head
        else tailRecApply(remaining.tail, currentIndexAccumlator + 1)
      }

      if (index < 0) throw new NoSuchElementException
      else tailRecApply(this, 0)
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
      def lengthTailRec(remaining: RList[T], accumulator: Int): Int = {
        if (remaining.isEmpty) accumulator
        else lengthTailRec(remaining.tail, accumulator + 1)
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
      def reverseTailRec(remaining: RList[T], accumulator: RList[T]): RList[T] = {
        if (remaining.isEmpty) accumulator
        else reverseTailRec(remaining.tail, remaining.head :: accumulator)
      }

      reverseTailRec(this, RNil)
    }

    // TODO this is not tail recursive because here recursive call is not the last thing  here
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
    override def ++[S >: T](anotherList: RList[S]): RList[S] = {
      def concatTailRec(remaining: RList[S], accumulator: RList[S]): RList[S] = {
        if (remaining.isEmpty) accumulator
        else concatTailRec(remaining.tail, remaining.head :: accumulator)
      }

      concatTailRec(this.reverse, anotherList)
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
      def removeAtTailRec(remaining: RList[T], currentIndex: Int, predecessor: RList[T]): RList[T] = {
        if (currentIndex == index) predecessor.reverse ++ remaining.tail
        else if (remaining.isEmpty) predecessor.reverse
        else removeAtTailRec(remaining.tail, currentIndex + 1, remaining.head :: predecessor)
      }

      removeAtTailRec(this, 0, RNil)
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
      def mapTailRec(remaining: RList[T], accumulator: RList[S]): RList[S] = {
        if (remaining.isEmpty) accumulator.reverse
        else mapTailRec(remaining.tail, f(remaining.head) :: accumulator)
      }

      mapTailRec(this, RNil)
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
      def flatmapTailRec(remaining: RList[T], accumulator: RList[S]): RList[S] = {
        if (remaining.isEmpty) accumulator.reverse
        else flatmapTailRec(remaining.tail, f.apply(remaining.head).reverse ++ accumulator)
      }
      /*
       TODO
          What we are trying to do here is that
          [1,2,3].flatmap(x => [x,2*x]) = concatinateall([[6,4],[4,2],[2,1],[]])
          i.e we need to get list of list first in reverse order
            [1,2,3].flatmap(x => [x,2*x]) = betterflatmap([1,2,3],[])

        TODO
            betterflatmap([1,2,3],[])
            will go into else branch
             betterflatmap([2,3],[[1,2]])
               will go into else branch
            betterflatmap([3],[[4,2],[2,1]])
             will go into else branch
             betterflatmap([],[[6,3],[4,2],[2,1])
             it will go if branch
             concatenateAll([[6,3], [4,2], [2,1]], [],[])
             it will go else if(currentList.isEmpty) concatenateAll(elements.tail,elements.head,accumulator) branch
             concatenateAll([ [4,2], [2,1]], [6,3],[])
             it will go to else branch else  else concatenateAll(elements, currentList.tail, currentList.head :: accumulator)
              concatenateAll([[4,2], [2,1]], [3],[6])
              it will go to else branch
             concatenateAll([[4,2], [2,1]], [],[3,6])
             it will go else if(currentList.isEmpty) concatenateAll(elements.tail,elements.head,accumulator) branch
                concatenateAll([[2,1]], [4,2],[3,6])
                it will go to else branch
                concatenateAll([[2,1]], [2],[4,3,6])
                 it will go to else branc h
                  concatenateAll([[2,1]], [],[2,4,3,6])
                it will go else if(currentList.isEmpty) concatenateAll(elements.tail,elements.head,accumulator) branch
                 concatenateAll([[]], [2,1],[2,4,3,6])
                  it will go to else branch
                    concatenateAll([[]], [1],[2,2,4,3,6])
                     it will go to else branch
                     concatenateAll([[]], [],[1,2,2,4,3,6])
                     it will go to if branch and return accumulator= [1,2,2,4,3,6]

                     TODO
                       Complexity for concatenateAll is O(z)
                        Complexity for betterflatmap is O(n)
                        so total complexity is O(n+z)
       */

      @tailrec
      def betterFlatMap(remaining: RList[T], accumulator: RList[RList[S]]): RList[S] = {
        if (remaining.isEmpty) concatenateAll(accumulator, RNil, RNil)
        else betterFlatMap(remaining.tail, f(remaining.head).reverse :: accumulator)
      }

      @tailrec
      def concatenateAll(elements: RList[RList[S]], currentList: RList[S], accumulator: RList[S]): RList[S] = {
        if (currentList.isEmpty && elements.isEmpty) accumulator
        else if (currentList.isEmpty) concatenateAll(elements.tail, elements.head, accumulator)
        else concatenateAll(elements, currentList.tail, currentList.head :: accumulator)
      }

      betterFlatMap(this, RNil)

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
      def filterTailRec(remaining: RList[T], accumulator: RList[T]): RList[T] = {
        if (remaining.isEmpty) accumulator.reverse
        else if (predicate.apply(remaining.head)) filterTailRec(remaining.tail, remaining.head :: accumulator)
        else filterTailRec(remaining.tail, accumulator)
      }

      filterTailRec(this, RNil)
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
      def rleTailRec(remaining: RList[T], currentTuple: (T, Int), accumulator: RList[(T, Int)]): RList[(T, Int)] = {
        if (remaining.isEmpty && currentTuple._2 == 0) accumulator
        else if (remaining.isEmpty) currentTuple :: accumulator
        else if (remaining.head == currentTuple._1)
          rleTailRec(remaining.tail, currentTuple.copy(_2 = currentTuple._2 + 1), accumulator)
        else rleTailRec(remaining.tail, (remaining.head, 1), currentTuple :: accumulator)
      }

      rleTailRec(this.tail, (this.head, 1), RNil)
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
           else if(nDuplications == k) du plicateEachTailRec(remaining.tail,remaining.head,0,accumulator)
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
      def duplicateEachTailRec(remaining: RList[T], currentElement: T, nDuplications: Int, accumulator: RList[T]): RList[T] = {
        if (remaining.isEmpty && nDuplications == k) accumulator.reverse
        else if (remaining.isEmpty) duplicateEachTailRec(remaining, currentElement, nDuplications + 1, currentElement :: accumulator)
        else if (nDuplications == k) duplicateEachTailRec(remaining.tail, remaining.head, 0, accumulator)
        else duplicateEachTailRec(remaining, currentElement, nDuplications + 1, currentElement :: accumulator)
      }

      duplicateEachTailRec(this.tail, this.head, 0, RNil)
    }

    /*
    TODO
      if(remaining.isEmpty && rotationsLeft==0) this
      this condition i have used here becuase let say
      [1,2,3].rotate(3) where k is divisible by the length of List
      so [1,2,3].rotate(3) == [1,2,3]
      [1,2,3].rotate(6) == [1,2,3]
       else if(remaining.isEmpty) rotateTailRec(this,rotationsLeft,RNil)
       this condition will occur when
        [1,2,3].rotate(4) =
        then we have traversed all the list and all rotated elements are stored in accumulator
        i.e [1,2,3].rotateTailRec(3) == [1,2,3]
        at this stage remaining is Empty but still
         still  1 rotation is pending so what we will do this.(1)
        i.e [1,2,3].rotateTailRec(1)

        TODO
         Lets run through some examples
          [1,2,3].rotate(1) = this will call to
          rotateTailRec([1,2,3],1,R[])
          we will get into this case: else rotateTailRec(remaining.tail,rotationsLeft-1,remaining.head :: accumulator)
          rotateTailRec([2,3],0,[1])
          now will get into this case: else if(rotationsLeft==0) remaining ++ accumulator.reverse
           [2,3] ++ [1] = [2,3,1] this is the o/p
           because [3,2] it get reversed in ++


           TODO
            Now lets take an example of [1,2,3].rotate(3)
            rotateTailRec([1,2,3],3,[])
              we will get into this case: else rotateTailRec(remaining.tail,rotationsLeft-1,remaining.head :: accumulator)
               rotateTailRec([2,3],2,[1])
            we will get into this case: else rotateTailRec(remaining.tail,rotationsLeft-1,remaining.head :: accumulator)
            rotateTailRec([3],1,[2,1])
            we will get into this case: else rotateTailRec(remaining.tail,rotationsLeft-1,remaining.head :: accumulator)
             rotateTailRec([],0,[3,2,1])
              if(remaining.isEmpty && rotationsLeft==0) this
              so we will get the original list
              [1,2,3]

        TODO
             Now lets take the case [1,2,3].rotate(4)
              rotateTailRec([1,2,3],4,[])
              we will get inti else part
              rotateTailRec([2,3],3,[1])
               we will get inti else part
                rotateTailRec([3],2,[2,1])
                 we will get inti else part
                  rotateTailRec([],1,[3,2,1])
                  now we will get into  else if(remaining.isEmpty) rotateTailRec(this,rotationsLeft,RNil)
                  rotateTailRec([1,2,3],1,[])
                  we will get inti else part
                   rotateTailRec([2,3],0,[1])
                   now we will get into  else if(rotationsLeft==0) remaining ++ accumulator.reverse
                   [2,3] ++ [1] but it will turned into by ++ def [3,2] ++ [1] = [2,3,1]
                   Complexity(O(max(N,K))
                   becase if k<N
                   then complexity will depend upon  else if(rotationsLeft==0) remaining ++ accumulator.reverse this case
                   and this has the complexity O(M+N)
                   and if k>N then we need to iterate till if(remaining.isEmpty && rotationsLeft==0) case
     */
    override def rotate(k: Int): RList[T] = {
      @tailrec
      def rotateTailRec(remaining: RList[T], rotationsLeft: Int, accumulator: RList[T]): RList[T] = {
        if (remaining.isEmpty && rotationsLeft == 0) this
        else if (remaining.isEmpty) rotateTailRec(this, rotationsLeft, RNil)
        else if (rotationsLeft == 0) remaining ++ accumulator.reverse
        else rotateTailRec(remaining.tail, rotationsLeft - 1, remaining.head :: accumulator)
      }

      rotateTailRec(this, k, RNil)
    }

    /*
    TODO
      [1,2,3,4,5]. sample(3) =
      sampleTailRec(3,[])
      will go else branch
      val index = random.nextInt(maxIndex) ==1 lets assume
      val newNumber= this.apply(index)  ==2
       sampleTailRec(2,[2])
       will go else branch
      val index = random.nextInt(maxIndex) ==2 lets assume
      val newNumber= this.apply(index)  ==3
      sampleTailRec(1,[3,2])
      will go else branch
      val index = random.nextInt(maxIndex) ==3 lets assume
      val newNumber= this.apply(index)  ==4
       sampleTailRec(0,[4,3,2])
       we will go  if(nRemaining==0) accumulator branch
       and return accumulator over here
       Complexity is O(N*K)
       because for each  val newNumber= this.apply(index) the complxity is O(N)
       and we doing k samples then it should be O(N*K)
     */
    override def sample(k: Int): RList[T] = {
      val random = new Random(System.currentTimeMillis())
      val maxIndex = this.length

      @tailrec
      def sampleTailRec(nRemaining: Int, accumulator: RList[T]): RList[T] = {
        if (nRemaining == 0) accumulator
        else {
          // deriving the index where i need to generate the sample value
          val index = random.nextInt(maxIndex)
          val newNumber = this.apply(index)
          sampleTailRec(nRemaining - 1, newNumber :: accumulator)
        }
      }

      def sampleElegent = {
        RList.
          from((1 to k).
            map(_ => random.nextInt(maxIndex)) // generating Rlist of random indexes
            .map(index => this.apply(index))) // mapping these indexes with values
      }

      if (k < 0) RNil
      else sampleElegent
    }

    /**
     * Hard Problems
     *
     * @param ordering : Ordering[S]
     * @tparam S
     * @return: RList[S]
     */
    override def insertionSort[S >: T](ordering: Ordering[S]): RList[S] = {
      /*
       insertSorted(4,[],[1,2,3,5])
       here check is 4>1 so it will stay before 4 we will add up in before list
       insertSorted(4,[1],[2,3,5])
        here check is 4>2 so it will stay before 4 we will add up in before list
       insertSorted(4,[3,2,1],[5])
        here check is 4>3 so it will stay before 4 we will add up in before list
        insertSorted(4,[3,2,1],[4,5])
        here check is 4<5 so it will stay after 4 we will add up in after list
        [3,2,1].reverse ++ (4 :: 5)
        [1,2,3,4,5]
        complexity is O(n) where n is the size if after list because we dont know where this element will be placed
        we need to traverse the whole after list
       */
      @tailrec
      def insertSorted(element: T, before: RList[S], after: RList[S]): RList[S] = {
        //insertSorted(4,[3,2,1],[4,5]) we are in this stage
        if (after.isEmpty || ordering.lteq(element, after.head)) before.reverse ++ (element :: after)
        else insertSorted(element, after.head :: before, after.tail)
      }

      /*
      pseudo code for the algo i.e our approach should be like that

       [3,1,4,2].sorted = insertSortTailRec([3,1,4,2,5],[])
                        insertSortTailRec([1,4,2,5],[3])
                        insertSortTailRec([4,2,5],[1,3])
                         insertSortTailRec([2,5],[1,3,4])
                         insertSortTailRec([5],[1,2,3,4])
                         insertSortTailRec([],[1,2,3,4,5])
                         now return accumulator
                         Complexity is O(n^2) because the insertSortTailRec calls
                         insertSorted n times for each single iteration
       */
      @tailrec
      def insertSortTailRec(remaining: RList[T], accumulator: RList[S]): RList[S] = {
        if (remaining.isEmpty) accumulator
        else insertSortTailRec(remaining.tail, insertSorted(remaining.head, RNil, accumulator))
      }

      insertSortTailRec(this, RNil)
    }

    override def mergeSort[S >: T](ordering: Ordering[S]): RList[S] = {
      /*
      As merge sorting approach we now that it breaks the list into two parts
      left and right it means left contains all elements less then the middle element and
      after contains all elements which are greater then middle element let say 3
       merge([1,2,3],[4,5,6],[]) = ......
       .....
        [1,2,3,4,5,6]
    TODO
        Merge Explanation
        merge([1,3], [2,4,5,6,7]) =
        now else if(ordering.lteq(listA.head,listB.head)) says 1<2
        merge([3], [2,4,5,6,7] , [1])
        now else case
        merge([3], [4,5,6,7] , [2,1])
        now else if(ordering.lteq(listA.head,listB.head)) says 3<4
         merge([], [4,5,6,7] , [3,2,1])
         now  if(listA.isEmpty) accumulator.reverse ++ listB
         o/p will [1,2,3,4,5,6,7]
       */
      @tailrec
      def merge(listA: RList[S], listB: RList[S], accumulator: RList[S]): RList[S] = {
        if (listA.isEmpty) accumulator.reverse ++ listB
        else if (listB.isEmpty) accumulator.reverse ++ listA
        else if (ordering.lteq(listA.head, listB.head)) merge(listA.tail, listB, listA.head :: accumulator)
        else merge(listA, listB.tail, listB.head :: accumulator)
      }

      /*
     TODO
      [3,1,2,4,5] => [[3],[1],[2],[5],[4]]
       and then i will pass this lst to mergeSortTailRec
        what i will do now i will pick two elements from list of list and will store them in small list
       and big list will remain empty for first call
  TODO
       mergeSortTailRec([[3],[1],[2],[5],[4]],[])
       now inside mergeSortTailRec i will pick two elements from smallList
       and will pass them to merge i.e
       merge([3],[1],[]) and job of merge is sort the left list and right list and merge them
       so it will return [1,3]
       now i will make a tail recursive call to mergeSortTailRec with o/p of merge method
       i wil place this o/p in bigList
  TODO
       mergeSortTailRec([[2],[5],[4]],[[1,3]])
       now again inside mergeSortTailRec i will pick two elements from smallList
       and will pass them to merge i.e
       merge([2],[5],[]) and job of merge is sort the left list and right list and merge them
       so it will return [2,5]
       now i will make a tail recursive call to mergeSortTailRec with o/p of merge method
       i wil place this o/p in bigList i.e prepended to the earlier version of big list
       i.e [[2,5],[1,3]]
  TODO
       mergeSortTailRec([[4]],[[2,5],[1,3]])
       now as we can see that there are no more elents left only [4] is there
       so we will not call merge now we will prepend this list to bigList and make one more recursive call to
       mergeSortTailRec

    TODO
       mergeSortTailRec([],[[4],[2,5],[1,3]])
       now when i get the smallList.isEmpty i will swap the list around i.e bigList with smalllist
       so smallList==[[4],[2,5],[1,3]] after swapping
       and will make one more recursive call
     TODO
        mergeSortTailRec([[4],[2,5],[1,3]],[])
        now again i will pick two elements from smallList and will merge them
        so two elemnts are [4],[2,5] i will call merge to merge them
        merge([4],[2,5],[]) i will get [2,4,5] i will use this o/p now to make recursive call
        to
      TODO
        mergeSortTailRec([[1,3]],[[2,4,5]])
        now inside mergeSortTailRec as we can see that there no two list to merge
        we will simply prepend the head of small list to bigList and again will make recursive call

     TODO
        mergeSortTailRec([],[[1,3],[2,4,5]])
        now i will swap the arguments one more time
        smallList == [[1,3],[2,4,5]] and
        bigList == []
        and make one more recursive call

    TODO
          mergeSortTailRec([[1,3],[2,4,5]],[])
          now again i will pick first two elements from smallList
          and will pass to merge method to merge them
          merge([1,3],[2,4,5]) ==> [1,2,3,4,5]
          and will make one more recursive call mergeSortTailRec

     TODO
            mergeSortTailRec([],[[1,2,3,4,5]])
            now as we can see that i can see there is only one list in List of list
            named bigList:RList[RList[S]] and smallList is Empty now
            so thats what i wanted now i will simply going to return [1,2,3,4,5]
            Complexity:
            As we know that complexity of merge sort O(n*log(n))
            because complexity(n) = 2 * complexity(n/2) + n
            because first we half the list so its size is n/2
            and + n means merge function complexity is O(n)
            this mathematical computation leads to O(n*log(n))
       */
      @tailrec
      def mergeSortTailRec(smallList: RList[RList[S]], bigList: RList[RList[S]]): RList[S] = {
        if (smallList.isEmpty) {
          if (bigList.isEmpty) RNil
          else if (bigList.tail.isEmpty) bigList.head
          else mergeSortTailRec(bigList, RNil)
        } else if (smallList.tail.isEmpty) {
          if (bigList.isEmpty) smallList.head
          else mergeSortTailRec(smallList.head :: bigList, RNil)
        }
        else {
          val first = smallList.head
          val second = smallList.tail.head
          val merged = merge(first, second, RNil)
          mergeSortTailRec(smallList.tail.tail, merged :: bigList)
        }
      }

      mergeSortTailRec(this.map(x => x :: RNil), RNil)
    }

    override def contains[S >: T](elem: S): Boolean = {
      @tailrec
      def containsTailRec(remaining: RList[S]): Boolean = {
        if (remaining.isEmpty) false
        else if (remaining.head == elem) true
        else containsTailRec(remaining.tail)
      }

      containsTailRec(this)
    }

    override def distinct[S >: T](ls: RList[S]): RList[S] = {
      @tailrec
      def distinctTailRec(remaining: RList[S], accumulator: RList[S]): RList[S] = {
        if (remaining.isEmpty) accumulator
        else if (accumulator.contains(remaining.head)) distinctTailRec(remaining.tail, accumulator)
        else distinctTailRec(remaining.tail, remaining.head :: accumulator)
      }

      distinctTailRec(this, RNil)
    }

    /*
     TODO
         Understanding partition method:
         partition([1,2,3,4,5],3,[],[]) = it will return this tuple ([1,2,3],[4,5] )

     TODO
         quickSort understanding and internal mechanics
         [3,1,2,5,4].quickSort
          first of all i need to convert this list into List[List]]
          now i will call quickSortTailRec with this List[List]]
           quickSortTailRec(remainingLists:RList[RList[T]], accumulator:RList[RList[T]])
           quickSortTailRec([[3,1,2,5,4]],[]) =
           now as we can see that remainingLists = [[3,1,2,5,4]]
           is not Empty then we will do partitioning first
           i will call partition  first   [3,1,2,5,4].partition(  [1,2,5,4],3 , [] , [])
           while calling partition i have head of this list as pivot
          and it will result the o/p  -> ([1,2],[4,5])
          now again i will make recursive call to quickSortTailRec
          so what i will do i will put the
           [1,2] and [5,4] and pivot ==3 as well into this remainingLists
           while making a recursive call

      TODO
          quickSortTailRec([[1,2],[3],[5,4]],[])
          again inside the quickSortTailRec
          what i will do  i will partition the head of this list
          i.e [1,2] i will make pivot 1
          partition([2],1,[],[]) -> ([],[2])
          this is the o/p now i will use this o/p

    TODO
          now i will extract this list out and put back the o/p of partition
          into the remainingList while making the recursive call
          [[1,2],[3],[5,4]]
          i added  smallerlist==[] , pivot == [1] and largerList == [2]
          into the remainingList and accumlator is empty
          quickSortTailRec([[],[1],[2],[3],[5,4]],[])

    TODO   now again i will make the recursive call
           as we can see that elements are List are single element and
            first element is empty list so i will skip the head and make a recursive call
            now after skipping the head
           so i will add the current head  into accumulator
           quickSortTailRec([[1],[2],[3],[5,4]],[])

    TODO   now again i will make the recursive call
           and will add the first element to the   accumulator
            quickSortTailRec([[2],[3],[5,4]],[[1]])

    TODO   now again i will make recursive call
            quickSortTailRec([[3],[5,4]],[[2],[1]])


    TODO   now again i will make recursive call
            quickSortTailRec([[5,4]],[[3],[2],[1]])
            now as we can see that this first element is neither Empty nor single element
            then we will use partitioning here inside quickSortTailRec
            partition([4],5,[],[]) -> ([4],[]) this will be the o/p
            now before making the recursive call here we will extract the items from
            list and will put the o/p of partitioning into the list
            quickSortTailRec([[4],[5],[]],[[3],[2],[1]])


    TODO   now again i will make the recursive call
           as we can see that elements are List are single element and
            third element is empty list
           so i will add  that into accumulator
          quickSortTailRec([[]],[[5],[4],[3],[2],[1]])
          now again as we can see that list is Empty now
          we will return the accumulator with flatten it and reverse it
          [1,2,3,4,5]


    TODO : Lets understand the mechanics of  partition over here
           partition([1,2,5,4],3,[],[])
           partition([2,5,4],3,[1],[])
           partition([5,4],3,[2,1],[])
           partition([4],3,[2,1],[5])
           partition([],3,[2,1],[4,5])
           now list is Empty then return this tuple ([2,1],[4,5])
           Complexity is O(n^2) for worst case when list already sorted
           for anaavreahge O(N * log(N))
     */
    override def quickSort[S >: T](ordering: Ordering[S]): RList[S] = {
      @tailrec
      def partition(list: RList[T], pivot: T, smallerList: RList[T], largerList: RList[T]): (RList[T], RList[T]) = {
        if (list.isEmpty) (smallerList, largerList)
        else if (ordering.lteq(list.head, pivot)) partition(list.tail, pivot, list.head :: smallerList, largerList)
        else partition(list.tail, pivot, smallerList, list.head :: largerList)
      }

      @tailrec
      def quickSortTailRec(remainingLists: RList[RList[T]], accumulator: RList[RList[T]]): RList[T] = {
        if (remainingLists.isEmpty) accumulator.flatmap(smallList => smallList).reverse
        else if (remainingLists.head.isEmpty) quickSortTailRec(remainingLists.tail, accumulator)
        else if (remainingLists.head.tail.isEmpty) quickSortTailRec(remainingLists.tail, remainingLists.head :: accumulator)
        else {
          val list = remainingLists.head
          val pivot = list.head
          val listToSplit = list.tail
          val (smaller, larger) = partition(listToSplit, pivot, RNil, RNil)
          quickSortTailRec(smaller :: (pivot :: RNil) :: larger :: remainingLists.tail, accumulator)
        }
      }

      quickSortTailRec(this :: RNil, RNil)
    }
  }


  object RList {
    def from[T](iterable: Iterable[T]): RList[T] = {
      @tailrec
      def convertToRListTailRec(remaining: Iterable[T], accumulator: RList[T]): RList[T] = {
        if (remaining.isEmpty) accumulator
        else convertToRListTailRec(remaining.tail, remaining.head :: accumulator)
      }

      convertToRListTailRec(iterable, RNil).reverse
    }
  }

  def main(args: Array[String]): Unit = {
    val listOfIntegers: RList[Int] = Node(1, new Node(2, new Node(3, RNil)))

    val list: RList[Int] = 1 :: 2 :: 3 :: RNil
    val list1: RList[Int] = 4 :: 5 :: 6 :: RNil
    val list3: RList[Int] = 1 :: 1 :: 1 :: 2 :: 3 :: 3 :: 4 :: 5 :: 5 :: 5 :: RNil
    val listz = list ++ list1
    val iterable: Iterable[Int] = 1 to 10000
   val iterable1: Iterable[Int] = Iterable.apply(2, 3, 4)

    val aLargeList = RList.from(1 to 10000)
    // TODO This expression is right associative by default in scala
    // TODO RNil.::3.::2.:: 1 == 1 :: 2 :: 3 :: RNil
    println(listOfIntegers)

    def testEasyFunctions: Unit = {
      def testApi(list: RList[Animal]) = {
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
      println(list.map(_ + 1))
      println(aLargeList.filter(_ % 2 == 0))
      println(list3.rle)
    }

    def testMedium(): Unit = {
      println(list.duplicateEach(3))
      println(list.rotate(4))
      println(list.rotate(6))
      val onToTen = RList.from(1 to 10)
      println(onToTen.sample(3))
      println("calculating flatmap")
      val time = System.currentTimeMillis
      aLargeList.flatmap(x => x :: (2 * x) :: RNil)
      println(System.currentTimeMillis() - time)
    }

    def testhardFunctions = {
      val list1: RList[Int] = 5 :: 4 :: 6 :: 1 :: 2 :: RNil
      implicit val ordering: Ordering[Int] = Ordering.fromLessThan[Int](_ < _)
      val time = System.currentTimeMillis
      println(list1.insertionSort(ordering))
      println(System.currentTimeMillis() - time)
      println(list1.mergeSort(ordering))
      // testing the edge case
      println(3 :: RNil.mergeSort(ordering))
      println(list1.quickSort(ordering))
    }

    testEasyFunctions
    testMedium()
    testhardFunctions

  }
}
