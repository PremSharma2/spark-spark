package zio

import java.io.IOException

object  ZioFibers  extends zio.App {

/*
TODO
    computation= value + side effect affect
    The ZIO library is centered around the ZIO type.
    Instances of ZIO are called “effects”,
    which describe anything that a program normally does:
    printing, computing, opening connections, reading, writing etc.
   However it’s worth pointing out that — much like other
  IO monads — constructing such an “effect” does not actually produce it at that moment.
  Instead, a ZIO instance is a data structure describing an effect.
  The ZIO type describes an effect which is caused by an input,
  and can produce either an error or a desired value. As such, it takes 3 type arguments:

TODO
 an input type R, also known as environment
 an error type E, which can be anything (not necessarily a Throwable)
 a value type A
 and we thus have ZIO[-R, +E, +A].
 Conceptually, a ZIO instance is equivalent to a
 function R => Either[E,A],
 and there are natural conversion APIs between ZIO and the standard library structures.


 */
  // The type of println is String => Unit. The program prints the given String to the console
  val result = println("The meaning of life is 42")
  val value = {
    println("The meaning of life is 42")// side effect
    42
  }
  def incrementValue(x:Int) = x+1 // pure function no side effects
/*
TODO
  //In pure FP this Expression must hold True
 //but it does not because their nature is different because
  // because value code block has side effects
  // so we cannot replace value with Expression
  it doesn’t work with code that produces side effects because
  we cannot substitute functions with their results. Such functions are often called impure:

 */
// The type of println is String => Unit. The program prints the given String to the console
val result2 = println("The meaning of life is 42")
  // Using the substitution model, we try to substitute the result of the println execution to the variable
  val result1: Unit = ()
  //...however, after the substitution, the meaning of the program completely changed
  //one more example
   incrementValue(42) ==incrementValue(value)

  //TO overcome this Scala has data-structures which hold the value + computation together
  //Zio[R,E,A] : R: Side effects, E:Exception , A: value produced
  /*
  TODO
      In A ZIO[R, E, A] data structure value is an immutable value that lazily describes a workflow or job.
      The workflow requires some environment R, and may fail with an error of type E, or succeed with a value of type A.
     These lazy workflows, referred to as _effects_, can be informally thought of as functions in the form:
    R => Either[E, A]
    type UIO[+A]= ZIO[Any, Nothing, A]   // Succeed with an `A`, cannot fail , no requirements.
    // data structures to wrap a value or an error
   // the input type is "any", since they don't require any input
   */
//TODO : Effects that cant Fail
//TODO :  type UIO[+A]= ZIO[Any, Nothing, A]  // Succeed with an `A`, cannot fail , no requirements.
/*
TODO
    Since We can never produce a value of type nothing We know  that
    a computation with an Error type of Nothing can never Fail
    because Nothing represents nothing returned
    UIO[+A]= ZIO[Any, Nothing, A]
 */
  val zmol:ZIO[Any,Nothing,Int] = ZIO.succeed(42)
  //type IO[+E, +A]   = ZIO[Any, E, A] // Succeed with an `A`, may fail with `E`  , no requirements.
  //Returns an effect that models failure with the specified error. The moral equivalent of throw for pure code
  //def fail[E](error: => E): IO[E, Nothing]
  // type IO[+E, +A]   = ZIO[Any, Exception, Nothing]  // Succeed with an `A`, may fail with `E`, no requirements
  val fail: IO[String, Nothing] = ZIO.fail("Something went wrong") // notice the error can be of any type

  zmol.exitCode

  //TODO reading and writing to the console are Side-effects or Effects
  // the input type is a Console instance, which ZIO provides with the import
  /*
  object Service {
      private def putStr(stream: PrintStream)(line: String): IO[IOException, Unit] =
        IO.effect(SConsole.withOut(stream)(SConsole.print(line))).refineToOrDie[IOException]
        object Service {

      private def putStrLn(stream: PrintStream)(line: String): IO[IOException, Unit] =
        IO.effect(SConsole.withOut(stream)(SConsole.println(line))).refineToOrDie[IOException]


        Retrieves a line of input from the console.
        Fails with an EOFException when the underlying java.io.Reader returns null.
        val getStrLn: ZIO[Console, IOException, String] =
         ZIO.accessM(_.get.getStrLn)
   */
  import zio.console._
  val greetingZio: ZIO[Console, IOException, Unit] = {
    for {
      _    <- putStrLn("Hi! What is your name?") // handle abstraction of writing to Console using PrintStream
      //def putStrLn(line: => String): ZIO[Console, IOException, Unit]
      name <- getStrLn
      _    <- putStrLn(s"Hello, $name, welcome to Rock the JVM!")
    } yield ()

  }
  import zio.console._
  val fx : String => ZIO[Console,IOException, Unit] = name => putStrLn(s"Hello, $name, welcome to Rock the JVM!")
  val consumerEffect: ZIO[Console, IOException, String] = getStrLn
  val fy : Unit => Unit= _ => ()

                                         val a: ZIO[Console, IOException, Unit] =
                                           putStrLn("Hi! What is your name?").
                                           flatMap(_ => consumerEffect.
                                           flatMap(fx(_).map(fy)))

