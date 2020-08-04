package partialFunctions

object FunctionalCollection extends App {

  /*
    Exercise implement aFunctional Set
    (A => Boolean) it is same as Function1[A,Boolean]
    Hence Set is a function
     */
  trait MySet[A] extends (A => Boolean) {
    def apply(element: A): Boolean = contains(element)

    def contains(element: A): Boolean

    def +(element: A): MySet[A]

    def ++(element: MySet[A]): MySet[A] // this is called union operator
    def map[B](fx: A => B): MySet[B]

    def flatMap[B](fx: A => MySet[B]): MySet[B]

    def filter(predicate: A => Boolean): MySet[A]

    def forEach(fx: A => Unit): Unit

    def -(element: A): MySet[A]

    def &(anotherSet: MySet[A]): MySet[A] // intersection
    def --(anotherSet: MySet[A]): MySet[A] // diffrence
    def unary_! : MySet[A]

  }

  class EmptySet[A] extends MySet[A] {
    override def contains(element: A): Boolean = false

    override def +(element: A): MySet[A] = new NonEmptySet[A](element, this)

    override def ++(element: MySet[A]): MySet[A] = element

    override def map[B](fx: A => B): MySet[B] = new EmptySet[B]

    override def flatMap[B](fx: A => MySet[B]): MySet[B] = new EmptySet[B]

    override def filter(predicate: A => Boolean): MySet[A] = this

    def forEach(fx: A => Unit): Unit = ()

    override def -(element: A): MySet[A] = this

    override def &(anotherSet: MySet[A]): MySet[A] = this

    override def --(anotherSet: MySet[A]): MySet[A] = this

    override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
  }

  class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
    override def contains(element: A): Boolean =
      element == head || tail.contains(element)

    override def +(element: A): MySet[A] =
      if (this contains element) this
      else new NonEmptySet[A](element, this)

    /*

[1,2,3] ++ [4,5]
[2,3] ++ [4,5] + 1
[3] ++ [4,5] + 1 +2
[] ++  [4,5]+1+2+3
[4,5]+1+2+3
[4,5,1,2,3]

or
[1,2,3].tail ++ [4,5] - first stack
[2,3].tail ++ [4,5,1] - second stack
[3].tail ++ [4,5,1,2] - third stack
EmptySet.tail ++  [4,5,1,2,3] - fourth stack
now recursion is tracing back
[]
[] ++ [4,5,1,2,3]
[4,5,1,2,3]


 */

    override def ++(anotherSet: MySet[A]): MySet[A] = {
      //this.tail ++ element + this.head
      var newSetAccumulator: MySet[A] = anotherSet + this.head
      this.tail ++ newSetAccumulator

    }

    /*

    [1,2,3].map(x=> x+1)
    accum= 2
    [2,3].map(fx) + 2
    [3].map(fx) + 3
    EmptySet .map(fx) + 4
    now recursion trace back
    []+ 4 = [4]
    []+ 4+ 3 = [4,3]
    [4,3] + 2= [2,3,4]
    [2,3,4]
     */
    override def map[B](fx: A => B): MySet[B] = {
      var accumulator: B = fx.apply(this.head)
      (this.tail.map(fx)) + accumulator
    }

    /*
    [1,2,3].flatMap(x=> MySet(x+1))
    [2,3].flatMap(fx) ++ [2]
    [3] .flatmap(fx) ++ [5] ++ [2]
    [].faltMAp(fx)  ++ [4] ++ [5] ++ [2]
    recursion will traceback now
    [] ++ [4,5,2]
    [4,5,2]
     */
    override def flatMap[B](fx: A => MySet[B]): MySet[B] = {
      var accumulator: MySet[B] = fx.apply(this.head)
      (tail.flatMap(fx)) ++ accumulator
    }

    override def filter(predicate: A => Boolean): MySet[A] = {

      val filteredTail = this.tail.filter(predicate)
      if (predicate(this.head)) filteredTail + head
      else filteredTail
    }

