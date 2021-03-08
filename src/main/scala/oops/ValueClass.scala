package oops

import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops.toCoercibleIdOps

object ValueClass {

//TODO online store
  case class Product(code:String,description:String)
  trait BackendService{
    def findByCode(code:String):Option[Product]
    def findByDescription(description:String):List[Product]
  }
  val acode= "12345-65-456"
  val aDescription="foam mattress"
  val backend= new BackendService {
    override def findByCode(code: String): Option[Product] = ???

    override def findByDescription(description: String): List[Product] = ???
  }

  /*
TODO
 this is a problem we need to solve it basically
 Soln1: we can use case class instead of these string values
  val acode= "12345-65-456"
  val aDescription="foam mattress"
  these should be represented by case classes

   */
  case class BarCode(code:String)
  case class Description(description:String)
  //TODO now lets redefine the backend service
  trait BackendService1{
    def findByCode(code:BarCode):Option[Product]
    def findByDescription(description:Description):List[Product]
  }
  val backendv1= new BackendService1 {
    override def findByCode(code: BarCode): Option[Product] = ???

    override def findByDescription(description: Description): List[Product] = ???
  }
  /*
  TODO
      soln2: use BarCode singleton object and make def apply over there
         which is called factory of Barcode
   */
  object Barcode{
    def apply(code:String):Either[String,BarCode] = Either.cond(
      code.matches("\\d-\\d{5}-\\d{5}"),
      BarCode(code),
      "code is bad"
    )
  }
  /*
 TODO soln: 3 value classes
        value classes are normal classes but these classes take
        only val constructor arguments and they extends AnyVal
        But in case cass fields are by default vals so need to
        do explicitly
        Advantages of extending AnyVals
         no run time overhead : JVM seeks value class instances as instances of String
         not the BarCodeVc instance
      Restrictions od Value classes:
      1 - only one val constructor argument
      2 - no other vals inside , just methods
      3 cannot be extended
      4 can only extends universal traits(trait with just defs and without initilization)


TODO
    But this run time advantage also fails here basically
    for the following example
    here in this api show the value class advantgae of runtine is not come into the picture
    because JVM is confused whteher T is to be cosidered as String or object
    as we are using generics which will be erased at run time
   */
  def show[T](arg:T) = arg.toString
/*
TODO
  second scenario where this advantage fails
  here also compiler is expecting of type Barcode so jvm will instantiate the class
 BarCodeVc because Array is generic type
 */
  val barcodes= Array[BarCodeVc](BarCodeVc("123-456-789"),BarCodeVc("123-456-866"))
  /*
  TODO Third case is pattern match where we will not able to take advantage
      this will instantiate the element being matched
   */
  BarCodeVc("123-456-789") match {
    case BarCodeVc(code) => println(code)
  }
  /*
  TODO final solution is here use of Nwe type api
       which will generate the value classes automatically
       for us which covers all these above discussed scenario
        @newtype class BarCodeNT(code:String)
   */
  case class BarCodeVc(val code:String) extends AnyVal{
   def countryCode= code.charAt(0)
  }
  @newtype class BarCodeNT(code:String)
  // now i will create a helper api here i.e utility object
  object BarCodeNT{
    def apply(code:String):Either[String,BarCodeNT] = {
      Either.cond(
        code.matches("\\d-\\d{5}-\\d{5}"),
        code.coerce,
        "code is bad"
      )
    }
  }
  def main(args: Array[String]): Unit = {
    backend.findByCode(acode)
    backend.findByDescription(acode)// this compiles but this is a bug
    // now if i will do same mistake here that will not compile
  //  backendv1.findByCode(code)
    backendv1.findByCode(BarCode(code = acode))
    // but still there are chances for bug generation this is not bug free code
    backendv1.findByCode(BarCode(code = aDescription))
    // this is also bad
    //TODO hence use of Either monad makes the code safe
  // backendv1.findByCode(Barcode(acode))
  }
}
