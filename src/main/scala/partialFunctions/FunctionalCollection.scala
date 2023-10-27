package partialFunctions

object FunctionalCollection extends App {

  /*
    Exercise implement aFunctional Set
    (A => Boolean) it is same as Function1[A,Boolean]
    Hence Set is a function
    Hence we can pass the reference of MySet as input to any HOF(map, flatmap...etc)
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
// When you negate the EmptySet
// its output will be the EveryThing of Type A i.e all values of domain A
    // thats why we gave here _ => true or true we have returend
    //represents a set that contains all possible elements of type A (based on some domain or universal set concept).
    override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
  }

  class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
    override def contains(element: A): Boolean =
      //Here we have used || operator as an alternative to if and else condition
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

     override def ++ (anotherSet: MySet[A]): MySet[A] = {
      //this.tail ++ element + this.head
      var newSetAccumulator: MySet[A] = anotherSet + this.head
      this.tail ++ newSetAccumulator

    }

    /*
In this approach we will use existing set
and make the recursion call in a way that
when last call is made Seq is Empty
and when recursion traces back it start adding the transformed elements in accumulator
to the Seq returned by last stack at every stack we will add and at last finally all elements
will be addded
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
    In this approach we will use existing set
and make the recursion call in away that
when last call is made Seq is Empty
and when recursion traces back it start adding the transformed elements in accumulator
to the Seq returned by last stack at every stack we will add and at last finally all elements

    following is the demonstration of recursion
    [1,2,3].flatMap(x=> MySet(x+1))
    [2,3].flatMap(fx) ++ [2]
    [3] .flatmap(fx) ++ [5]
    [].flatmap(fx)  ++ [4]
    [] ++ [4]
    recursion will traceback now
    [] ++ [4]
    [4] ++ [5]
    [4,5] ++ [2]
    [4,5,2]
    [4,5,2] : FINAL RESULT AFTER RECURSION FINISHES
     */
    override def flatMap[B](fx: A => MySet[B]): MySet[B] = {
      var accumulator: MySet[B] = fx.apply(this.head)
      (tail.flatMap(fx)) ++ accumulator
    }

    /*
    In this approach we will use existing set
and make the recursion call in away that
when last call is made Seq is Empty
and now recursion traces back
[4,2,6].filter(%2==0)
[] + 6
[6] + 2
[6,2] + 4
[4,2,6]
     */
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

    override def &(anotherSet: MySet[A]): MySet[A] = {
      //filter(x => anotherSet.contains(x))
      //filter(x => anotherSet.apply(x))
      // here anotherset is also a function so we can pass this as refrence
      // it is like passing val anotherset : Function1= new Function1{
      //  apply()
   //}
    //
      filter(anotherSet)
    }

    override def --(anotherSet: MySet[A]): MySet[A] =
      filter(x => !anotherSet(x))

    // unary_! is define to negate the current Set it will return the property based Set
    // this will return the exact opposite to current i.e all -ve numbers
    // because we have negate the current set
    override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
  }

  //Property based sets are useful for defining infinite All inclusive sets
  // it could be infinite set
  // all elements of Type A are in domain of set in All inclusive sets which satisfy the property
  //{ x in A | property(x)  } that means x which is of A type and satisfy this property
  // can only be added into this set this is canonical definition of Property based set
  // PropertyBasedSet[A](property: A => Boolean) This is scala representation
  // of { x in A | property(x)  }
  // Canonical definition of property bases set
// This is infinite property based set this is replacement of AllInclusiveSet

  class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {

    // { x in A | property(x)  } in this method are verifying that this element is exist in this set or not
    // if it exist it has to satisfy the property
    override def contains(element: A): Boolean = property(element)

    //Set{ x in A | property(x)  } + element = { x in A | property(x)|| x==element}
    // this means that all elements are present in in this set should satisfy this property
    // and new element is being added should be similar to already present elements in set
    // Hence   canonical definition of Set is been changed now
    //e => property(e) || e == x effectively "extends" the set to
    // include x without affecting the original elements in the set that satisfy property(e).
    // This means that the new set contains all elements from the old set (as they satisfy property(e))
    // and also the element x.
    override def +(element: A): MySet[A] =
      new PropertyBasedSet[A](x => property(x) || x == element)

    // Set{ x in A | property(x)  } ++ anotherSet =>
    // Set{ x in A | property(x) || anotherSet contains x }
    // Set{ x in A | property(x) || anotherSet contains x }
    // o/p is here is the new set with new customized property
    // Hence output will be new set which either satisfies the property or passed argument set
    // should contains the same type element which are present in calling set
    // i.e property based set then only canonical definition will get complete

    /*
  TODO
       So the new PropertyBasedSet[A] contains all elements x
       that either satisfy the original property or are contained in anotherSet.
       In other words, the new set represents the union of the original set and anotherSet

     */
    override def ++(anotherSet: MySet[A]): MySet[A] =
      new PropertyBasedSet[A](x => property(x) || anotherSet.contains(x))
//all inclusive set i.e all integers of domain Int we apply map function x=> _%3
    // now we dont know whether is is infinite set or finite set
    // and if its finite then we dont know or whether it will satisfy the property
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
    // with opposite property thats how negate function works
    // if the original set is infinite set of even numbers then new set will be odd
    // numbered infinite set
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

  val s: MySet[Int] = MySet(1, 2, 3)
  s forEach(println)
  s + 5 forEach(println)
  s + 5 ++ MySet(-1,-3) forEach(println)
   s + 5 ++ MySet(-1,-3) + 3 flatMap (x=> MySet(x,2*x)) forEach println
  // s.unary_!  it will give property based set with property that validate opposite nature set
  //new PropertyBasedSet[A](x => !property(x))
  // and apply calls contain property(element)
  // so we are trying to add  2 intoProperty based set using apply method that in turn will call
  // contains which will refer to property of this PropertyBased Set
  val negetive: MySet[Int] = s.unary_! // all the natural numbers not equal to the [1,2,3,4]
  // i.e we negate the Set we changed the dimensions of set and  returned the PropertySet here
  //i.e this will result it has reomved the current elements   satisfying the property
//x => !this.contains(x)
  // i.e we have reversed the property by using s.unary_!

  println(negetive.apply(2))
  println(negetive.apply(5))
  val negetiveEven: MySet[Int] = negetive.filter(_ % 2 == 0)
  println(negetiveEven(5))
    val negetiveEven5= negetiveEven + 5 // it will become like this all the vene numbers except [1,2,34]

}
