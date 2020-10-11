package monads

object MonadExercise extends App {

  /*
  Here Lazy monad or Lazy Monad means the value contained by monad will be lazy evaluated
  i.e we will use callByName expression
  Hence so forth we ned to make changes in map and flatMap functions of Monad
  implement a Lazy[T] monad = computation will only be executed when its needed
  apply
  flaytMap
  these contract shuld be implemented
   */

  class Lazy[+A](value : => A){
    private lazy val internalValue: A =value
    //here we have changed the Func type A to =>A i.e input to function is also callbyname
    // expression i.e fx: (=> A)=> Lazy[B]
    // now fx.apply(value) it will not be evaluated becuase here input to fx is call by name
  def flatMap[B] (fx: (=> A)=> Lazy[B]): Lazy[B] = fx.apply(internalValue)
    def getValueFromContainer= internalValue
    def map [B] (fx: (A=>B) ): Lazy[B] = flatMap(x=> Lazy.apply(fx(x)))
  }
  object  Lazy{
    def apply [A](value: => A): Lazy[A] = new Lazy(value)
  }

val lazyInstance: Lazy[Int] = Lazy.apply{
  println("Today i dont feel like to do anything")
  42
}
 // here x => Lazy.apply(10*x) is used to implement ETW pattern
    val flatMapppedInstance: Lazy[Int] = lazyInstance.flatMap(x => Lazy.apply(10*x))

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



}
