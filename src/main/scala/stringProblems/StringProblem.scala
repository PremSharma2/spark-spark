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

  /*
  TODO
      ransomNote(
       "I have your daughter. I want 1000000 dollars, or you'll never see her again.",
       "I bought this really nice doll for my daughter. It was 20 dollars on Amazon.
        She's never been happier.
        I often have discounts from my network,
        so if you want to buy some really cool stuff for your kids,
         I can send you an invite
        if you sign up to my newsletter.
        It's read by 100000 people,
        and you'll never need to search for online discounts again."
      )
    */
  def ransomeNote(note: String, magzine: String): Boolean = {
    def buildMap1(string: String): Map[Char, Int] = {
      string.groupBy(c => c).mapValues(_.length).toMap
    }

    /*
    TODO
     It's a tuple
     (x, y) is a tuple which we deconstruct in the case there
     */
    def buildMap(str: String): Map[Char, Int] = {
      str.foldLeft(Map[Char, Int]()) {
        case (map, char) => map + (char -> (map.getOrElse(char, 0) + 1))
      }
    }

    val noteMap = buildMap(note)
    val magzineMap = buildMap(magzine)
    noteMap.keySet.
      forall(char => noteMap.getOrElse(char, 0) <= magzineMap.getOrElse(char, 0))

  }


  // "aaabc" -> "abaca", "acaba"
  // "aaa" -> ""
  // rearrange chars so that no two adjacent chars are identical
  /*
 TODO
      char count Map={a-3, b-1, c-1}
      ot({a-3, b-1, c-1}, \0 "") =
      ot({a-2, b-1, c-1}, 'a', "a") =
      ot({a-2, c-1}, 'b', "ab") =
      ot({a-1, c-1}, 'a', "aba") =
      ot({a-1}, 'c', "abac") =
      ot({}, 'a', "abaca") =
      when map is empty return accum
      "abaca"
      Complexity: O(N^2) time
      Better complexity: use TreeMap: O(N*log(N))
      because it takes O(n) to populate the Map[Char,Int] and on top of
      map what we are doing here actually we are doing maxby that means we are traversing the map
      so its O(n^2)
      use tree map for better complexity
      The elements are sorted in natural order (ascending).
      The TreeMap should be used when we require key-value pair in sorted (ascending) order.
     */
  def reorganizeString(string: String) = {
    //'\u0000' is unicode char for null character
    @tailrec
    def organizeTailRec(charCount: Map[Char, Int], forbiddenChar: Char = '\u0000', accumlator: String): String = {
      if (charCount.isEmpty) accumlator
      else {
        val newChar: Char = charCount.filter(_._1 != forbiddenChar).maxBy(_._2)._1
        val newCharCount: Map[Char, Int] =
          if (charCount(newChar) == 1) charCount - newChar
          else charCount + (newChar -> (charCount(newChar) - 1))
        organizeTailRec(newCharCount, newChar, accumlator + newChar)
      }
    }

    def buildMap(string: String): Map[Char, Int] = {
      string.groupBy(c => c).mapValues(_.length)
    }

    val charCount: Map[Char, Int] = buildMap(string)
    if (charCount.values.exists(_ > (string.length + 1) / 2)) ""
    else organizeTailRec(charCount, '\u0000', "")
  }


  def main(args: Array[String]): Unit = {
    println(countCharacters("Scala"))
    println(checkAnagrams("scala", "haskel"))
    println(checkAnagrams("desserts", "stressed"))
    val result = ransomeNote(
      "I have your daughter. I want 1000000 dollars, or you'll never see her again.",
      "I bought this really nice doll for my daughter. It was 20 dollars on Amazon." +
        "She's never been happier." +
        "I often have discounts from my network," +
        "so if you want to buy some really cool stuff for your kids," +
        "I can send you an invite" +
        "if you sign up to my newsletter." +
        "It's read by 100000 people," +
        "and you'll never need to search for online discounts again."
    )
    println(result)
    println(reorganizeString("aaaaa").isEmpty)
    println(reorganizeString("abbcb"))
  }
}
