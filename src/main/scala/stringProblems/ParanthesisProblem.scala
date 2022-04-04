package stringProblems

object ParanthesisProblem {
  /*
   "()" => true
   "()()"=> true
   "(())"=> true
   ")("=> false


TODO
    Explanation
     validParanthesTailRec("(())",0) =
     vpt("())",0)
     vpt("())",1)
     vpt("))",2)
     vpt(")",1)
     vpt(" ",0)
   now matches the condition if (remaining.isEmpty) openParanths == 0
   returns true

TODO  vpt("())",0)
      vpt("))",1)
       vpt(")",0)
  matches this condition      else if (openParanths == 0 && remaining.head == ')') false
   */
  // complexity is O(n)
  def hasValidParanthesis(str: String): Boolean = {
    def validParanthesTailRec(remaining: String, openParanths: Int): Boolean = {
      if (remaining.isEmpty) openParanths == 0
      else if (openParanths == 0 && remaining.head == ')') false
      else if (remaining.head == '(') validParanthesTailRec(remaining.tail, openParanths + 1)
      else validParanthesTailRec(remaining.tail, openParanths - 1)
    }

    validParanthesTailRec(str, 0)
  }
  val OpenToClose: Map[Char, Char] = Map('{' -> '}', '[' -> ']', '(' -> ')')

  val CloseToOpen: Map[Char, Char] = OpenToClose.map(_.swap)

  def parenthesesAreBalanced(s: String): Boolean = {
    if (s.isEmpty) true
    else {
      @scala.annotation.tailrec
      def go(position: Int, stack: List[Char]): Boolean = {
        if (position == s.length) stack.isEmpty
        else {
          val char = s(position)
          val isOpening = OpenToClose.contains(char)
          val isClosing = CloseToOpen.contains(char)
          if (isOpening) go(position + 1, char :: stack)
          else if (isClosing) {
            val expectedCharForMatching = CloseToOpen(char)
            stack match {
              case _ :: rest =>
                go(position + 1, rest)
              case _ =>
                false
            }
          } else false
        }
      }
      go(position = 0, stack = List.empty)
    }
  }
  def main(args: Array[String]): Unit = {
   println(hasValidParanthesis("(())"))
    println(hasValidParanthesis("())"))
    println(parenthesesAreBalanced("()"))
    println(parenthesesAreBalanced("[()]"))
    println(parenthesesAreBalanced("{[()]}"))
    println(parenthesesAreBalanced("([{{[(())]}}])"))
    println(parenthesesAreBalanced("{{[]()}}}}"))
    println(parenthesesAreBalanced("{{[](A}}}}"))
    println(parenthesesAreBalanced("{[(])}"))
  }
}
