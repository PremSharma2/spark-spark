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

  def main(args: Array[String]): Unit = {
   println(hasValidParanthesis("(())"))
    println(hasValidParanthesis("())"))

  }
}
