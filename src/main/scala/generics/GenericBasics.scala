package generics

object GenericBasics extends App {
  //covariant list
  class MyGenrcList [+A] {
    // if we try to add dog in a covariant list then it will turn that list to more generic type i.e of Animal
    //i.e in a list of A if i try to put in a Type B then this list will convert into of type B which is super type of A
    def add[B>:A](element:B):MyGenrcList[B]={
      ???
    }
  }

  val listOfIntegers= new MyGenrcList[Int]
  val listOfStrings= new MyGenrcList[String]


  object MyEmptyList{
    //here this method empty take a parameter of type A
    def   empty[A] :MyGenrcList [A]={

      ???
    }
  }
  val listofIntegers=MyEmptyList.empty[Int]

  // variance problem

  class Animal
  class Cat extends Animal
  class Dog extends Animal
  //List of cat extends List of animals this is called covariance
  class CovariantList[+A](animal:A)
  //or
  //class CovariantList[Animal](animal:A)
  val animal:Animal=new Cat
  //here we have declared as animal and passed it with cat i.e this List first element is cat and now it can extend to animals
  val animalList: CovariantList[Animal]=new CovariantList[Cat](new Cat)
  //what if we try to add dog element in this list?? As it suggests that it can be extended to List[Animals]
  //pls refer add method of this list above for this question
  //animallist.add(new Dog)
  class InVariantList[A]


  val inVariantAnimalList: InVariantList[Animal]=new InVariantList[Animal]

  //ContraVariant this can extend up to Super Type of Animals

  class ContraVariant[-A]
  val contraVariantList: ContraVariant[Cat]=new ContraVariant[Animal]
  //Here We have declared the Trainer Type of Cat and passed the Animal
  class Trainer[-A]

  val trainer :Trainer[Cat]= new Trainer[Animal]

  // bounded types
  class Cage [A <: Animal](animal:A)
  val cage=new Cage(new Dog)

  //lower Bounded Type
  //it will accept Animal or its super types
  class Zoo [A >: Animal](animal:A)

  class Grandparent
  class Parent extends Grandparent
  class Child extends Parent

  class InvariantClass[A]
  class CovariantClass[+A]
  class ContravariantClass[-A]

  class VarianceExamples {

    def invarMethod(x: InvariantClass[Parent]) {}
    def covarMethod(x: CovariantClass[Parent]) {}
    def contraMethod(x: ContravariantClass[Parent]) {}

   // invarMethod(new InvariantClass[Child]             // ERROR - won't compile
    invarMethod(new InvariantClass[Parent])            // success
    // invarMethod(new InvariantClass[Grandparent])       // ERROR - won't compile

    covarMethod(new CovariantClass[Child])             // success
    covarMethod(new CovariantClass[Parent])            // success
    // covarMethod(new CovariantClass[Grandparent])       // ERROR - won't compile

    //contraMethod(new ContravariantClass[Child])        // ERROR - won't compile
    contraMethod(new ContravariantClass[Parent])       // success
    contraMethod(new ContravariantClass[Grandparent])  // success
  }

  class Animal1Cage[+T](val animial:T)

  class Dog1
  class Puppy extends Dog1

  class AnimalCarer(val dog:Animal1Cage[Dog1])

  val puppy = new Puppy
  val dog = new Dog1

  val puppyAnimal:Animal1Cage[Puppy] = new Animal1Cage[Puppy](puppy)
  val dogAnimal:Animal1Cage[Dog1] = new Animal1Cage[Dog1](dog)

  val dogCarer = new AnimalCarer(dogAnimal)
  val puppyCarer = new AnimalCarer(puppyAnimal)
  trait AnimalCage[-T]{
   def  add(animal :T)
  }

  class AnotherContravariantCage extends AnimalCage[Dog1] {
    override def add(animal: Dog1): Unit = ???
  }
  val accCage: AnotherContravariantCage = new AnotherContravariantCage
}