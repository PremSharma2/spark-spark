package ImplicitsAndTypeClasses

object PimpMyLibraryExercise extends App {
  //Exercise 1
// implicit && value class  this represent the implicit conversion
  implicit class RichString(value : String){
  def asInt:Int = Integer.valueOf(this.value) // here this expression returns java.lang.Int which scala convert
  //to Int
 def encrypt(cypherDistance:Int):String = this.value.map(char => (char + cypherDistance).asInstanceOf[Char] )
  }

// these all are implicit conversions
  println("3".asInt + 4)
  println("Prem".encrypt(2))

  // Exercise 2

  implicit class RichInt(val value :Int) extends AnyVal {
    def times(function: () => Unit): Unit ={
      def timesAux(n:Int):Unit=
        if (n<=0) ()
        else {
          function.apply()
          timesAux(n-1)
        }
      timesAux(4)
    }
    def *[T](list:List[T])= {
      def concatenate(n:Int):List[T]= {
        if(n<=0) List()
        else concatenate(n-1) ++ list
      }
      concatenate(value)
    }


  }
  3.times(() => println("Scala Rocks"))
  println(4* List(1,2))
  implicit def stringToInt(str:String)=Integer.valueOf(str)
  // Here compiler will search for all implicit value classes which takes value as string and look for
  // method / defined for this , if not found look for plane value classes and inside the class look for
  // methods which does implicit conversion of string to int or search locally
  // and Int has / this method defined
  //
  //println("6" / 2)
// implict calsses norammly are plane classes with method defined inside the class for implicit conversion
  // This implementation is an alternate to above implementation of implicit value classes
  class RichAltInt(value:Int){
    implicit def implicitconversion(value:Int):RichAltInt= new RichAltInt(value)
  }
// Doing implicit conversions or pimping is not encouraged via method in scala i.e method implicit conversion
  // is not encouraged lets proof it why?
  implicit def intToBoolean(i:Int):Boolean= i==1
  val conditionedValue1= if (3) "OK" else "Something Wrong"
val conditionedValue= if (3) "OK" else "Something Wrong"
  println(conditionedValue)
}