    def forEach(fx: A => Unit): Unit = {
      fx(head)
      tail.forEach(fx)
    }

    override def -(element: A): MySet[A] =
      if (head == element) tail
      else tail - element + head

    override def &(anotherSet: MySet[A]): MySet[A] =
    //filter(x => anotherSet.contains(x))
    //filter(x => anotherSet.apply(x))
      filter(anotherSet)

    override def --(anotherSet: MySet[A]): MySet[A] =
      filter(x => !anotherSet(x))

    // unary_! is define to negate the current Set it will return the property Set
    override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
  }

  // all elements of Type A in Allinclusive sets which satisfy the property
  //{ x in A | property(x)  } that means x which is of A type and satisfy this property
  // can only be added into this set
  class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
    override def contains(element: A): Boolean = property(element)

    //Set{ x in A | property(x)  } + element = { x in A | property(x)|| x==element}
    // this means that all elements are present in in this set should satisfy this property
    // and new element is being addes should be equal to already [resent element
    override def +(element: A): MySet[A] =
      new PropertyBasedSet[A](x => property(x) || x == element)

    // Set{ x in A | property(x)  } ++anotherSet =>
    // Set{ x in A | property(x) || anotherSet contains x }
    // o/p is here is the new set with new customized property
    // Hence output will be new set which either satisfies the property or passed argument set
    // should contains the same type element which are present in calling set
    // i.e property based set
    override def ++(anotherSet: MySet[A]): MySet[A] =
      new PropertyBasedSet[A](x => property(x) || anotherSet.contains(x))

    override def map[B](fx: A => B): MySet[B] = politlyFail

    override def flatMap[B](fx: A => MySet[B]): MySet[B] = politlyFail

    //that means set is going to hold th element if it satisfies the property and predicate
    override def filter(predicate: A => Boolean): MySet[A] =
      new PropertyBasedSet[A](x => property(x) && predicate(x))

    override def forEach(fx: A => Unit): Unit = politlyFail

    override def -(element: A): MySet[A] = filter(x => x != element)

    override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

    override def --(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

    // this is negate method it will return Property Based set
    override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

    def politlyFail: Nothing = throw new IllegalArgumentException("Its really deep hole")
  }

  object MySet {
    /*
    val s= MySet(1,2,3)= buildSet(seq(1,2,3),[])
          = buildSet(seq(2,3),[] + 1)
          = buildSet(seq(3),[] + 1 +2 )
          = buildSet([],[] + 1 + 2 + 3)
          = [1,2,3]
     */
    def apply[A](values: A*): MySet[A] = {

      def buildSet(valSeq: Seq[A], accumlator: MySet[A]): MySet[A] = {
        if (valSeq.isEmpty) accumlator
        else buildSet(valSeq.tail, accumlator + valSeq.head)
      }

      buildSet(values.toSeq, new EmptySet[A])
    }
  }

  val s = MySet(1, 2, 3)
  //s forEach(println)
  //s + 5 forEach(println)
  //s + 5 ++ MySet(-1,-3) forEach(println)
  // s + 5 ++ MySet(-1,-3) + 3 flatMap (x=> MySet(x,2*x)) forEach println
  // s.unary_!  it will give property based set with property that validate oppsite nature set
  //new PropertyBasedSet[A](x => !property(x))
  // and apply calls contain property(element)
  // so we are trying to add  2 intoProperty based set using apply method that in turn will call
  // contains which will refer to property of this PropertyBased Set
  val negetive: MySet[Int] = s.unary_! // all the natural numbers not equal to the [1,2,3,4]
  // i.e we negate the Set we changed the dimensions of set we returned the PropertySet here

  println(negetive.apply(2))
  println(negetive.apply(5))
  val negetiveEven = negetive.filter(_ % 2 == 0)
  println(negetiveEven(5))

}
