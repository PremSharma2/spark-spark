package typemembers

object TypeMember extends App {
  trait Animal
  class Dog extends Animal
  class Crocodile extends Animal
  class Cat extends Animal

  class AnimalCollection{
    // TODO this abstract type member declaration is alternative to use of Generics
    // TODO but at the same time it is also the member of class
    type Animaltype // abstract type member or we can say T instead of Aniamltype
    type BundedAnimal<:Animal   // T<: Animal
    type T >:Dog <:Animal
    // Todo this is type aliasing
    type catType = Cat
  }
val ac = new AnimalCollection
  //TODO accessing the abstract type member class and assigning the type to it
 val dog : ac.T = new Dog
  // this is not useful as such but type aliasing very much useful
  val cat : ac.catType = new Cat
  type catTypeAlias = Cat
  // TODO : -> use of type aliasing
  val pussycat : catTypeAlias = new Cat
  val mycat :Cat= new Cat
// alternatives to generics when
// we design APIS we can use type a
// liases and abstract type members
  trait Mylist{
    type T
    def add(element :T):Mylist
  }
  class NonEmptyList(value:Int) extends Mylist{
    override type T = Int

    override def add(element: Int): Mylist = new NonEmptyList(2)
  }
  //.type this is type alias to already define type like cat refrence variable is of whateva tyep
  type CatsType = cat.type
  //val blackCat: CatsType = cat

//TODO Exercise is some other team have developed this api and u are using it
  // TODO but u considered this is bad design

  trait MList{
    //type A<: String // we can use this like as well as typeconstraint 26262
    type A
    def head: A
    def tail : MList
  }
  class StringList(hd: String, tl:StringList) extends MList {
    override type A = String

    override def head: String = hd

    override def tail: StringList = tl
  }
// no you wanted to compile this guy only not the above
// one because this API is designed for
  //Integers not for Strings
  // So we want to enforce this limitation to compile time also so that code should not compile
  class IntList(hd: Int, tl:IntList) extends MList {
    override type A = Int

    override def head: Int = hd

    override def tail: IntList = tl
  }
//TODO : -> Lets fix this design by using bounded type
  trait ApplicableToNumbers{
  //TODO : ->  abstract type bounded member
    type A<: Int
  }

  trait Seq{
    type A
    def head: A
    def tail : Seq
  }
  /*class CustomSeq(hd: String, tl:CustomSeq) extends Seq with ApllicableToNumbers {
    // it is not computed by the compiler basically
    override type A = String

    override def head: String = hd

    override def tail: CustomSeq = tl
  }*/
  /*
 TODO
   no you wanted to compile this guy only not the above one because this API is designed for
  Integers not for Strings
   So we want to enforce this limitation to compile time also so that code should not compile

   */
  class IntSeq(hd: Int, tl:IntSeq) extends Seq with ApplicableToNumbers{
    override type A = Int

    override def head: Int = hd

    override def tail: IntSeq = tl
  }
}
