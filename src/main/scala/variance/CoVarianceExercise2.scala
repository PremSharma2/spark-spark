package variance

object CoVarianceExercise2 extends App {


  trait Animal {
    def name: String
  }

  case class Cat(name: String) extends Animal
  case class Dog(name: String) extends Animal

  /**
   *
   * TODO
   *     +A <: Animal: This says A is a covariant type parameter that is a subtype of Animal.
         B >: A: This says B is a supertype of A.
        The error occurs because there is no constraint that forces B to be a subtype of Animal,
        even though A has to be. Essentially, this means that if A is some subtype of Animal,
        B can be any supertype of A even if it's not related to Animal at all,
        which is clearly not what you intended.

       To fix the error, you need to ensure that B is also constrained as a subtype of Animal:
   */
  class Container[+A <: Animal] private (private val animals: List[A]) {

    //def add[B >: A](animal: B): Container[B] = new Container(animal :: animals)
    //By adding <: Animal after B >: A, you ensure that B is also a subtype of Animal.
    def add[B >: A <: Animal](animal: B): Container[B] = new Container(animal :: animals)

    def get(index: Int): Option[A] = animals.lift(index)

    def size: Int = animals.length

    override def toString: String = animals.reverse.mkString(", ")
  }

  object Container {
    def empty[A <: Animal]: Container[A] = new Container(List.empty[A])
  }



  val catsContainer: Container[Cat] = Container.empty[Cat].add(Cat("Whiskers")).add(Cat("Fluffy"))
  println(catsContainer)  // Prints: Cat(Whiskers), Cat(Fluffy)

  val mixedContainer: Container[Animal] = catsContainer.add(Dog("Buddy"))
  println(mixedContainer)  // Prints: Dog(Buddy), Cat(Whiskers), Cat(Fluffy)

  val maybeCat: Option[Cat] = catsContainer.get(0)
  println(maybeCat)  // Prints: Some(Cat(Whiskers))

  val maybeDog: Option[Animal] = mixedContainer.get(0)
  println(maybeDog)  // Prints: Some(Dog(Buddy))
}
