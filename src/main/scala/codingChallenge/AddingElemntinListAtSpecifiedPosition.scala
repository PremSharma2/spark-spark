package codingChallenge

object  AddingElemntinListAtSpecifiedPosition extends App {

  def insert[T](list: List[T], i: Int, value: T) = {
    list.take(i) ++ List(value) ++ list.drop(i)
  }

}
