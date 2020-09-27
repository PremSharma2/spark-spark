package oops

import scala.xml.Null

object NullVsNothingVsNone  extends App{

  // The Null reference Type is of Null
  // it accepts only null values in lower case null means
  // any null reference in scala or java is represented by Null class
  // Null is replacement for all refrence types
  val ref :Null= null;
//when there is no string or no object is constructed we can assign the refrence of Null class
  //Also Null class is also sub type of the of all the classes
  //AnyRef-> all refrence Types -> Null
  // Null class has special treatment from compiler
  // it will automatically make that Null subclass of all classes
  val noString:String= ref
  val noPerson : Person= ref


  //----------------------------------------------------------------------------------------------
  //3 Nil it represents Empty Collection i.e when head and tail is null then we can represent that
  // null refrence to Nil
  // Nil is an singleton object that can be attributed to any EmptyList
  // Like we created a EmptyList, But Nil unlike Null has proper values
  // i.e proper attributes
  val anEmptyList: List[Int] = Nil
    println(Nil.length)

  //---------------------------------------------------------------------------------------------
  // 4 None which is subtype of Options


  val anAbsentInt:Option[Int] = None
  println(anAbsentInt.isEmpty)

  // 5 Unit represents Void retirning functions
  val theUnitValue: Unit= ()

  // 6 Nothing means no value at all
  // you cant extend this type neither u instantiate this class
  // its just used to represent when exception is thrown from function
  // that it returns nothing
  //val nothingInt: Int= throw  new RuntimeException("No Int")
  // now as we know in scala every thing is expression an every expression returns some value
  // so this error throwing expression will return Nothing

  val nothingInt1: Nothing= throw  new RuntimeException("No Int")

}
