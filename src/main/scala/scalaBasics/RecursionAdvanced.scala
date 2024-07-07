package scalaBasics

import scala.annotation.tailrec
import scala.util.Random

object RecursionAdvanced {


  def insertionSort[A](list : List[A],comp : (A,A) => Int):List[A] = {

    def insert (element:A,rest :List[A]):List[A]= {
        if(rest.isEmpty) List(element)
        else if (comp(element,rest.head)<0) element :: rest
        else rest.head :: insert(element,rest.tail)
    }

    if (list.isEmpty || list.tail.isEmpty)  list
    else insert(list.head, insertionSort(list.tail,comp))
  }



  import scala.annotation.tailrec

  def tailRecInsertionSort[A](list: List[A], comp: (A, A) => Int): List[A] = {

    @tailrec
    def tailRecInsert(element: A, sorted: List[A], accumulator: List[A]): List[A] = sorted match {
      case Nil => (element :: accumulator).reverse
      case head :: tail =>
        if (comp(element, head) < 0) (element :: sorted ::: accumulator).reverse
        else tailRecInsert(element, tail, head :: accumulator)
    }

    @tailrec
    def sort(unsorted: List[A], sorted: List[A]): List[A] = unsorted match {
      case Nil => sorted
      case head :: tail => sort(tail, tailRecInsert(head, sorted, Nil))
    }

    sort(list, Nil)
  }



  def mergeSort[A](list : List[A],comp : (A,A) => Int):List[A] = {
    def merge(halfLeft:List[A],halfRight:List[A]) :List[A] = {
      if(halfLeft.isEmpty) halfRight
      else if(halfRight.isEmpty) halfLeft
      else if(comp(halfLeft.head,halfRight.head)<0) halfLeft.head :: merge(halfLeft.tail,halfRight)
      else  halfRight.head :: merge(halfLeft,halfRight.tail)
    }


    if (list.isEmpty || list.tail.isEmpty)  list
    else {
      //lets split the list in two parts
      val (hl,hr) = list.splitAt(list.length/2)
      val hlSorted= mergeSort(hl,comp)
      val hrSorted= mergeSort(hr,comp)
       merge(hlSorted,hrSorted)

    }

  }


  def quickSort[A](list : List[A],comp : (A,A) => Int): List[A] = {
    if (list.isEmpty || list.tail.isEmpty)  list
    else {
      val pivot= list.head
      //list1 : elements<pivot
      //list2 : elements>pivot
      //sort(list1) + pivot + sort(list2)

      //todo implement : ->  list1 : elements<pivot
      val (smaller,bigger)= list.tail.partition(x=> comp(x,pivot)< 0)
         quickSort(smaller,comp) ++ (pivot:: quickSort(bigger,comp))
    }
  }


  def main(args: Array[String]): Unit = {
    val randomList= (1 to 10).map(_ => Random.nextInt(10)).toList
    val naturalComparator : (Int,Int)=>Int = Ordering[Int].compare
    val sortedList=insertionSort(randomList,naturalComparator)
    println(sortedList)
  }
}
