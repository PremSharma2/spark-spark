package variance

object VarianceExercise  extends App{
  /*
  TODO
   * Write an Monadic Implementation  (Invariant,covariant,contravariant) version of Parking API
   * abstract class Parking[T](ListOfAllVehicles :List[T]){
   * park(vehicle:T)
   * impound(vehicles:List[T])
   * checkVehicles(conditions:String):List[T]
   * 
   * }
   *
   *
   * Imp points for varince
   *
   *TODO --------------------------------------------------
TODO
  def method(...): SomeType
  that SomeType is in a covariant position. Here are the cases:
  If SomeType is a normal type (e.g. Person), then you're good.
  If SomeType is a generic type argument (e.g. T),
 then T must at least not be contravariant in the class where the method is being defined.
 Invariant and covariant is fine.
 *
TODO what if SomeType is higherKinded type List[T] ??
 1 If SomeType is a generic type containing your class' type argument (e.g. List[T]),
 * then the position of SomeType is accepted
 (regardless of whether T is annotated with +/-, much like case 1 above),
 *  but the variance position is now transferred to T. This is the complex part.
 2 If SomeType is covariant in T (e.g. List), then SomeType's variance position transfers to T.
 * In this case, T would be in a covariant position.
 If SomeType is contravariant in T, then SomeType's variance position is transferred backwards to T.
 In this (your) case, T will be in a contravariant position, which matches the original type definition.
 If SomeType is invariant in T, then the position of T is strictly invariant,
 * and T must not have any +/- annotation.
   */
  class IList[T]
  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  //Invariant API
  class InvariantList[T]
  class IParking[T](vehicles:List[T]){
    def park(vehicle:T):IParking[T]= ???
    def impound (vehicles:List[T]):IParking[T]= ???
    def checkVehicles(conditions:String):List[T] = ???
    //ETW pattern
    def flatMap[S](f : T => IParking[S]): IParking[S]= ???
  }
  
  //Covariant API Implementation
  //TODO If your Generic class creates or contains elements of type T it should be +T
  case class CParking[+T](vehicles :List[T])   {
    // we need to do the heck the compiler because method argument are in contravariant position
   // def park [S>:T] (vehicle:S):T= ???
    def get():CParking[T] = CParking(vehicles)
    def park [S>:T] (vehicle:S):CParking[S]= CParking(vehicle :: vehicles)
    def impound [S>:T] (addVehicles:List[S]):CParking[S]= CParking(vehicles ++ addVehicles)
    def checkVehicles(conditions:String):List[T] = ???
    def flatMap[S](f : T => CParking[S]): CParking[S] = ???
    // Due to double Variance function input has become covariant and its o/p has become contravariant
    def map [S>:T](function : T => S): CParking[S] = ???
  }
  //TODO Contravariant implementation are majorly used in type class
  //Todo beacuse we need to take an action on type
  
  class XParking[-T] (vehicles :List[T]){
     def park (vehicle:T):XParking[T]= ???
     def impound (vehicles:List[T]):XParking[T]= ???
     def checkVehicles [S<:T](conditions:String):List[S] = ???
     // def flatMap[S](f : T => CParking[S]): CParking[S] = ???
     /*
     TODO
      * Error:
      * contravariant type T occurs in covariant position in type T ⇒ com.scala.variance.VarianceExercise.CParking[S] of value f
      * contravariant type T occurs in covariant position in type T ⇒ 
      * com.sapient.variance.problem.VarianceExercise.XParking[S] of value f
      *
      * Explanation:
      *
      * this is basically nothing but (f: Function1[T,XParking[S]]
      * and Function1 is at contravariant position on its input params because of second thumb rule
      * by API specification for Function1 in scala api docs (T is contravariant and XParking[S]
      * is covariant position)
      * But Problem here is that T has become double contravariant that eventually will be equal to  covariant
      * Double Contravariant hence this position is becomes Covariant
      *  so this is Covariant Position now not contravariant
      * we need to make changes with respect to T
      */
     def flatMap [R<:T,S](f : R => XParking[S]): XParking[S] = ???
  }
  
  
  //Here error was covariant type occurs at invariant position bcz IList is  Invariant
  //IList[T] is invariant,So We are making Invariant List to covariant
  
   //Covariant Implementation
   //TODO If your Generic class creates or contains elements of type T it should be +T
  class CParking2[+T](  vehicles :IList[T])   {
    // we need to do the hacking of compiler to avoid second ThumB Rule
    // for variance i.e to make the method argument is at
     // contravariant position
    def park [S>:T] (vehicle:S):CParking2[S]= ???
    /*
     * using IList[T] as an argument instead of IList[S] is not legal because 
         you cannot supply an invariant parameter 
         with a covariant value
     */
    //covariant type T occurs in invariant position in
    // type com.scala.variance.VarianceExercise.IList[T] of value vehicles
     //def impound  (vehicles:IList[T]):CParking2[T]= ???
    def impound [S>:T] (vehicles:IList[S]):CParking2[S]= ???
    //covariant type T occurs in invariant position in type (conditions: String)
     //Covariant type T occurs in invariant position in type VarianceExercise.IList[T] of value checkVehicles
    def checkVehicles [S>:T](conditions:String):IList[S] = ???
     //Covariant type T occurs in contravariant position in type T of value S
    // def checkVehicles [S<:T](conditions:String):IList[S] = ???

   //  def checkVehicles (conditions:String):IList[T] = ???
    // Note : Scala smartly converted this Covariant   position into Contravariant to fix this Invariant List
  }
  //Contravariant monadic  implementation
  
  class XParking2[-T] ( vehicles :IList[T]){
     def park(vehicle:T):XParking2[T]= ???
     /*
     def impound (vehicles:IList[T]):XParking2[S]= ???
      *Error: contravariant type T occurs in invariant position
       in type com.scala.variance.VarianceExercise.IList[T] of value vehicles
      * Reason: you cannot supply an invariant parameter
         with a contravariant type value
         This IList is invariant and you are supplying the contravariant type
         Hence this is wrong
         *
         *
         * and to Fix this

         * if we use type restriction like this
         *      def impound [S>:T](vehicles:IList[S]):XParking2[S]= ???
                then it still not accepting it
        Error: contravariant type T occurs in covariant position in type >: T of type S
         this position has become covariant hence we will heck in this way
         [S<:T]
         Note : Scala smartly converted this Contravariant  position into Covariant
      */
     def impound [S<:T](vehicles:IList[S]):XParking2[S]= ???
//Contravariant type T occurs in covariant position in type T of value S
    // because what we are trying here is S is super type of T which is wrong because T is contrvarient
    //
   //  def checkVehicles [S>:T](conditions:String):IList[S] = ???
   def checkVehicles [S<:T](conditions:String):IList[S] = ???
  }
}