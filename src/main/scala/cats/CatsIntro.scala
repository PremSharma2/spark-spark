package cats
// In built type classes 
object CatsIntro extends App {

/// this is Eq type class here working behind the scene
  val aComparison= 2 == "aString"
//lets import the type class
  // part 1 import the type class  Eq these type classes are scala in built
  //----------------------------------------------------------------------

  // part2 import type class instances for a particular type i.e Int here
    import cats.instances.int._
   val typeclassinstance: instances.int.type = cats.instances.int

//--------------------------------------------------------------------------

// part 3 use the type class API
  /*
  Here Eq comanion object looks like this

  it takes type class instance as implicit value Like we did in
  Json Serilization way
  and this apply returns the type class instance
   */
  /*
  object Eq extends EqFunctions[Eq] with EqToEquivConversion {

  /**
   * Access an implicit `Eq[A]`.
   */
  @inline final def apply[A](implicit ev: Eq[A]): Eq[A] = ev
   */


  val intEqualityTypeClassInstance: Eq[Int] = Eq[Int]
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
}
