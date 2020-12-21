package catz.datamanipulation

import cats.Eval

object FunctionalState  extends App {

  //TODO In functional programming the state is Data Structure
  //TODO state is nothing but it is equivalent to iterative computations of oops
    //Here S is the state we need to manage and A is the desirable value we obtain from single computation
  // here also as we can see that here in Function type
  // from a state initial S we obtain new  state S i.e of same type plus we get the
  // result A of single computation
  //val f: Int => (Int, String) = ???  // or Function1[Int, Tuple[Int, String]] so we type alisaes with MState[Int,String]
     val f: Int => (Int, String) = (x:Int)=> (x,"Hello")
  type MState[Int,String] =  Int => (Int, String)  // // or Function1[Int, Tuple[Int, String]]
  type MyState[S,A] = S => (S,A)
 import cats.data.State
  /*
  final class IndexedStateT[F[_], SA, SB, A](val runF: F[SA => F[(SB, A)]])
  type StateT[F[_], S, A] = IndexedStateT[F, S, S, A]
  //new MyMonad[Option]
  type State[S, A] = StateT[Eval, S, A]
   def apply[S, A](f: SA => (SB, A)): State[SB, A] =
    IndexedStateT.applyF(Now((s: S) => Now(f(s))))

     def applyF[F[_], SA, SB, A](runF: F[SA => F[(SB, A)]]): IndexedStateT[F, SA, SB, A] =
    new IndexedStateT(runF)

    final class IndexedStateT[F[_], SA, SB, A](val runF: F[SA => F[(SB, A)]])
   */
  // state is a wrapper over function the idea here is that we are changing the state via pure FP
  val countAndSay : State[Int,String]= State.apply(
                                      currentCount => (currentCount+1,s"incremented the state"))
val tuple: (Int, String) = countAndSay.run(10).value
/*
def run(initial: SA)(implicit F: FlatMap[F]): F[(SB, A)] =
//f: (initial: S) => Now(fx(initial))
    F.flatMap(runF)(f => f.apply(initial))
 */

  val result: Eval[(Int, String)] = countAndSay.run(10)

  println(result.value)
  //TODO iterative computation in oops
  var a = 10
  a+=1 // state s1
  val firstComputation = s"Added 1 to 10 , obtained $a"
  a*=5  // state s2
  val secondMutation= s"Multiplied with 5 , obtained $a"


//TODO But what if we have to implement the same computation In pure FP
  val firstTransformation: State[Int, String] = State.
    apply((s:Int) => (s+1, s"Added 1 to 4, and obtained ${s+1}" ))
  val secondTransformation: State[Int, String] = State.
                               apply((s:Int) => (s*5, s"Multiplied with 5 and  obtained  ${s*5}" ))
/*
TODO in map scala have chained two functions using andThen def i.e composed two functions
// TODO : chaining of transformation can only be dine via flatMap

//TODO Lets talk about map function first
def map[B](f: A => B)(implicit F: Functor[F]): IndexedStateT[F, SA, SB, B] =
    transform { case (s, a) => (s, f(a)) }

    def transform[B, SC](f: (SB, A) => (SC, B))(implicit F: Functor[F]): IndexedStateT[F, SA, SC, B] =
    IndexedStateT.applyF(F.map(this.runF) { sfsa =>  //SA => F[(SB, A)]
      AndThen(sfsa).andThen { fsa => //Eval[(SB, A)]
        F.map(fsa) { case (s, a) => f(s, a) }
      }
    })

 def applyF[F[_], SA, SB, A](runF: F[SA => F[(SB, A)]])
 */
  /*
  def run(initial: SA)(implicit F: FlatMap[F]): F[(SB, A)] =
    F.flatMap(runF)(f => f(initial))
   */
  val tansformedComputation = firstTransformation.map(s=> "state-changed")
  val transfrmedresult: Eval[(Int, String)] =tansformedComputation.run(2)

//composing two transformations
  // TODO here first computation will run
  // and the o/p of i.e (Int,String) will be given second transformation
  // i.e state as well as value returned by single computation as input
  // so 5 will be given to second transformation
  /*
   def flatMap[B, SC](fas: A => IndexedStateT[F, SB, SC, B])(implicit F: FlatMap[F]): IndexedStateT[F, SA, SC, B] =
    IndexedStateT.applyF(F.map(this.runF) { safsba => //  SA => F[(SB, A)]
      AndThen(safsba).andThen { fsba => // F[(SB, A)]
        F.flatMap(fsba) {
          case (sb, a) =>
            fas(a).run(sb)
        }
      }
    })

   */
  // TODO this is classical example of the composing two state
  //  transformations i.e o/p of one state transformation
  // TODO is the input to the other state transformation
  //TODO flatMap is used for this compose transformation

  val fx: String => State[ Int,(String, String)]=
    firstResult => secondTransformation.
    map(secondResult => (firstResult,secondResult))

val composedTransformation:State[Int,(String,String)] = firstTransformation.flatMap(fx)


 val composedResult: Eval[(Int, (String, String))] = composedTransformation.run(4)
  println(composedResult.value)

  val FirstTransformation: State[Int, String] = State.
    apply((s:Int) => (s+1, s"Added 1 to 4, and obtained ${s+1}" ))


  val compositeTransformation: State[Int, (String, String)] = for {
      firstResult <- FirstTransformation
      secondResult <-  secondTransformation
  } yield (firstResult,secondResult)
// TODO : we can achieve this by chaining the two functions
  // TODO and that's what scala does internally
  val func1: Int => (Int, String) = (s:Int) => (s+1, s"Added 1 to 4, and obtained ${s+1}" )
  val func2: Int => (Int, String) = (s:Int) => (s*5, s"Multiplied with 5 and  obtained  ${s*5}" )
/*

Composes two instances of Function1 in a new Function1, with this function applied first.
Params:
g – a function R => A
Type parameters:
A – the result type of function g
Returns:
a new function f such that f(x) == g(apply(x))
 def andThen[A](g: R => A): T1 => A = { x => g(this.apply(x)) }
 */
  val composeFunction: Int => (String, (Int, String)) = func1.andThen{
    case (newState,firstState) => (firstState,func2(newState))
  }
  println(composeFunction.apply(2))


  //TODO Exercise Online store
  case class ShoppingCart(items:List[String], total:Double)

  def addtoCart(item:String , price:Double ):State[ShoppingCart,Double] = {
    State.apply{
      cart => (ShoppingCart(item :: cart.items, cart.total + price),price + cart.total)
    }

  }

  /*
  val compositeTransformation: State[Int, (String, String)] = for {
      firstResult <- FirstTransformation
      secondResult <-  secondTransformation
  } yield (firstResult,secondResult)
   */

  val result1: State[ ShoppingCart, Double] =addtoCart("Apple",20)
    .flatMap(fr => addtoCart("banana",30).flatMap{
    sr => addtoCart("ElectricOven",50).map(tr => tr)
  })
val premcart:State[ShoppingCart,Double] = for{
  a <- addtoCart("Apple",20)
  b <- addtoCart("banana",30)
  total <- addtoCart("ElectricOven",50)
} yield total

  println(premcart.run(ShoppingCart(List(),0.0)).value)
}
