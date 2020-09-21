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

  // this is called recursive Type and A <: Animal[A] is called F bounded Polymorphism Plus SelfType
  // It means that if A<: Animal and also it Requires A also that Means in short it should be A i.e Both A should be equal
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

}
