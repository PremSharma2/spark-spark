package catz

// In built type classes
object CatsIntro extends App {

/// this is Eq type class here working behind the scene
  val aComparison= 2 == "aString"
//lets import the type class
  // part 1 import the type class  Eq these type classes are scala in built
  //import cats.Eq  : -. Type class
  /*
  Eq type class looks like this
  trait Eq[A] extends Any with Serializable { self =>
  def eqv(x: A, y: A): Boolean
   */
  //----------------------------------------------------------------------

  // part2 import implicit  type class instances
  // for a particular type i.e Int here
//--------------------------------------------------------------------------

// part 3 use the type class API
  /*
  Here Eq comanion object looks like this

  it takes type class instance as implicit value Like we did in
  Json Serilization way
  and this apply returns the type class instance
   */
  /*
  Companion object looks like this
  This is ours and below one is scala one
  object Equal{
    // this takes an type class instance
    // we ca read like this use this equalizer on these values of Type T
    def apply[T](a:T,b:T)(implicit equalizer:Equal[T]) = equalizer
  }
  // This is Type Class instance
  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name==b.name
   */
  /*
  object Eq extends EqFunctions[Eq] with EqToEquivConversion {
final def apply[A](implicit ev: Eq[A]): Eq[A] = ev
   */

// this will automatically inject implicit type class instance
  // of type int into the Eq companion because we have given the type is Int
  import cats.Eq
  import cats.instances.int._
  // Here we are calling Eq type class companion object which in turn returns the
  // Type class instance
  val intEqualityTypeClassInstance: Eq[Int] = Eq.apply[Int]
  /*
  now we will call eqv method of type class Eq via type class instance
  def eqv[@sp A](x: A, y: A)(implicit ev: E[A]): Boolean =
    ev.eqv(x, y)

   */

   intEqualityTypeClassInstance.eqv(2,2)
  /*
  Like this : ->

  trait Equal[T]{
    def apply(a:T,b:T):Boolean
  }
  //Companion object for equal
  object Equal{
    // this takes an type class instance
    // we ca read like this use this equalizer on these values of Type T
    def apply[T](a:T,b:T)(implicit equalizer:Equal[T]) :Boolean= equalizer.apply(a,b)
  }
  // This is Type Class instance
  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name==b.name
   */


  //--------------------------------------------------------------
  // part 4 use extension methods or type enrichment by importing it
  /*
  Like this : ->
  type Enrichment or extension method
  implicit class TypeSafeEqual[T](value:T){
    def ===(other:T)(implicit equalizer:Equal[T]) = equalizer.apply(value,other)
    def =!=(other:T)(implicit equalizer:Equal[T]) = !equalizer.apply(value,other)
  }
   */
  import cats.syntax.eq._
  val a: Boolean = 2===3

  // part 5 Extending type class operation to composite types
  // for that we need to import the correct implicit  type  class instance
  import cats.instances.list._ // by this we bring Eq[List[Int]]
  val aListcomparison: Boolean = List(2) === List(3)

  // part 6 create a type class instance for custom type
  case class ToyCar(model :String, price:Double)
  // create your own type class using cats API
  // now what scala will  do here it will create a type class instance
  //i.e Eq[A] instance but it is not singleton object it is anonymous class object
  // using this function f: (A, A) => Boolean
  //i mean its an way of creating type class instance for custom type
  // with this method scala will give you type class instance
  //and we marked it implicit so that this type class instance cam be marked implicit
  /*
  def instance[A](f: (A, A) => Boolean): Eq[A] =
    new Eq[A] {
      def eqv(x: A, y: A) = f(x, y)
    }
   */
  implicit val toyCar: Eq[ToyCar] = Eq.instance[ToyCar]{
    (car1,car2) => car1.price == car2.price
  }

  val compareToyCars= ToyCar("ferari", 29.99) === ToyCar("Baleno" , 39.99)
  println(compareToyCars)
}
