package lambda

object TuplesandMaps extends App {

  // tuples=finite  ordered lists
  val tuple = new Tuple2(2, "Hello-Scala") // Tuple2[Int,string]
  val tuple1 = Tuple2.apply(3, "Hello")
  val tuple2 = (3, "Hello") // Tuple2 has apply method

  println(tuple._1)
  println(tuple._2)
  println(tuple.copy(_2 = "good bye java"))
  println(tuple)
  // Maps

  val aMap: Map[String, Int] = Map()
  //companion object apply method which acts as a factory for map
  //def apply[A,B](entries :(A,B)*):Map[A,B]
  val phonebook = Map.apply(("Jim", 3333), ("Rob", 6666), ("JIM", 999)).withDefaultValue("No value found")
  println(phonebook.contains("Jim"))
  //Optionally returns the value associated with a key.
  println(phonebook.get("Mary"))
  /*
   * Retrieves the value which is associated with the given key. 
   * This method invokes the default method of the map 
   * if there is no mapping from the given key to a value. 
   * Unless overridden, the default method throws a NoSuchElementException
   */
  println(phonebook.apply("Mary"))
  val newPairing = "Mary" -> 678
  //new pairing added into the map
  val newPhonebook = phonebook + newPairing
  println(newPhonebook)
//Builds a new collection by applying a function to all elements of this immutable map
  println(phonebook.map(pair => pair._1.toLowerCase() -> pair._2))
//Filters this map by retaining only keys satisfying a predicate
  println(phonebook.filterKeys(x => x.startsWith("J")))
  //Transforms this map by applying a function to every retrieved value
  println(phonebook.mapValues(phonenumber => "03256--" + phonenumber))
  //conversions
  val tupledList= phonebook.toList
  println(tupledList)
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  //Partitions this list into a map of lists according to some discriminator function
  println(names.groupBy(name => name.charAt(0)))
  // o/p of this groupBy(name => name.charAt(0)) is Key Value pair where key is o/p of discrimnating function
  // and value is the List

  //Map(J -> List(James, Jim), A -> List(Angela), M -> List(Mary), B -> List(Bob), D -> List(Daniel))
}