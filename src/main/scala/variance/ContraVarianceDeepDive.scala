package variance



object ContraVarianceDeepDive {

  /*
TODO
   So what’s variance?
  It’s that cute little question: if dogs are animals,
  could lists of dogs be considered lists of animals as well?
  This is the variance question,
  i.e. whether the subtype relationship can be transferred to generic types.
 If the answer to the variance question is “yes”,
 then we consider the generic type covariant,
  and in Scala we write a + next to the type argument, like
  abstract class List[+T]
   */
  class Animal

  class Dog(val name: String) extends Animal

  class Cat(val name: String) extends Animal

  abstract class MyList[+T]

  val laika: Animal = new Dog("Laika")
  val lassie: Animal = new Dog("Laika")
  val hachi: Animal = new Dog("Laika")
  val myDogs: List[Animal] = List(lassie, hachi, laika) // List[Dog]
  /*
TODO Explanation of above Code
   This is more easily understood. Dog subtype of Animal, therefore List[Dog] subtype of List[Animal].
    However, that’s not the only possible answer.
    We can also have no variance (no + sign),
   which makes a generic type invariant.
    This is the Java way of dealing with generics.
    Dog subtype of Animal? I don’t care. List[Dog] and List[Animal]
    are two different types, with no relationship between them. That means if you write
   */
  /*
  TODO Contravariance: ->
     Let’s get this straight.
     So we have Dog subtype of Animal,
     and we’re wondering what could be the relationship between List[Dog] and List[Animal].
     In the contravariance answer,
     we would have a list of animals being a subtype of a list of dogs,
     which is the exact opposite of covariance above. The following will be very confusing.
     When you write a minus in the generic type,
     that’s a marker to the compiler that the
     generic type will have the subtype relationship exactly opposite to the types it wraps.
     Namely, MyList[Animal] is a subtype of a MyList[Dog].
     The code below would compile,
     but we would not be happy,
     because it makes no sense.
     Why would we write that for a list? Why would a list of animals be a SUBTYPE of a list of dogs?
   */

  class MyContraList[-T]

  val myAnimals: MyContraList[Dog] = new MyContraList[Animal] // some animals

  /*
  TODO Contravariance use case :->
      Let’s go back to the Dog-Animal relationship and
      let’s try to imagine something like a Vet, which can heal an animal
      I’ve already defined it with a -T, for the following reason:
      if you ask me “Prem,
      gimme a vet for my dog” and I’ll give you a vet which can heal ANY animal,
      not just your dog, your dog will live.
   */
  trait Vet[-T] { // we can also insert an optional -T <: Animal here if we wanted to impose a type constraint
    def heal(animal: T): Boolean
  }

  def gimmeAVet: Vet[Dog] = new Vet[Animal] {
      override def heal(animal: Animal): Boolean = {
        true
      }
  }

  val myDog = new Dog("Buddy")
  val myVet: Vet[Dog] = gimmeAVet
  myVet.heal(myDog)
  /*
 TODO
  This example is from the last project I was working on.
    Say you have a type-class PrettyPrinter[A]
  that provides logic for pretty-printing objects of type A.
    Now if B >: A (i.e. if B is superType of A)
  and you know how to pretty-print B
    (i.e. have an instance of PrettyPrinter[B] available)
  then you can use the same logic to pretty-print A.
  In other words, B >: A implies PrettyPrinter[B] <: PrettyPrinter[A].
    So you can declare PrettyPrinter[A] contravariant on A.

  */
  trait VetDoctor[-A] {
    def cure(a: A): String
  }

  def vetCureApi[A](a: A)(implicit p: VetDoctor[A]) = p.cure(a)

  implicit object AnimalDoctor extends VetDoctor[Animal] {
    def cure(a: Animal) = "[Animal : %s]" format (a)
  }
  vetCureApi[Dog](new Dog("Tom"))
  // TODO : here as we can see that
  //  we have declared the PrettyPrinter[Dog] but we have passed
  // TODO the reference of Animal because of contravariance implicitly
  def main(args: Array[String]): Unit = {

  }
}
