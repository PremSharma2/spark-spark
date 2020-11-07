package varianceAndTypeClasses

object ImpactOfVarianceOnTypeClass  extends App {

  // import the type Class we are interested and import the
  // type class instances and extension methods
   import cats.instances.int._
   import cats.instances.option._
   import cats.syntax.eq._  // this brings the Type Enrichment API or pimp the librarry API

  val optionComparison= Option(2) === Option(3)
  println(optionComparison)
// Here Compiler does not found the implicit type class instance of Eq[Some[Int]]
  //Even though some is sub type of Option
  // in other words this is happening due to variance problem
  // we only have implicit Type class instance Eq[Option]
  //val InvalidComparison = Some(2) === None
  // variance
  class Animal
  class Cat extends  Animal
  // covariant type: -> subtyping is propagated to the Generic type
  class Cage[+T]
  val cage: Cage[Animal] = new Cage[Cat] // this  is covariant

  // contravarient type: subtyping is propagated backwards to the generic type
  class Vet[-T]
  val vet: Vet[Cat] = new Vet[Animal]
  // RULE of thumb : -> "Has a T = covarient , "Acts on T = contravarient
  // best example of contravarient is Type classes
  // variance affect how type class insatnces are being fetched
// For Example
  // This is Contravarient Type Class
  trait SoundMaker[-T]
  // now lets create Type class instance here
  implicit object AnimalSpundMaker extends SoundMaker[Animal]
  // now lets create the API method called makeSound
  def makeSound[T](implicit soundmaker:SoundMaker[T]) = println("make-Sound")

  // now if i cmake call to this API makeSound and we have implicit object Available
  // SoundMaker[Animal] this will because we have implicit object
  // but when we make a call to API with Cat this will also compile
  // Because Type class instance for Animal
  // is also applicable to Cat
  makeSound[Animal]
  // Here it is compiling because compiler is searching for
  //IMPLICIT VALUE SoundMaker[Cat] and it found the SoundMaker[Animal]
  // for Compiler its same situation like this  val vet: Vet[Cat] = new Vet[Animal]
  // i.e looking for  Vet[Cat] and found new Vet[Animal] but it can be passed
  // because this Type class is Contravarient  so it will compile
  //
  makeSound[Cat]

  implicit object OptionSoundMaker extends SoundMaker[Option[Int]]
  makeSound[Option[Int]]
  // Here by the help of contravarient we solved the variance problem
  makeSound[Some[Int]]
  // Lets make a Covarient type class
  trait animalShow[+T]{
    def show :String
  }
  def organizeShowEvent[T](implicit event: animalShow[T]) = event.show


  implicit object Generalshow extends animalShow[Animal]{
    override def show: String = "COVARIENT IMPLCI OTYPE CLASS OBJECT"
  }

  implicit object Catshow extends animalShow[Cat]{
    override def show: String = "COVARIENT IMPLCI OTYPE CLASS OBJECT"
  }
organizeShowEvent[Cat] // this is ok As we have Cat type implicit object
  //organizeShowEvent[Animal]// here compiler is confsed because it went for to search he
  // implicit object [Animal] type animal it found it but
  // here type cat is also available that too is type of Animal
  //Rule 2: Covarient Type class will always use more specific type class instance for that type
  //but my also confuse the compiler if generel TC  instance is also present
// Rule 3: You cant have both benefits of Contravrient and Covarient
  // so catz library make it invarient because there is no use of varience
  // when compiler is getting confused
  // to fix the above Option Problem
  // because here you want to Compare Empty Option with Non Empty Option then
  // this is the best way
  Option(2) === Option.empty[Int]
}
