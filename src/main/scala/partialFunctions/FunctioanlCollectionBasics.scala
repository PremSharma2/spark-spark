package partialFunctions

object FunctioanlCollectionBasics  extends App{


  val aSet = Set(1,2,3,4,5)
/*
TODO
    The critical API of a set consists of
  the ability to tell whether an item is in the set or not
  the ability to add an element to a set (and if it exists, don’t add it again)
  the ability to remove an element from the set
  (and if it doesn’t exist, don’t remove it again, of course)
 */
  val aNewSet= aSet + 6
  val smallSet= aSet - 6

  aSet.contains(2)  // returns boolean
/*
TODO
   Notice that the apply method makes the set “callable” like a function.
   At the same time, that invocation always returns a value (true or false), for any argument you pass to it.
  So notice that a set behaves like a function A => Boolean,
  because you can pass any argument of type A,
  and you’ll get a Boolean (whether or not the argument is in the set).
  Here’s an outrageous idea: sets ARE functions!
 */
  aSet(2)// it also return boolean hence Set is a Function


  /*
 TODO
     trait Set[A] extends Iterable[A]
    with collection.Set[A]
    with SetOps[A, Set, Set[A]]
    with IterableFactoryDefaults[A, Set] {
  ...
}

// in the scala.collection.immutable package

trait SetOps[A, +CC[X], +C <: SetOps[A, CC, C]]
  extends collection.SetOps[A, CC, C] {
  ...
}

// in the general scala.collection package

trait SetOps[A, +CC[_], +C <: SetOps[A, CC, C]]
  extends IterableOps[A, CC, C]
    with (A => Boolean) { // <-- jackpot!
  ...
}
   */


 // Here’s a small experiment —
  // let’s write a small implementation of a set that implements the functional interface A => Boolean:
  /*
  TODO
      The main trait implements the crucial Set API:
   testing if an element is in the set
   adding an element
   removing an element
   */

  sealed trait RSet[A] {
    def contains(x: A): Boolean
    def +(x: A): RSet[A]
    def -(x: A): RSet[A]
    def ++(other: RSet[A]): RSet[A]
  }

  /*
  TODO
      Let’s then continue with an implementation of an empty set,
      correctly typed. The standard library uses an case object typed with Set[Any]
      and then type-checked via casting, but let’s use a small case class for our experiment:
      The implementation of 2 out of 3 methods is easy:
   the set doesn’t contain anything, so contains(x) == false for all x in A
   the set can’t remove anything, so return the same set
   Let’s now consider a set given by a property, i.e. similarly to how we were taught in math classes.
   For example, the set of all even natural numbers is something like { x in N | x % 2 == 0 }.
   Pure sets in mathematics are described by their properties.
   Some sets may be finite, or infinite, some may be countable (or not).
   */
  case class Empty[A]() extends RSet[A] {
    override def contains(x: A) = false
    // todo A single-element set is a property-based set, where the property only returns true for that particular element.
    def +(x: A): RSet[A] = new PropertySet[A](_ == x)
    def -(x: A): RSet[A] = this
    override def ++(other: RSet[A]): RSet[A] = other
  }

/*
TODO
    Let’s look at the main API methods:
  this set is all about the property of the elements, so contains returns true only if that property is satisfied
  adding an element means adjusting the property so that it also holds true for the element we want to add
  removing an element means adjusting the property so that it definitely returns false for the element we’re removing
  e  => property(e) || e == x effectively "extends" the set to include x
   without affecting the original elements in the set that satisfy property(e).
   This means that the new set contains all elements from the old set (as they satisfy property(e)) and also the element x
 */

  case class PropertySet[A](property: A => Boolean) extends RSet[A] {
    override def contains(x: A): Boolean = property(x)
    override def +(x: A): RSet[A] = PropertySet(e => property(e) || e == x)
    override def -(x: A): RSet[A] =
      if (contains(x)) PropertySet(e => property(e) && e != x)
      else this
    override def ++(other: RSet[A]): RSet[A] =
      PropertySet(e => this.contains(e) || other.contains(e))
  }

