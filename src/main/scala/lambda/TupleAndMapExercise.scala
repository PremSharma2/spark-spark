package lambda

object TupleAndMapExercise extends App {
  
  
  def addtoNetwork(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
     // new entry in the networkMap is represented by (person -> Set())
    network + (person -> Set())
  }

  def friend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network.apply(a)
    val friendsB = network.apply(b)
    network + (a -> (friendsA + b)) + (b -> (friendsB + a))
  }

  def unFriend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network(a)
    val friendsB = network(b)
    network + (a -> (friendsA - b)) + (b -> (friendsB - a))
  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
    // to remove this person from the network we need to unfriend this person from the all network  entries of  the persons 
    def removeAux(friends: Set[String], networkAccm: Map[String, Set[String]]): Map[String, Set[String]] = {
      if (friends.isEmpty) networkAccm
      //we need to unfriend the person to be deleted with the listoffriends head and
      //call removeAux recursively to unfriend all the friends
        //var networkaccum=unFriend(networkAccm, person, friends.head)
      else removeAux(friends.tail, unFriend(networkAccm, person, friends.head))

    }
    val unfriended = removeAux(network.apply(person), network)
    unfriended - person
  }

  val emptynetwork: Map[String, Set[String]] = Map()
  val network = addtoNetwork(addtoNetwork(emptynetwork, "BOB"), "Mary")
  println(network)
  println(friend(network, "BOB", "Mary"))
  println(unFriend(friend(network, "BOB", "Mary"), "BOB", "Mary"))
  println(remove(friend(network, "BOB", "Mary"), "BOB"))
  /*
   * this method is used to find the total number of friends of a person in a network
   * 
   */
  def nFriends(network: Map[String, Set[String]], person: String): Int = {
    if (!network.contains(person)) 0
    else {
      network(person).size
    }
  }
  println(nFriends(friend(network, "BOB", "Mary"), "BOB"))

  def mostFiends(network: Map[String, Set[String]]): String = {
    network.maxBy(pair => pair._2.size)._1
  }
  println(mostFiends(network))
  
  def nPeopleWithNoFriends(network: Map[String, Set[String]]): Int = {
    network.filterKeys(key => network(key).isEmpty).size
  }
  def socialConnection(network: Map[String, Set[String]], a: String, b: String): Boolean = {
    def bfsSearch(target: String, consideredPeople: Set[String], discoveredPeople: Set[String]): Boolean = {
      if (discoveredPeople.isEmpty) false
      else {
        val person = discoveredPeople.head
        if (target == person) true
        else if (consideredPeople.contains(person)) bfsSearch(target, consideredPeople, discoveredPeople.tail)
        else bfsSearch(target, consideredPeople + person, discoveredPeople.tail ++ network.apply(person))

      }
    }
    bfsSearch(b, Set(), network.apply(a) + a)
  }

  
}