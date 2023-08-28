package collections.basic.operationAndTransformation
import scala.collection.immutable

object TuplesandMaps extends App {

  // tuples=finite  ordered lists
  val tuple = new Tuple2(2, "Hello-Scala") // Tuple2[Int,string]
  val tuple1 = Tuple2.apply(3, "Hello")
  val tuple2 = (3, "Hello") // Tuple2 has apply method

  println(tuple._1)
  println(tuple._2)
  //update the value
  println(tuple.copy(_2 = "good bye java"))
  println(tuple)

  /**
   * TODO
   *   It’s also possible to omit some elements
   *   when we declare the variables using an underscore in the desired place:

   */
  val (_, myAge) = tuple
  val arrayOfTuples = List((1, "Two"), (3, "Four"))

  val tuplemap: immutable.Seq[String] =arrayOfTuples map {
    case (e1: Int, e2: String) => e1.toString + e2
  }

  /**
   * TODO
   *   def tupled[a1, a2, b](f: (a1, a2) => b): Tuple2[a1, a2] => b = {
       case Tuple2(x1, x2) => f(x1, x2)
  }
   */
  import Function.tupled
  arrayOfTuples map tupled { (e1, e2) => e1.toString + e2 }

  /**
   * TODO
   *  Tuples can be particularly useful when
   *  we need to return multiple values from a function
   *  or pass multiple values to a function.
   *  This may be preferable instead of using a case class,
   *  particularly if we need to return unrelated values.
   *
   */

  val numbers = List(1, 2, 3, 4, 5)
  val (sum, count) = numbers.foldLeft(Tuple2(0, 0)) {
    case ((runningSum, runningCount), number) => (runningSum + number, runningCount + 1)
  }
  val average = sum.toDouble / count


  def partition[A](xs: List[A])(predicate: A => Boolean): (List[A], List[A]) = {
    xs.foldRight(Tuple2(List.empty[A], List.empty[A])) {
      case (a, (lefts, rights)) =>
        if (predicate(a)) (a :: lefts, rights) else (lefts, a :: rights)
    }
  }
  val (evens, odds) = partition(List(1, 3, 4, 5, 2))(_ % 2 == 0)


  /**
   * TODO
   *  Likewise, we can also use a tuple for passing multiple parameters
   *  to a one-parameter function. We even have a special tupled() method
   *  that converts a function with more than one parameter,
   *  to a function which accepts a tuple as the only argument:

   */

  val data = Map(
    "Joe" -> 34,
    "Mike" -> 16,
    "Kelly" -> 21
  )

  case class User(name: String, isAdult: Boolean)

  val createUser: (String, Int) => User = (name, age) => User(name, age >= 18)
  val users: immutable.Iterable[User] = data.map(createUser.tupled)
  // Maps

  val aMap: Map[String, Int] = Map()
  //companion object apply method which acts as a factory for map
  //def apply[A,B](entries :(A,B)*):Map[A,B]
  val phonebook: Map[String, Int] = Map.apply[String, Int](("Jim", 3333), ("Rob", 6666), ("JIM", 999)).withDefaultValue(121)
  println(phonebook.contains("Jim"))
  //Optionally returns the value associated with a key.
  val optionalValue: Option[Int] = phonebook.get("Mary")
  val optionalValue1: Int = phonebook.getOrElse("Mary", 12345)
  println(phonebook.get("Mary"))

  /**
   * TODO
   *  Retrieves the value which is associated with the given key.
   *  This method invokes the default method of the map
   *  if there is no mapping from the given key to a value.
   *  Unless overridden, the default method throws a NoSuchElementException
   */

  println(phonebook.apply("Mary"))
  val newPairing: (String, Int) = "Mary" -> 678
  //new pairing added into the map
  val newPhonebook = phonebook + newPairing

  val newPhonebook1 = phonebook.updated(newPairing._1,newPairing._2)

  println(newPhonebook)
  //TODO : -> Builds a new collection or Map by applying a function to all elements of this immutable map
  println(phonebook.map(pair => pair._1.toLowerCase() -> pair._2))
  //TODO : -> Filters this map by retaining only key-value  satisfying a predicate
  println(phonebook.filterKeys(x => x.startsWith("J")))
  //TODO : Transforms this map by applying a function to every retrieved value from Key-Value
  println(phonebook.mapValues(phonenumber => "03256--" + phonenumber))
  //conversions
  val tupledList: immutable.Seq[(String, Int)] = phonebook.toList
  println(tupledList)
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  //TODO fetching the values of Map
  val trades= Map[String,List[String]]("county-1" ->List("Bob", "James", "Angela", ""))
   val r: Iterable[String] =trades.values.flatten

  /**
   * TODO
   *  When we want to update an map in any algorithm then we should use foldLeft
   *  for example traversing any monad we want to update the the map or maintain the key value pair
   *  better to use foldLeft
   */
  val list= List("county-1", "county-2", "county-3")
  list.foldLeft(Map.empty[String,List[String]]){
    case (acc,x) => acc.updated(x,list)
  }

  //TODO Partitions this list into a map of lists according to some discriminator function
  println(names.groupBy(name => name.charAt(0)))
  // o/p of this groupBy(name => name.charAt(0)) is Key Value pair where key is o/p of discrimnating function
  // and value is the List

  //Map(J -> List(James, Jim), A -> List(Angela), M -> List(Mary), B -> List(Bob), D -> List(Daniel))
}