 // zmol.*>()
/*
TODO
    Methods like flatMap and zip* let you get back to sequential computation,
    just like procedural programming, i.e., doing one thing after another:
 */
  import scala.io.StdIn
  //type Task[+A]     = ZIO[Any, Throwable, A] // Succeed with an `A`, may fail with `Throwable`, no requirements.
  //Imports a synchronous side-effect into a pure ZIO value,
  // translating any thrown exceptions into typed failed effects creating with ZIO.fail.
  val readLine: Task[String] = ZIO.effect(StdIn.readLine())
  def printLine(line: String) = ZIO.effect(println(line))
/*
TODO Understanding map and FlatMap
  // execute readLine, then pass its result to printLine.
  // flatMap can be read like “and then do Expression2 with
  // the result of Expression1”:
  //OR
  //Returns an effect that models the execution of this effect,
  // followed by the passing of its value to the specified continuation function k,
  // followed by the effect that it returns.
  //val parsed = readFile("foo.txt").flatMap(file => parseFile(file))
    OR  We can say that combining the Effects Sequentially



 */
  //type Task[+A]     = ZIO[Any, Throwable, A] // Succeed with an `A`, may fail with `Throwable`, no requirements
val readLine1: ZIO[Any, Throwable, String] = ZIO.effect(StdIn.readLine())
  val echo: ZIO[Any, Throwable, Unit] = readLine1.flatMap(line => printLine(line))
  /*




Todo
  map of ZIO
  Returns an effect whose success is mapped by the specified f function.
  def map[B](f: A => B): ZIO[R, E, B] = new ZIO.FlatMap(self, new ZIO.MapFn(f))
  new ZIO.FlatMap(self, new ZIO.MapFn(f))
 final class FlatMap[R, E, A, B](val zio: ZIO[R, E, A], val k: A => ZIO[R, E, B])
TODO
   final class MapFn[R, E, A, B](override val underlying: A => B) extends ZIOFn1[A, ZIO[R, E, B]] {
    def apply(a: A): ZIO[R, E, B] =
      new ZIO.Succeed(underlying(a))
  }
  abstract class ZIOFn1[-A, +B] extends ZIOFn with Function[A, B]

   */
  val mapZio: ZIO[Any, Throwable, String] =readLine1.map(_.toUpperCase)
  /*
TODO
     you can chain a bunch of flatMap’s together like this
   but it’s easier to read a for-expression/comprehension
   that last line of code is equivalent to this:
   */
  val echo1: ZIO[Any, Throwable, Unit] = for {
    line <- readLine
    _    <- printLine(line)
  } yield ()


  //concurrency
  def printThread = s"[${Thread.currentThread().getName}]"

  val bathTime = ZIO.succeed("Going to the bathroom")
  val boilingWater = ZIO.succeed("Boiling some water")
  val preparingCoffee = ZIO.succeed("Preparing the coffee")

  def sequentialWakeUpRoutine(): ZIO[Any, Nothing, Unit] = for {
    _ <- bathTime.debug(printThread)
    _ <- boilingWater.debug(printThread)
    _ <- preparingCoffee.debug(printThread)
  } yield ()

/*
TODO
     Handling Error with Zio
     You can surface failures with ZIO#either,
     which takes an ZIO[R, E, A] and produces an ZIO[R, Nothing, Either[E, A]].
     Returns an effect whose failure and success have been lifted into an Either.
     The resulting effect cannot fail,
     because the failure case has been exposed as part of the Either success case.
    This method is useful for recovering from ZIO effects that may fail.
   The error parameter of the returned ZIO is Nothing,
   since it is guaranteed the ZIO effect does not model failure.
 */
//type UIO[Either]      = ZIO[Any, Nothing, Either]
  val zeither: UIO[Either[String, Int]] =
    IO.fail("Uh oh!").either
/*
TODO
    You can submerge failures with ZIO.absolve,
    which is the opposite of either and turns an ZIO[R,
     Nothing, Either[E, A]] into a ZIO[R, E, A]:
     Submerges the error case of an Either into the ZIO. The inverse operation of IO.either.
     def absolve[R, E, A](v: ZIO[R, E, Either[E, A]]): ZIO[R, E, A] =
    v.flatMap(fromEither(_))
 */
   val srtOp: ZIO[Any, Nothing, Either[String, Double]] = sqrt(ZIO.succeed(2.0))

  def sqrt(io: UIO[Double]) =
      io.map(value =>
        if (value < 0.0) Left("Value must be >= 0.0")
        else Right(Math.sqrt(value))
      )
    val absoveOutput: ZIO[Any, String, Double] = ZIO.absolve(srtOp)

  /*
  TODO
      ZIO can preserve information
      -We can Express how exactly our effects can fail
      - Composed ErrorTypes Automatically inferred you can see here in readConfig def
      -
   */
  sealed trait ConfigError
  case class Config(params: String = _)

  final case class IOError(message: String) extends ConfigError

  final case class ParseError(message: String) extends ConfigError

  def readFile(path: String): ZIO[Any, IOError, String] = ???

  def parseConfig(s: String): ZIO[Any, ParseError, Config] = ???

  def readConfig(path: String): ZIO[Any, ConfigError, Config] = {
    readFile("").flatMap(_ => parseConfig(""))
  }

  /*
TODO
    Catching All Errors
 If you want to catch and recover from
 all types of errors and effectfully attempt recovery,
  you can use the catchAll method:
   */
  val z: IO[ IOError, String] =
    readFile("primary.json").catchAll(_ =>
      readFile("backup.json"))

  /*
 TODO
    Catching Some Errors
   If you want to catch and recover from only some types of exceptions and effectfully attempt recovery,
   you can use the catchSome method:
   */
  val data: ZIO[Any, IOError, String] =
    readFile("primary.data").catchSome {
      case _ : IOError =>
        readFile("backup.data")
    }

  /*
 TODO
    Fallback
  You can try one effect, or, if it fails,
  try another effect, with the orElse combinator:
   */

  val primaryOrBackupData: ZIO[Any, IOError, String] =
    readFile("primary.data").orElse(readFile("backup.data"))

  //type URIO[-R, +A] = ZIO[R, Nothing, A]
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] ={
    sequentialWakeUpRoutine().exitCode
    //zmol.exitCode
    greetingZio.exitCode
  }


}
