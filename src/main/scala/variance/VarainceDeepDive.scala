package variance

import generics.GenericBasics.{Child, ContravariantClass, CovariantClass, Grandparent, InvariantClass, Parent}


object VarainceDeepDive extends App {

  trait Animal
  class Dog extends Animal
  class Crocodile extends Animal
  class Cat extends Animal
  //what is varaince ?
  //problem of inheritance - type substitution of Generics
  //Its all about substituting the Type of Generic Class or FuntionParameter at Run Time
  /*
   * Variance defines Inheritance relationships of Parameterized Types. Variance is all about Sub-Typing.
   * 
   * For List[T], if we use List[Int], List[AnyVal], etc. then these List[Int] and List[AnyVal] are known as “Parameterized Types”
   * Variance defines Inheritance relationship between these Parameterized Types.
   * 
   * Types of Variance in Scala
     Scala supports the following three kinds of Variance.

    Covariant
    Invariant
    Contravariant


    Covariant in Scala
     If “S” is subtype of “T” then List[S] is is a subtype of List[T].
     This kind of Inheritance Relationship between two Parameterized Types is known as “Covariant”
     
     Contravariant in Scala
     If “S” is superType of “T” then List[T] is is a subtype of List[S].

    This kind of Inheritance Relationship between two Parameterized Types is known as “Contravariant”
    
    abstract class Type [-T]{
  def typeName : Unit
}

class SuperType extends Type[AnyVal]{
  override def typeName: Unit = {
    println("SuperType")
  }
}
class SubType extends Type[Int]{
  override def typeName: Unit = {
    println("SubType")
  }
  
  class TypeCarer{
  def display(t: Type[Int]){
    t.typeName
  }
}

** Here as we can see that we have declared Int but we are passing AnyVal so it is valid,because t is at contravarient position

object ScalaContravarianceTest {

  def main(args: Array[String]) {
    val superType = new SuperType this is Type[AnyVal] Type
    val subType = new SubType   this is Type[Int]

    val typeCarer = new TypeCarer

    typeCarer.display(subType)
    typeCarer.display(superType)
  }
}
   * 
   */



  class Kitty extends Cat
  class Cage[T]
  class CCage[+T]
  val covariantCage: CCage[Animal] = new CCage[Cat]
  class XCage[-T]

  val contraVariantCage: XCage[Cat] = new XCage[Animal]
/*
The compiler checks each use of each of the class’s type parameters. Type parameters annotated with +
may only be used in positive positions,
while type parameters annotated with - may only be used in negative positions.
A type parameter with no variance annotation may be used in any position,
 and is, therefore, the only kind of type parameter that can be used in neutral positions of the class body.
 */
  //covariant positions Constructor Argument
  //Here, T is the type parameter and + is the symbol of Covariance
  class CovariantCage[+T](val animal: T)
  class MyCage(animal:Animal) extends CovariantCage[Animal](animal)
   val catCage:CovariantCage[Animal]=new CovariantCage[Cat](new Cat)
  //Golden First Thumb Rule for constructor Argument is 
  // (val aanimal:T) this means that this class will only accept Covarient Types at this position

  //class ContraVaraintCage[-T](val animal:T)
  /*here this is the error
   * contravariant type T occurs in covariant position in type ⇒ T of value animal
   *
   * Reason for this Error is as follows: :
   *
   * if somehow compiler compiles it then
   * class ContraVariantCage[-T](val animal:T)
   *
   *
   * val catcage:ContraVariantCage[Cat]=new ContraVariantCage[Animal](new CrocoDile)
   * the problem here is that we wanted a specific cage i.e cat cage and we are filling with some other types of animal
   * although it can accept animals so this is logically wrong compiler is not allowing illogical argument
   * i.e After Declaring it Type[Cat] how can we put CrocoDile in it it is logically Wrong
   *
   */
   //--------------------USE OF VAR INSTEAD OF VAL-------------------------------------------------------------------------------
   /*
    * When we pass var instead of val then still it will not compile ,It will throw an error
    * Covariant Type occurs in contravariant Position in type T of value animal
    * class CoVariantVariableCage[+T](var animal:T) // types of var  animal  is at contravariant position
    *
    * Reason:
    *
    * because if somehow compiler pass this code then i can write
    * val cage:CoVariantVariableCage[Animal]= new CoVariantVariableCage[Cat](new Cat)
    * that is fine ,now cage is of animal so i can put another animal inside cage:CoVariantVariableCage[Animal] as this var you can change the reference variable
    * and we changed the attribute of CoVariantVariableCage
    *
    * cage.animal=new Crocodile
    * that in theory is fine because we have not violated the first thumb rule 
    * problem here is that the you have instantiated with specific type and you are passing Generic type that is also logically wrong
    */
   //Lets take another example
   /*
    * class ContraVariantVariableCage[-T](var animal:T) // types of var animal   is at  covariant position
    * 
    * here it will not compile and throw compile time error
    * contravariant Type occurs in covariant Position in type T of value animal
    * 
    * Reason:
    * because if somehow compiler pass this code then i can write
    * val cage:ContraVariantVariableCage[Cat]= new ContraVariantVariableCage[Animal](new Cat)
    * 
    * Hence after looking into the last two examples we can come to conclusion is that only acceptable type for var is at Invariant Position
    * 
    * class InVaraintVariableCage[T](var animal:T) 
    * no error
    * 
    */
  /*
   * contravariant type T occurs in covariant position in type ⇒ T of variable animal
   * val catcage:ContraVaraintCage[Cat]=new ContraVaraintCage[Animal](new CrocoDile)
   * here also same logical error we are doing as we were doing earlier
   */
   
