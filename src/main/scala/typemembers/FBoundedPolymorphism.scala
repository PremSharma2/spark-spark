package typemembers

import typemembers.FBoundedPolymorphism.Animal

object FBoundedPolymorphism extends App {

  /*
  trait Animal{
    def breed:List[Animal]
  }

  class Cat extends Animal{
    override def breed: List[Animal] = ??? // problem here is that we dont want List[animal] to be returned from here we want List[Cat] tobe returned
  }


  class Dog extends Animal{
    override def breed: List[Animal] = ??? // problem here is that we dont want List[animal] to be returned from here we want List[Dog] tobe returned
  }


 */
  // Lets Solve this problem by our due diligence
  // i.e lets return List[Cats] and List[Dog] explicitly
  // we can do that as we are overriding the breed
  // But this will not solve the problem because still by mistake someone can return List[Cat] in dog class
  // the only thing will solve this is Enforcing the Type Correctness at compile time
  // this is solution number one
  /*
  trait Animal{
    def breed:List[Animal]
  }

  class Cat extends Animal{
    override def breed: List[Cat] = ???
  }


  class Dog extends Animal{
    override def breed: List[Cat] = ???
  }

   */

  // Solution number 2

  // Here we have used in type parameter as Animal type i.e recursive type
  // This means that the type you will pass here it must Extend Animal i.e self Type and animal is typed to A i.e A<: Animal[A]
  // for example if we pass Cat then Cat is extending the Animal of Type Cat Cat< : Animal[Cat]
  /*
  trait Animal[A <: Animal[A]]{ // this is called recursive Type and A <: Animal[A] is called F bounded Polymorphism
    def breed:List[Animal[A]]
  }

  class Cat extends Animal[Cat]{
    // this type recursive forces the type restriction now it will return only List[Cat]
    override def breed: List[Animal[Cat]] = ???
  }


  class Dog extends Animal[Dog]{
    override def breed: List[Animal[Dog]] = ???
  }
  // practical Example of this approach
  // most of the ORM framework uses this Approach to enforce the Correct type Restriction

  trait Entity[E  <: Entity[E]]
  // another example of F bounded Polymorphism
  class Person extends Comparable[Person] { // this is one more Example F bounded Polymorphism
    override def compareTo(o: Person): Int = ???
  }
  // But Still Problem is not solved Completely
  //It is solved partially it is still Compiling but it needs to return Crocodile not Dog

  class Crocodile extends Animal[Dog]{
    override def breed: List[Animal[Dog]] = ???
  }

   */

  // This introduces Solution number 3 Here we will use FBP + Self Types
  //Idea here is that Type How do i will enforce compiler the class i am defining and the
  // type i am annotating i.e A <: Animal[A]  are same
  // In this we will use  recursive Type and A <: Animal[A] is called F bounded Polymorphism Plus SelfType
  // It means that if A<: Animal  it Requires A also that Means in short it should be A i.e
  // Both A should be equal or  Whatever Decedents of Animal[A] we will implement they must als be an A
  /*
  trait Animal[A <: Animal[A]]{ self: A=>
    def breed:List[Animal[A]]
  }

  class Cat extends Animal[Cat]{
    // this type recursive forces the type restriction now it will return only List[Cat]
    override def breed: List[Animal[Cat]] = ???
  }


  class Dog extends Animal[Dog]{
    override def breed: List[Animal[Dog]] = ???
  }

  class Crocodile extends Animal[Crocodile]{
    override def breed: List[Animal[Crocodile]] = ???
  }

  // But still problem is not solved Lets explain this with example
  //Lets say we have a new hierarchy based on above hierarchy
  // Hence Once we down the class Hierarchy one level then
  // Fbounded polymorphism fails
  trait Fish extends Animal[Fish]
    class Shark extends Fish{
      // again same problem it should Return List[Animal[Shark]]
      override def breed: List[Animal[Fish]] = List(new Cod)
    }

  class Cod extends Fish{
    // again same problem it should Return List[Animal[Shark]]
    override def breed: List[Animal[Fish]] = ???
  }

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
  object HtmlSerializer{
    // it take type class instance as argument and the type T
    def serialize[T](value : T)(implicit serializer:HtmlSerializer[T]):String=
      serializer.serialize(value)
    // for more better design
    def apply [T](implicit serializer: HtmlSerializer[T]) = serializer
  }
   */
//Animal instance
  object Dog {
  // type class instance
  implicit object DogCanBreed extends CanBreed[Dog] {
    override def breed(a: Dog): List[Dog] = List()
  }
  }
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


   */
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
// last component to complete type class pattern
  implicit class AnimalOps[A](animal:A){
  def breed (implicit animalTypeClassInstance: Animal[A]): List[A] =
    animalTypeClassInstance.breed(animal)
}
  val dog= new Dog
  val x: Seq[Dog] =dog.breed
}