package typemembers

object TypeMember extends App {
  trait Animal
  class Dog extends Animal
  class Crocodile extends Animal
  class Cat extends Animal

  class AnimalCollection{
    // this abstract type member declaration is alternative to use of Generics
    type Animaltype // abstract type member
    type BundedAnimal<:Animal
    type SuperBundedAnimal >:Dog <:Animal
    // this is type aliasing
    type catType = Cat
  }
val ac = new AnimalCollection
 val dog : ac.SuperBundedAnimal = new Dog
  // this is not useful as such but type aliasing very much useful
  val cat : ac.catType = new Cat
  type catTypeAlias = Cat
  val pussycat : catTypeAlias = new Cat
  val mycat :Cat= new Cat
// alternatives to generics when
// we design APIS we can use type aliases and abstract type members
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
// Exercise is some other team have developed this api and u are using it
  // but u considered this is bad design

  trait MList{
    //type A<: String // we can use this like as well as typeconstraint 26262
    type A
    def head: A
    def tail : MList
  }
  class CustomList(hd: String, tl:CustomList) extends MList {
    override type A = String

    override def head: String = hd

    override def tail: CustomList = tl
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
//Lets fix this design by using bounded type
  trait ApllicableToNumbers{
  // abstract type bounded member
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
  // no you wanted to compile this guy only not the above one because this API is designed for
  //Integers not for Strings
  // So we want to enforce this limitation to compile time also so that code should not compile
  class IntSeq(hd: Int, tl:IntSeq) extends Seq with ApllicableToNumbers{
    override type A = Int

    override def head: Int = hd

    override def tail: IntSeq = tl
  }
}
