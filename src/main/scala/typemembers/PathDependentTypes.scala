package typemembers

object PathDependentTypes  extends App {

  class Outer{
    class Inner
    object  InnerObject
    type abstractMemberType
    // every inner member has same parent called Outer#Inner
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod:Int ={
    // we can define classes inside the method
    class HelperClass
    // we can't define type members but only type aliases
    type Helper = String
    2
  }
  //we can access the abstract type member of Outer or we say it path dependent abstract Type
  val abstrattypeMember : Outer#abstractMemberType = ???
  val outer= new Outer
  val oo= new Outer
  val inner= new outer.Inner
  val ooinner= new oo.Inner
  outer.printGeneral(inner)
  oo.printGeneral(inner)

  /*
  Lets designed a Datbase which is keyed by Int or string
  Lets designed it for other keys as well
   */
/*
  trait Item[Key]
  class IntItem extends Item[Int]
  class StringItem extends Item[String]
*/
  // lets define a method which fetch the value
  // from the database
  //def get [ItemType](key: Key) : ItemType

  //get[IntItem] (42) // ok
  //get[StringItem] ("Scala")// should be ok


  // for the solution of above problem
  // we can abstract types or type aliases
  // as we want some type constraint of compile time
  trait ItemLike{
    type key
  }
  trait Item[K] extends ItemLike{
    override type key = K
  }
  class IntItem extends Item[Int]{
    override type key = Int
  }
  class StringItem extends Item[String]
  // compiler will interpret as ItemType<: ItemLike the type like this
  /*
  trait ApllicableToNumbers{
    type A<: Number
  }
  i.e whateva the type is declared in ItemLike it will treat as bounded type
            ItemType<:key
   */
  // here idea is to add type constraint or upper bound with this guy
  // itemLike, in short it is parametrized with abstract type member Key
  def get [ItemType<: ItemLike] (key: ItemType#key) : ItemType = ???
 def getKey[T] (key : T) :T = ???
  get[IntItem] (42) // ok
  get[StringItem] ("Scala")// should be ok
  //get[Int] ("prem")
  //getKey[IntItem]("")

}
