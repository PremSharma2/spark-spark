package monads

object MonadExercise extends App {

  /*
 TODO
  Here Lazy monad or Lazy Monad means the value contained by monad will be lazy evaluated
  i.e., we will use callByName expression
  Hence so forth we need to make changes in map and flatMap functions of Monad
  implements a Lazy[T] monad = computation will only be executed when its needed
  apply
  flatMap
  these contract should be implemented
   The aim of LazyMonad is to:
         Accept any expression (including side-effects or expensive computations)
          Delay its evaluation
          Allow you to chain computations via flatMap and map without evaluating anything until getValueFromContainer is called
           Make sure everything remains lazy across all monadic operations


   */

  class LazyMonad[+A](valueExpression : => A){
     lazy val internalValue: A =valueExpression
    //here we have changed the Func type A to =>A i.e., input to function is also callByName
    // expression i.e fx: (=> A)=> Lazy[B]
    // now fx.apply(value) it will not be evaluated because here input to fx is call by name
    //The function fx takes a lazy argument — it may or may not evaluate it
  def flatMap[B] (fx: (=> A)=> LazyMonad[B]): LazyMonad[B] = fx.apply(this.internalValue)

    //This is where the chain ends and forces evaluation
    def getValueFromContainer: A =
    internalValue
    def map [B] (fx: A=>B ): LazyMonad[B] = this.flatMap(x=> LazyMonad.apply(fx(x)))
  }
  object  LazyMonad{
    def apply [A](value: => A): LazyMonad[A] = new LazyMonad(value)
  }

val lazyInstance: LazyMonad[Int] = LazyMonad.apply{
  println("Today i dont feel like to do anything")
  42
}
 // here x => Lazy.apply(10*x) is used to implement ETW pattern
    val flatMapppedInstance: LazyMonad[Int] = lazyInstance.flatMap(x => LazyMonad.apply(10*x))

println(flatMapppedInstance.getValueFromContainer)
  println(flatMapppedInstance.getValueFromContainer)
  /*
  Lets proof all the monad laws:
  1 law : => Left Identity
  Lazy.apply(v).flatMap(fx)= fx(v)
  Here as we can see in our FlatMap implementation it is doing same thing
  Hence it satisfies first law

  -----------------------------------------------
  2nd Law: Right Identity
  lazyValInstance.flatMap(applyfunction)= lazyValInstance
  Lets proof this here
  val lazyInstance=Lazy.apply(v)
  lazyInstance.flatMap(x => Lazy.apply(x))
  output is this flatMap operation is Lazy(v) hence the same instance
  Hence second law is also proved
----------------------------------------------------------

3rd Law Associativity

val lazyInstance=Lazy.apply(v)
lazyInstance.flatMap(f).flatMap(g) = lazyInstance.flatMap(x=> fx(x).flatMap(g))
Lets Proof this lets take LHS
we can write the LHS as follows
Lazy(v).flatMap(f).flatMap(g) = fx(v).flatMap(g)
and now take RHS of equation
lazyInstance.flatMap(x=> fx(x).flatMap(g))
and this composite function what it says that
apply this composite function on the value contained in lazyInstance
fx(v).flatMap(g) and this does the same
hence both Rhs and lhs are doing same thing hence proved

i.e
// Exercise number 2 map and flatten in turms of flatmap
Monad[A]{

   def flatMap[B] (fx: (=> A)=> Lazy[B]): Lazy[B] = fx.apply(internalValue)
   def map [B] (fx: (A=>B) ): Monad[B] = flatMap(x=> unit(fx(x)))

 def flatten(m:Monad[Monad[T]]):Monad[T] = m.flatMap(x:Monad => x)

 lets proof this
 fx.apply(this.head) ++ this.tail.flatMap(fx)
 List(1,2,3).map(_*2)= List(1,2,3).flatMap(x => List.apply(x*2))
   */

val list :List[_] = List(1,2,3)

  // test the lazynes
  val m1 = new LazyMonad[Int]({
    println("Evaluating m1!")
    10
  })

  val m2 = m1.map(x => { println("Doubling!"); x * 2 })
  m2.internalValue

  /*
  TODO
      since map is implemented like this
     map(fx) = flatMap(x => LazyMonad(fx(x)))
     This expands to
    val m2 = m1.flatMap(x => LazyMonad({ println("Doubling!"); x * 2 }))
     Here's the key: flatMap is being passed
     a function that returns a new LazyMonad,
     and the input x is by-name — => A.
     flatMap(fx) = fx.apply(internalValue)
     So m1.internalValue is accessed right now.
     flatMap forces internalValue
     fx.apply(m1.internalValue)
     That means m1.internalValue must be evaluated → triggers:
      println("Evaluating m1!")
          10
          Now internalValue = 10 is returned and passed (as x) into:
          x => LazyMonad({ println("Doubling!"); x * 2 })
          This creates a new LazyMonad:
          LazyMonad({ println("Doubling!"); 10 * 2 })






   */



  /*
  TODO
   m1 → evaluated during first flatMap
    ↳ m2 → evaluated during second flatMap
      ↳ m3 → final value, evaluated in getValue
    */

  m1.flatMap(x => LazyMonad({ println("Doubling!"); x * 2 })).flatMap(x=> LazyMonad(x))


}
