package codingChallenge

object RemoveDuplicatesFromSeq  extends App {

//[(1,0),(1,1),(2,2),(2,3)]
/*
(1 to 5).iterator.sliding(3).toList
res18: List[Sequence[Int]] = List(List(1, 2, 3), List(2, 3, 4), List(3, 4, 5))
[[(1,0),(1,1)],[(1,1),(2,2)],[(2,2),(2,3)]]
 */
  def removeDuplicates[T](l: List[T]): List[T] = l.iterator.zipWithIndex
    .sliding(2,1)
    .collect({
      case Seq((onlyElement, _)) =>
        Seq(onlyElement)
      case Seq((element1, 0), (element2, _)) =>
        if (element1 == element2) Seq(element1) else Seq(element1, element2)
      case Seq((element1, _), (element2, _)) if element1 != element2 =>
        Seq(element2)
    })
    .flatten
    .toList



  println(removeDuplicates(List.empty[Int]) == List.empty[Int])
  println(removeDuplicates(List(1)) == List(1))
  println(removeDuplicates(List(1, 1)) == List(1))
  println(removeDuplicates(List(1, 2)) == List(1, 2))
  println(removeDuplicates(List(1, 1, 2)) == List(1, 2))
  println(removeDuplicates(List(1, 1, 1, 2)) == List(1, 2))
  println(removeDuplicates(List(1, 1, 2, 3, 3)) == List(1, 2, 3))

}
