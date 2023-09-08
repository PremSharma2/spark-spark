package typemembers

object PathDependentTypes  extends App {

  class Outer{
    class Inner
    //todo :
    // Inner objects are useful for
    // encapsulating functionality
    // that doesn't depend on the state of the outer class.
    object  InnerObject
    type abstractMemberType
    // every inner member has same parent called Outer#Inner
    def printGeneral(i: Outer#Inner) = println(i)
  }


  def aMethod:Int ={
    // we can define classes inside the method
    class HelperClass
    // TODO we can't define type members but only type aliases in methods
    type Helper = String
    2
  }


/*
TODO
     The # notation means you're referring to a type
     that's defined inside of another type,
     but you're not tying it to any particular instance of the outer type.
     This is in contrast to path-dependent types,
     which would tie abstractMemberType to a specific instance of Outer.
     The type notation (#) that refers to any abstractMemberType from any instance of Outer.

 */
  //TODO we can access the abstract type member of Outer or we say it path dependent abstract Type
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
  class StringItem extends Item[String]{
    override type key = String
  }


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

  /*
TODO
   You are passing in a key of a type
   that is compatible with the ItemType you are working with.
   Whatever is returned from this function is of the type ItemType.
   This is particularly powerful for creating type-safe APIs.
   For example, if ItemType is IntItem,
   then ItemType#key would be Int,
   ensuring that you can't accidentally pass
   in a String or any other type as the key.
   When to Use This Approach:
   Type Safety: When you want to ensure strict type checking
   between keys and items,
   this pattern ensures that you cannot mix them up.

TODO
  Extensibility: If new item types are added in the future,
  this method will still work without changes,
  because it is defined generically
  for all subtypes of ItemLike.

TODO
  Reusability:
  This single method can be used
  for different types of items and their keys,
  reducing code duplication.
   */
  def get [ItemType<: ItemLike] (key: ItemType#key) : ItemType = ???
 def getKey[T] (key : T) :T = ???
  get[IntItem] (42) // ok
  get[StringItem] ("Scala")// should be ok
  //get[Int] ("prem")
  //getKey[IntItem]("")



}
