package stringProblems

import scala.annotation.tailrec

object ParanthesisProblem {
  val OpenToClose: Map[Char, Char] = Map('{' -> '}', '[' -> ']', '(' -> ')')
  val CloseToOpen: Map[Char, Char] = OpenToClose.map(_.swap)

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

  /*
 TODO
  Compiler will not allow this Logic because it will prompt that return Type is AnyVal
   where as Required is Boolean
   Soln: is Put everything in else Condition and if there are many if and else conditions
  in else block then use pattern match otherwise Exception will come
   java.lang.UnsupportedOperationException: tail of empty list

   TODO
    def parenthesesAreBalancedModified(s: String): Boolean = {
      if (s.isEmpty) true
      else {
        @tailrec
        def go(position: Int, accumulator: List[Char]): Boolean = {
          if (position == s.length) accumulator.isEmpty
          else if(OpenToClose.contains(s.head)) go(position+1 ,s.head :: accumulator)
          else if(CloseToOpen.contains(s.head)) go(position+1 , accumulator.tail)
          else false
        }
        go(position = 0, accumulator = List.empty)
      }
    }

    //TODO or
    TODO
     def parenthesesAreBalancedModified(s: String): Boolean = {
      if (s.isEmpty) true
      else {
        @tailrec
        def go(position: Int, accumulator: List[Char]): Boolean = {
          if (position == s.length) accumulator.isEmpty
          else {
          if (if(OpenToClose.contains(s.head)) go(position+1 ,s.head :: accumulator))
          else if(CloseToOpen.contains(s.head)) go(position+1 , accumulator.tail)
          else false
          }

        }
        go(position = 0, accumulator = List.empty)
      }
    }

   */

  def parenthesesAreBalanced(s: String): Boolean = {
    if (s.isEmpty) true
    else {
      @tailrec
      def go(index: Int, accumulator: List[Char]): Boolean = {
        if (index == s.length) accumulator.isEmpty
        else {
          val char = s(index)
          val isOpening = OpenToClose.contains(char)
          val isClosing = CloseToOpen.contains(char)
          if (isOpening) go(index + 1, char :: accumulator)
          else if (isClosing) {
            accumulator match {
              case _ :: tail =>   go(index + 1, tail)
              case _ => false
            }
          } else false
        }
      }

      go(index = 0, accumulator = Nil)
    }
  }


  def parenthesesAreBalancedModified(s: String): Boolean = {
      @tailrec
      def go(index: Int, accumulator: List[Char]): Boolean = {
         if (index == s.length) accumulator.isEmpty
         else if(accumulator.tail.isEmpty) false
        else if  (OpenToClose.contains(s(index))) go(index + 1, s.head :: accumulator)
        else if (CloseToOpen.contains(s(index)))  go(index + 1, accumulator.tail)
        else false

      }
    if (s.isEmpty) return true
    go(index = 0, accumulator = Nil)
    }


  def main(args: Array[String]): Unit = {
    println(parenthesesAreBalancedModified("(())"))
    println(parenthesesAreBalanced("(())"))
    println(parenthesesAreBalanced("())"))
    println(parenthesesAreBalanced("()"))
    println(parenthesesAreBalanced("[()]"))
    println(parenthesesAreBalanced("{[()]}"))
    println(parenthesesAreBalanced("([{{[(())]}}])"))
    println(parenthesesAreBalanced("{{[]()}}}}"))
    //println(parenthesesAreBalancedModified("{{[]()}}}}"))
    println(parenthesesAreBalanced("{{[](A}}}}"))
    println(parenthesesAreBalanced("{[(])}"))
  }
}
