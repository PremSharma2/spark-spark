package catz


import cats.{Eval, Monoid}

object FoldingTypeClass  {

  object ListFoldingExercise{
    def map[A,B](listA:List[A])(f: A=>B):List[B]={
       listA.foldRight(List.empty[B])((currentElement, accumlatorList) =>
         f(currentElement):: accumlatorList )
    }

    def flatMap[A,B](list:List[A])(f: A=>List[B]):List[B]={
      list.foldLeft(List.empty[B])(
        (accumlatorList,currentElement) => accumlatorList ++ f(currentElement))
    }

    def filter[A](list:List[A])(predicate: A=>Boolean):List[A]={
  list.foldRight(List.empty[A])(
    (currentElement, accumlatorList)=>
      if (predicate(currentElement)) currentElement :: accumlatorList else  accumlatorList)
    }
    def combineAll[A](list:List[A])(implicit monoid:Monoid[A]): A ={
    list.foldLeft(monoid.empty)(monoid.combine)
    }
  }
// TODO Cats also Provide type classes for this kind of operation called Foldable
   import cats.Foldable
  import cats.instances.list._ // implicit Foldable[Int]
  import cats.instances.option._ // implicit Foldable[Option]
  val foldabletypeclassinstance: Foldable[List] =Foldable.apply[List]

  val foldedResult: Int = foldabletypeclassinstance.
    foldLeft((List(1,2,3)),0)(_+_) //_+_ = (a,b) => a + b

  val foldabletypeclassinstanceforOption: Foldable[Option] =Foldable.apply[Option]

  val foldedResultOption: Int = foldabletypeclassinstanceforOption.
    foldLeft((Some(2)),0)(_+_) //_+_ = (a,b) => a + b

  // Here foldLeft is stack-safe regardless your Container
  // because we have use chained Eval which makes stake safe
  /*
   * TODO
        Computation performed in eval call by name expression
         is always lazy, even when called on an
   *    eager (Now) instance.
        Lazily perform a computation based on an Eval[A], using the
   *     function `f: A => B` for map operation to produce an Eval[B] given an A.
   *    This call is stack-safe -- many .flatMap calls may be chained
   *    without consumed additional stack during evaluation. It is also
   *   written to avoid left-association problems, so that repeated
   *    calls to .flatMap will be efficiently applied.
   */
  val sumFoldRight: Eval[Int] = foldabletypeclassinstance.
    foldLeft(List(1,2,3),Eval.now(0)){
      (eval,num) => eval.map(_+num)
    }
  import cats.instances.string._//Monoid[String]
  import cats.implicits.catsKernelStdGroupForInt
  /*
  def combineAll[A: Monoid](fa: F[A]): A = fold(fa)
  def fold[A](fa: F[A])(implicit A: Monoid[A]): A =
    foldLeft(fa, A.empty) { (acc, a) =>
      A.combine(acc, a)
    }
   */
  val anotherSum: Int =foldabletypeclassinstance.combineAll(List(1,2,3))

  /*
  def foldMap[A, B](fa: F[A])(f: A => B)(implicit B: Monoid[B]): B =
    foldLeft(fa, B.empty)((b, a) => B.combine(b, f(a)))
   */

  val mappedConcat: String = foldabletypeclassinstance.foldMap( List(1,2,3))(_.toString)

  //TODO : working with nested data structure
  import cats.instances.vector._
  //TODO Working with nested datastructures
  val nestedDataStructure = List(Vector(1,2,3),Vector(4,5,6))
  // Here we can combine two foldables
 val foldedDatastructure= Foldable[List] compose cats.Foldable[Vector]

  val combineValue: Int = foldedDatastructure.combineAll(nestedDataStructure)
   def main(args: Array[String]): Unit = {
     import ListFoldingExercise._
     import cats.instances.int._//Monoid[Int]
     val list = (1 to 4).toList
     println(map(list)(_+1))
     println(flatMap(list)(x=> List(x,x+1)))
     println(filter(list)(_ %2 == 0))
     println(combineAll(list))
  }

}