   //-------------------------------------------Method Argument types Analysis---------------------------------------------------------------------------
  //trait AnotherCovariantCage[+T]{
  // def addAnimal(animal:T) //Method argument are in contravariant position
  //covariant type T occurs in contravariant position in type T of value animal
  //method arguments are always in contravariant position
  //here method argument is again in contravariant position
  //  }


   //Reason :
  //i.e if method argument is declared as cage:CCage[Animal] hence it can accept Cage[Animal] or its sub Types
  //val cage:CCage[Animal]=new CCage[Dog]
  //cage.addAnimal(new Cat)
  //We do not want cats and dogs are at same place that's why compiler does not allow us
  //this is logically wrong Cats and Dogs cant be in Same Cage it should be either cat or Dog
 // Second Thumb rule for Method arguments is that they are at Contravariant Position
   
   
   

  class AnotherContravariantCage[-T] {
    // Acc to Second Thumb rule this is fine this will compile
    def addAnimal(animal: T) = true
  }
  val accCage: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]

  accCage.addAnimal(new Cat)
  accCage.addAnimal(new Kitty)
  //we cannot add this one
//accCage.addAnimal(new Dog)
  
  
  ///We want to Build A Covariant Collection i.e Collection of All kinds then we need to break the Rule for that we will heck the compiler
  class MyList[+A] {
    // We need to make the changes in order to built a covariant list otherwise it will throw an error
    //of contravariant position
    //we hack the compiler and told him this will accept the argument B which is super type of A and we will add that element into collection
    //and will return collection of that type i.e B
    //this is basically widening the type
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }
  val emptyList1: MyList[Animal] = new MyList[Cat]
  val emptyList: MyList[Cat] = new MyList[Kitty]
  val animals: MyList[Cat] = emptyList.add(new Kitty)
  val moreAnimals: MyList[Cat] = animals.add(new Cat)
  val evenMoreAnimal: MyList[Animal] = moreAnimals.add(new Dog)

  
  
  
  
  
  //Third Thumb rule is that Method return type is at covariant Position

  class PetShop[-T] {
    // def get(isItAPuppy:Boolean):T={   //Method return type are in covariant position
    //contravariant type T occurs in covariant position in type (isItAPuppy: Boolean)T of method get
    //Due to the Third thumb rule we are getting this error
    /*Reason For this Error:

      * val catShop:PetShop[Cat]=new PetShop[Animal]{
      * def get(isItAPuppy: Boolean):Animal=new Cat
      * }
      *if we have allowed contravariant as return type then type safety rule violates for eg:
      * val dogShop:PetShop[Dog]=catShop
      * this is looks perfect bcz catShop IS petShop[animal] of animal and  PetShop is contravariant and catshop is super type of dogshop
      * now dog shop is equal to catShop,Which is illogical hence we cannot have Contravariant in Return Position
      * dogShop.get(true)
      * but this will return me an EVIL CAT!!! which is logically wrong how can PetShop[Dog] return an Cat
      * That is why scala made second golden thumb rule ,And MoreOver this is logically wrong so compiler will thow an error
      */
    //}
    //Solution of above problem is to hack the Compiler and make the return type like it is covariant
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }
  val shop: PetShop[Dog] = new PetShop[Animal]
  class RoteWoiler extends Dog
  val bigFurry=shop.get(true, new RoteWoiler)
  //val animal=shop.get(false, new Cat)
  //val catshop:PetShop[Cat]=shop
}