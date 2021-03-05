package stringProblems

import scala.annotation.tailrec

object StringProblem {
  /*
   countCharacterTailRec("Scala, []) =
   countCharacterTailRec("cala, ["s"->1,])
    countCharacterTailRec("ala, ["s"->1,"c"->1])
   countCharacterTailRec("la, ["s"->1,"c"->1,"a"->1])
   countCharacterTailRec("a, ["s"->1,"c"->1,"a"->1,"l"->1])
    countCharacterTailRec(" ", ["s"->1,"c"->1,"a"->2,"l"->1])
   */
  def countCharacters(string: String): Map[Char, Int] = {
    @tailrec
    def countCharacterTailRec(remaining: String, accumulator: Map[Char, Int]): Map[Char, Int] = {
      if (remaining.isEmpty) accumulator
      else if (accumulator.contains(remaining.head)) {
        val currentChar = remaining.head
        val currentOccurences = accumulator(currentChar)
        countCharacterTailRec(remaining.tail, accumulator + (currentChar -> (currentOccurences + 1)))
      } else countCharacterTailRec(remaining.tail, accumulator + (remaining.head -> 1))
    }

    countCharacterTailRec(string, Map())
  }

  def checkAnagrams(sa: String, sb: String): Boolean = countCharacters(sa) == countCharacters(sb)
// both are same
  def checkAnagrams2(sa: String, sb: String): Boolean = sa.sorted == sb.sorted


  def main(args: Array[String]): Unit = {
    println(countCharacters("Scala"))
    println(checkAnagrams("scala", "haskel"))
    println(checkAnagrams("desserts", "stressed"))
  }
}
