package codingChallenge

import scala.collection.immutable.Stack
import scala.collection.immutable.Map
import scala.collection.mutable
object ValidParanthesis extends App {

  //remove if not needed
  import scala.collection.JavaConversions._

  class Solution {

    // Hash table that takes care of the mappings.


    val mappings = scala.collection.mutable.Map(')'-> '(',  '}' -> '{' , ']'->'[' )


    def isValid(s: String): Boolean = {
      // Initialize a stack to be used in the algorithm.
      val stack: Stack[Character] =  Stack[Character]()
      for (i <- 0 until s.length) {
        val c: Char = s.charAt(i)
        // If the current character is a closing bracket.
        if (this.mappings.containsKey(c)) {
          // Get the top element of the stack. If the stack is empty, set a dummy value of '#'
          val topElement: Char = if (stack.isEmpty) {
             '#'
          }
          else stack.top
          // If the mapping for this bracket doesn't match the stack's top element, return false.
          if (topElement != this.mappings.get(c)) {
            false
          }
        } else {
          // If it was an opening bracket, push to the stack.
          stack.push(c)
        }
      }
      // If the stack still contains elements, then it is an invalid expression.
      stack.isEmpty
    }

  }


}