  object RSet {
    // Public constructor for an empty RSet of any type
    def empty[A]: RSet[A] = Empty()

    def apply[A](values: A*): RSet[A] =
      values.foldLeft(empty[A])(_ + _)

    def fromProperty[A](property: A => Boolean): RSet[A] =
      PropertySet(property)
  }

  //TODO use case of PropertyBasedSet
  val emptySet: RSet[Int] = RSet.empty
  val withOne = emptySet + 42   // ✅ Returns RSet[Int]
  println(withOne.contains(42)) // true

  val set = RSet(1, 2, 3)       // folded over empty
  /**
 TODO
  Use Case 2: Query Optimization
  Imagine you have a database and you know certain rows
  are more likely to be queried based on some property.
  A PBSet can be used to easily check whether a row satisfies the likely-to-be-queried property.
  without loading the data
   */

  val frequentlyQueried = PropertySet[String](x => x.startsWith("A"))

  def isFrequentlyQueried(s: String): Boolean = frequentlyQueried.contains(s)

  println(isFrequentlyQueried("Apple"))  // Output: true
  println(isFrequentlyQueried("Banana")) // Output: false

  /**
 TODO
  Use Case 3: Authorization
  Let's say you have different tiers of users in an application
  and each tier has a set of privileges.
  You could define each tier's privileges as a PBSet.
   */

  case class Product(
                      id: String,
                      name: String,
                      category: String,
                      brand: String,
                      price:Int
                    )

  val cheapElectronics = RSet.fromProperty[Product] { p =>
    p.price < 50 && p.category == "Electronics"
  }

  val popularBrands = RSet.fromProperty[Product] { p =>
    p.brand == "Apple" || p.brand == "Samsung"
  }

  val combineFilters=cheapElectronics ++ popularBrands
  //So you're creating an exception to the rule — including the iPhone manually, even though it doesn't satisfy the original property.
  val filtered = cheapElectronics + Product("iPhone-1001", "iphone16", "Electronics", "Apple",12000) // Adds one special exception


  val iphone = Product("sku123", "iPhone", "Electronics", "Apple", 999)

  println(cheapElectronics.contains(iphone)) // false
  println(filtered.contains(iphone))         // true

  val first5Elements: RSet[Int] = RSet.empty[Int] + 1 + 2 + 3 + 4 + 5

  val first5lementsFancy = RSet(1,2,3,4,5)
  val first1000Elements = RSet(1 to 1000: _*) // pass as varargs



  //TODO The interesting thing about this set definition is that
  // you can now declare infinite sets, just based on their property.
  // For example, the set of even natural numbers is now trivial:

  val allEvens = PropertySet[Int](_ % 2 == 0)

  /*
  TODO
      For example, think of the main API of a sequence (Seq) in Scala:
       sequences have the sole job of giving you an item at an index.
       If that index is out of bounds, an exception will be thrown.
   */

  val aSeq = Seq(1,2,3)
  aSeq(1) // 2
  aSeq(5) // Out of bounds!
  /*
  TODO
    Question: what functional “thing” is invokable like a function,
     but not on all arguments? Hint: it starts with “P” and rhymes with “artialFunction”.
    Yep, I said it: sequences are partial functions.
    The type of the argument is an Int (the index which you want to access)
    and the return type is A (the type of the Seq). It’s also found in the standard library:
   */
  // scala.collection.immutable
  /*
  trait Seq[+A] extends Iterable[A]
    with collection.Seq[A]
    with SeqOps[A, Seq, Seq[A]]
    with IterableFactoryDefaults[A, Seq] {
    ...
  }

  // scala.collection
  trait Seq[+A]
    extends Iterable[A]
      with PartialFunction[Int, A] // <-- jackpot
      with SeqOps[A, Seq, Seq[A]]
      with IterableFactoryDefaults[A, Seq]
      with Equals {
    ...
  }

   */
}
