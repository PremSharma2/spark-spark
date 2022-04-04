package functional.programming

object HofsForOop {

  class Applicable{
    def apply(x:Int) = x+1
  }
  val applicable = new Applicable
  applicable.apply(1 ) // it will return 2
  applicable(2) // in FP it should be similar to calling any function
  // should look like calling any function when you call any def on any instance
  // like we use to do in c applicable(2)
  // or call calling any property or def should be similar like applicable.length
  /*
    TODO
         Lets discuss about the function objects in scala
         here incrementer is the jvm object here
         of type Function1[Int,Int] trait
   */
  val incrementer: (Int => Int) = new Function[Int,Int] {
    override def apply(x: Int): Int = x+1
  }

  def incrementer1: (Int => Int) = new Function[Int,Int] {
    override def apply(x: Int): Int = x+1
  }
  incrementer.apply(2)
  incrementer(2)
  incrementer1(3)
  //TODO  now we can use lambda instead of Anonymous type
  val incrementeralt: Int => Int = (x:Int) => x+1
  incrementeralt(2)
  def nTimeS(f:Int => Int , n:Int) :Int=> Int = ???
}
