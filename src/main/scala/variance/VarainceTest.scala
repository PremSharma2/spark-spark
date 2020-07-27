package variance

object VarainceTest extends App {
  abstract class Food { val name: String }
  abstract class Fruit extends Food
  case class Banana(name: String) extends Fruit
  case class Apple(name: String) extends Fruit
  abstract class Cereal extends Food
  case class Granola(name: String) extends Cereal
  case class Museli(name: String) extends Cereal

  trait CombineWith[T] {
    val item: T
    def combineWith(another: T): T
  }

  case class Bowl[F](contents: F)

  abstract class Animal {

    val name: String
    override def toString: String = s"Animal -$name"
  }
  case class Dog(name: String) extends Animal

  case class FoodBowl[F <: Food](contents: F) {
    override def toString: String = s"A yummy bowl of ${contents.name}s"

  }

  case class FoodBowl2[+F <: Food](contents: F) {
    override def toString: String = s"A yummy bowl of ${contents.name}s"

  }

  def serveToAFruitEater(fruitBowl: FoodBowl[Food]) = s"mmm,those ${fruitBowl.contents.name}s were very good"
  def serveToAFruitEaterCovariance(fruitBowl: FoodBowl2[Food]) = s"mmm,those ${fruitBowl.contents.name}s were very good"

  // understanding of invariance
  /*
   * type mismatch; found : com.scala.variance.VarainceTest.FoodBowl[com.scala.variance.VarainceTest.Fruit]
   * required: com.scala.variance.VarainceTest.FoodBowl[com.scala.variance.VarainceTest.Food]
   * Note: com.scala.variance.VarainceTest.Fruit <: com.scala.variance.VarainceTest.Food,
   * but class FoodBowl is invariant in type F. You may wish to define F as +F instead. (SLS 4.5)
   */
  //here it will not accept the FoodBowl(Apple("apple")) because u have not declared the relationship in terms of variance u need to rewrite the FoodBowl with +F
  val fruitBall: FoodBowl[Fruit] = FoodBowl(Apple("apple"))
  val fruitBall1: FoodBowl2[Fruit] = FoodBowl2(Apple("apple"))
  //serveToAFruitEater(fruitBall)
  //this is ok because it is implemented with covariant
  //hence now val fruitBall:FoodBowl[Fruit]=fruitBowl:FoodBowl[Food] hence relationship is derived
  serveToAFruitEaterCovariance(fruitBall1)
  val nums: List[Int] = List(1, 2, 3, 4)
  //this is ok because List is defined as Covarient in scala
  val numsAny: List[Any] = nums

  // Similarly Set is marked as Invariant in nature

  val numset: Set[Int] = Set(1, 2, 3, 4)
  //this will not compile
  //val numsAnyset:Set[Any]=numset
  /*
   * type mismatch; found : Set[Int] required: Set[Any] Note: Int <: Any, but trait Set is invariant in type A.
   * You may wish to investigate a wildcard type such as _ <: Any. (SLS 3.2.10)
   * hence Set of two diffrent types are not related to each other because Set is declared as Invarient
   */

  //----------------------------------------ContraVariance-----------------------------------------------------------------------------------

  trait Sink[T] {
    def send(item: T): String
  }

  object AppleSink extends Sink1[Apple] {
    def send(item: Apple) = s"Coring and eating ${item.name}"
  }

  object BananaSink extends Sink1[Banana] {
    def send(item: Banana) = s"Coring and eating ${item.name}"
  }

  object FruitSink extends Sink1[Fruit] {
    def send(item: Fruit) = s"Eating a healthy  ${item.name}"
  }

  object AnySink extends Sink1[Any] {
    def send(item: Any) = s"Sending  ${item.toString()}"
  }
  def sinkAnapple(sink: Sink[Apple]): String = {
    sink.send(Apple("Royal-apple"))
  }

  def sinkAnapple1(sink: Sink1[Apple]): String = {
    sink.send(Apple("Royal-apple"))
  }

  val fruitsink = FruitSink
  //tis will not compile bcz you have not defined the variance you cant pass fruitSink where applesink is required
  //sinkAnapple(fruitsink)
  /*
   * type mismatch; found : com.scala.variance.VarainceTest.FruitSink.type required:
   *  com.scala.variance.VarainceTest.Sink[com.scala.variance.VarainceTest.Apple]
   *  Note: com.scala.variance.VarainceTest.Fruit >: com.scala.variance.VarainceTest.Apple
   *  (and com.scala.variance.VarainceTest.FruitSink.type <: com.scala.variance.VarainceTest.Sink[com.scala.variance.VarainceTest.Fruit]),
   * but trait Sink is invariant in type T. You may wish to define T as -T instead. (SLS 4.5)
   */
  //Hence Modification required to fix this is -T annotation to decalre the variance
  trait Sink1[-T] {
    def send(item: T): String
  }
  //This will compile because we have declared the variance
  sinkAnapple1(fruitsink)
  sinkAnapple1(AnySink)
  //this will not compile because apple and Banana are siblings not parent cheild relationship
  // sinkAnapple1(BananaSink)

  // Scala Function Variance
  trait Description {
    val describe: String
  }
  case class Taste(describe: String) extends Description
  case class Texture(describe: String) extends Description
  def describeAnApple(fn: Apple => Description) = fn.apply(Apple.apply("RoyalApple"))
  val juicyFruit: Fruit => Taste = fruit => Taste.apply(s"This is ${fruit.name} and is nice")
  describeAnApple(juicyFruit)
  //more understanding of method input parameter and method output parameter interms of varaince
  val bumpybanana: Banana=>Texture = banana=> Texture.apply(s"This is ${banana.name} and is nice")
  def describeAnFruit(fn: Fruit => Description) = fn.apply(Apple.apply("RoyalApple"))
 // describeAnApple(bumpybanana)
  //this will not compile bcz method input is at contravarient position and method is declared as it will take Fruit ,but you have passed apple so it will take it
  // because apple is an fruit,now you rae tryin to pass Banana it will not take it will complain bcz Banana is not super type of Apple
  /*
   * type mismatch; found : com.scala.variance.VarainceTest.Banana ⇒ com.scala.variance.VarainceTest.Texture 
   * required: com.scala.variance.VarainceTest.Apple ⇒ com.scala.variance.VarainceTest.Description
   */




}