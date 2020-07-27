package variance

object VarianceExercise  extends App{
  /*
   * Write an Monadic Implementation  (Invariant,covariant,contravariant) version of Parking API
   * abstract class Parking[T](ListOfAllVehicles :List[T]){
   * park(vehicle:T)
   * impound(vehicles:List[T])
   * checkVehicles(conditions:String):List[T]
   * 
   * }
   * 
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
    def flatMap[S](f : T => IParking[S]): IParking[S]= ???
  }
  
  //Covariant API Implementation
  class CParking[+T](vehicles :List[T])   {
    // we need to do the hecking of compiler to avoid Second ThumB Rule for variance
    def park [S>:T] (vehicle:S):CParking[S]= ???
    def impound [S>:T] (vehicles:List[S]):CParking[S]= ???
    def checkVehicles(conditions:String):List[T] = ???
    def flatMap[S](f : T => CParking[S]): CParking[S] = ???
    //def flatMap(f : T => CParking[T]): CParking[T] = ???
  }
  //Contravariant implementation
  
  class XParking[-T] (vehicles :List[T]){
     def park(vehicle:T):XParking[T]= ???
     def impound (vehicles:List[T]):XParking[T]= ???
     // we need to do the hecking of compiler to avoid Third ThumB Rule for variance
     def checkVehicles [S<:T](conditions:String):List[S] = ???
     // def flatMap[S](f : T => CParking[S]): CParking[S] = ???
     /*
      * 
      * Error:
      * contravariant type T occurs in covariant position in type T ⇒ com.scala.variance.VarianceExercise.CParking[S] of value f
      * contravariant type T occurs in covariant position in type T ⇒ 
      * com.sapient.variance.problem.VarianceExercise.XParking[S] of value f
      *
      * Explanation:
      *
      * this is basically nothing but (f: Function1[T,XParking[S]]
      * and Function1 is at contravariant position on its input params because of second thumb rule
      * by API specification for Function1 in scala api docs (T is contravariant and XParking[S] is covariant position)
      * But Problem here is that T has become double contravariant that eventually will be equal to  covariant
      * Double Contravariant hence this position is becomes Covariant so this is Covariant Position now not contravariant
      * we need to make changes with respect to T
      */
     def flatMap [R<:T,S](f : R => XParking[S]): XParking[S] = ???
  }
  
  
  //Here error was covariant type occurs at invariant position bcz IList is  Invariant
  //IList[T] is invariant,So We are making Invariant List to covariant
  
   //Covariant Implementation
  class CParking2[+T](vehicles :IList[T])   {
    // we need to do the hacking of compiler to avoid second ThumB Rule for variance i.e to make the method argument is at
     // contravariant position
    def park [S>:T] (vehicle:S):CParking2[S]= ???
    /*
     * using IList[T] as an argument instead of IList[S] is not legal because 
         you cannot supply an invariant parameter 
         with a covariant value
     */
    //covariant type T occurs in invariant position in type com.scala.variance.VarianceExercise.IList[T] of value vehicles
     //def impound  (vehicles:IList[T]):CParking2[T]= ???
    def impound [S>:T] (vehicles:IList[S]):CParking2[S]= ???
    //covariant type T occurs in invariant position in type (conditions: String)
     //Covariant type T occurs in invariant position in type VarianceExercise.IList[T] of value checkVehicles
    def checkVehicles [S>:T](conditions:String):IList[S] = ???
     //Covariant type T occurs in contravariant position in type T of value S
    // def checkVehicles [S<:T](conditions:String):IList[S] = ???

   //  def checkVehicles (conditions:String):IList[T] = ???
  }
  //Contravariant monadic  implementation
  
  class XParking2[-T] (vehicles :IList[T]){
     def park(vehicle:T):XParking2[T]= ???
     /*
     def impound (vehicles:IList[T]):XParking2[S]= ???
      *Error: contravariant type T occurs in invariant position in type com.scala.variance.VarianceExercise.IList[T] of value vehicles
      * Reason: you cannot supply an invariant parameter
         with a contravariant type value
         This IList is invarient and you are supplying the contravarient type
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
      /*
       def impound (vehicles:IList[T]):XParking2[T]= ???
       Contravariant type T occurs in invariant position in type VarianceExercise.IList[T] of value vehicles
       */
     // we need to do the hecking of compiler to avoid Second ThumB Rule for variance
     //if we use S>:T then 
     //contravariant type T occurs in covariant position in type >: T of type S hence it is covariant position
    //  Note : Scala smartly converted this Contravariant  position into Covariant
     def checkVehicles [S<:T](conditions:String):IList[S] = ???
  }
}