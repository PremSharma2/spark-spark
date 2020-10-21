package partialFunctions

import partialFunctions.FunctionalCollection.{EmptySet, MySet}

object AllinclusiveSet1 extends App {

  class AllInclusiveSet[A] extends MySet[A] {
    override def contains(element: A): Boolean = true

    override def +(element: A): MySet[A] = this

    override def ++(element: MySet[A]): MySet[A] = this

    // allinclusiveSet[Int]= all natural numbers
    //as allinclusiveSet is inifinite set basically so we can't implement the
    //map, flatmap,
    override def map[B](fx: A => B): MySet[B] = ???

    override def flatMap[B](fx: A => MySet[B]): MySet[B] = ???

    override def filter(predicate: A => Boolean): MySet[A] = ???

    override def forEach(fx: A => Unit): Unit = ???

    override def -(element: A): MySet[A] = ???

    override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

    override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

    override def unary_! : MySet[A] = new EmptySet[A]
  }

}
