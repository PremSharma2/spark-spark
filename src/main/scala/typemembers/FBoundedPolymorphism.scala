package typemembers

object FBoundedPolymorphism extends App {

  /*
TODO
  trait Animal{
    def breed:List[Animal]
  }


TODO
  class Cat extends Animal{
    override def breed: List[Animal] = ???
    // problem here is that we dont want List[animal] to be returned from here we want List[Cat] tobe returned
  }

TODO
  class Dog extends Animal{
    override def breed: List[Animal] = ???
    // problem here is that we dont want List[animal]
    to be returned from here we want List[Dog] to be returned
  }

 */


  // Lets Solve this problem by our due diligence
  // i.e lets return List[Cats] and List[Dog] explicitly
  // we can do that as we are overriding the breed
  // But this will not solve the problem because still by mistake someone can return List[Cat] in dog class
  // the only thing will solve this is Enforcing the Type Correctness at compile time
  // this is solution number one
  /*
TODO
  trait Animal{
    def breed:List[Animal]
  }
TODO
  class Cat extends Animal{
    override def breed: List[Cat] = ???
  }

TODO
  class Dog extends Animal{
    override def breed: List[Cat] = ???
  }

   */



  // Solution number 2
  /*
TODO
  // Here we have used in type parameter as Animal type i.e recursive type
  // This means that the type you will pass here it must Extend Animal i.e self Type and animal is typed to A
  // i.e Animal also takes type parameter A<: Animal[A]
  // for example if we pass Cat then Cat must extend  the Animal of Type Cat Cat< : Animal[Cat]

 */
  /*

TODO
  This kind of self-referential type constraint is known as F-bounded polymorphism
  or this is called recursive Type and A <: Animal[A] is called F bounded Polymorphism

TODO
   F-bounded polymorphism is used to specify that
   a type parameter will obey a certain contract
   not just now but also in all future subclassing.
   In your example, the F-bounded type A <: Animal[A]
   is saying that "whatever A is, it must be a subtype of Animal[A]


TODO
 Recursive Type
    Animal[A <: Animal[A]] is also an example of a recursive type
    because A is defined in terms of itself,
    which means you can create chains of more and more specialized Animals,
    while still being able to write generic code that operates on any Animal[A].


TODO
  trait Animal[A <: Animal[A]]{
    def breed:List[Animal[A]]
  }
TODO
  class Cat extends Animal[Cat]{
    // this type recursive forces the type restriction now it will return only List[Cat]
    override def breed: List[Animal[Cat]] = List(new Cat, new Cat)
  }

TODO
  class Dog extends Animal[Dog]{
    override def breed: List[Animal[Dog]] = List(new Dog, new Dog)
  }


TODO
  Advantages of Recursive Types in this Context
  Type Safety: By using this recursive type,
  you are enforcing that the breed method returns
  not just any Animal,
  but an animal of the same subtype.
  It prevents you from doing something like this:
TODO
  class Mammal extends Animal[Mammal] {
  def breed: List[Animal[Mammal]] = List(new Bird)  // This would be a compile-time error
}

TODO
 Generic Code:
  Since the trait Animal still uses a type parameter A,
  you can write generic code that works on any Animal[A].

TODO
  def displayBreeding[A <: Animal[A]](animal: Animal[A]): Unit = {
  val offspring = animal.breed
  println(s"Produced ${offspring.length} offspring")
}



  // practical Example of this approach
  // most of the ORM framework uses this Approach to enforce the Correct type Restriction
TODO
  trait Entity[E  <: Entity[E]]
  trait Entity[E <: Entity[E]] {
  def save(): E
  def delete(): E
  // ... other CRUD operations
}
TODO
 class User extends Entity[User] {
  def save(): User = {
    // save logic
    this
  }
  def delete(): User = {
    // delete logic
    this
  }
}

   */
  /*

 TODO
  // This introduces Solution number 3 Here we will use FBP + Self Types
  //Idea here is that Type How do i will enforce compiler the class i am defining and the
  // type i am annotating i.e A <: Animal[A]  are same
  // In this we will use  recursive Type and A <: Animal[A] is called F bounded Polymorphism Plus SelfType
  // It means that if A<: Animal  it Requires A also that Means in short it should be A i.e
  // Both A should be equal or  Whatever Decedents of Animal[A] we will implement that must also be an A
   The self-type self: A =>
   enforces that whoever extends the Animal[A] trait
   must also be a subtype of A.
   This creates an additional restriction on top
   of the F-bounded polymorphism (A <: Animal[A]),
   ensuring that not just any Animal[A] can be used,
   but that A itself has to be the
   subtype that is implementing Animal[A].
   */
  /*
  trait Animal[A <: Animal[A]]{ self: A=>
    def breed:List[Animal[A]]
  }
/*

TODO
  class Cat extends Animal[Cat]{
    // this type recursive forces the type restriction now it will return only List[Cat]
    override def breed: List[Animal[Cat]] = ???
  }

TODO
  class Dog extends Animal[Dog]{
    override def breed: List[Animal[Dog]] = ???
  }

 */
TODO
  class Crocodile extends Animal[Crocodile]{
    override def breed: List[Animal[Crocodile]] = ???
  }
TODO
  // But still problem is not solved Lets explain this with example
  //Lets say we have a new hierarchy based on above hierarchy
  // Hence Once we down the class Hierarchy one level then
  // Fbounded polymorphism fails
  trait Fish extends Animal[Fish]
    class Shark extends Fish{
      // again same problem it should Return List[Animal[Shark]]
      override def breed: List[Animal[Fish]] = List(new Cod)
    }
TODO
  class Cod extends Fish{
    // again same problem it should Return List[Animal[Shark]]
    override def breed: List[Animal[Fish]] = ???
  }
TODO
 */
  //Solution is with Type Classes
  // class hierarchy of Animal which type-class will use to perform operation
  /*
  trait Animal
  class Dog extends Animal
  // type class will be used to enforce  type restriction
  // by the type classes ,So Method Signature will be forced by the type Parameter here
  trait CanBreed[A]{
    def breed(a:A) : List[A]
  }
// Design will be like this we did before
  /*

TODO
  object HtmlSerializer{
    // it take type class instance as argument and the type T
    def serialize[T](value : T)(implicit serializer:HtmlSerializer[T]):String=
      serializer.serialize(value)
    // for more better design
    def apply [T](implicit serializer: HtmlSerializer[T]) = serializer
  }
   */

TODO
 //Animal instance
  object Dog {
  // type class instance
  implicit object DogCanBreed extends CanBreed[Dog] {
    override def breed(a: Dog): List[Dog] = List()
  }
  }
   */
  /*
  implicit class CanBreedOps[A](animal:A){
    def breed (implicit canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
  }
  val dog= new Dog
  val x: Seq[Dog] =dog.breed
  /*
  new CanBreedOps[Dog](dog).breed(Dog.DogCanBreed )
  implicit value to be passed to breed is Dog.DogCanBreed
   */
  //Now lets Test this with Some bad code
  class Cat extends Animal
  object Cat {
    // type class instance
    implicit object CatsCanBreed extends CanBreed[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }
  val cat= new Cat
  //cat.breed here compiler is not allowing
  // Lets Modify this Code


TODO   */
  // Lets Modify this Code
  // Lets make animal itself type class
  trait Animal[A] { // This is pure Type Class
    def breed(animal: A): List[A]
  }
// This is the type on which Type Class applies the operation i.e A = Dog
    class Dog
    object Dog {
      // type class instance
      implicit object DogAnimal extends Animal[Dog] {
        override def breed(a: Dog): List[Dog] = List()
      }
    }
 //last component to complete type class pattern
  implicit class AnimalOps[A](animal:A){
  def breed (implicit animalTypeClassInstance: Animal[A]): List[A] =
    animalTypeClassInstance.breed(animal)
}


  val dog= new Dog
  val x: Seq[Dog] =dog.breed



}