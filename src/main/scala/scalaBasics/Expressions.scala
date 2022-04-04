package scalaBasics

object Expressions extends App {
  //In scala every thing is an expression and every expression has some value associated with it
  val x: Int = 1+2 // Expression
  //printing expression value
  println(x)
  val booleanExpression1: Boolean = x==1
  println(booleanExpression1)
  println(x==1)// Boolean Expression
  // false scenario boolean expression
  println(!(1==x)) // Boolean Expression
  //assignment and declaration of an variable in scala they both goes hand in hand
  var a:Int=3
  //Side effect in variable
  a+=3
  println(a)
  // one more boolean expression
  val booleanExpression: Boolean = 4 % 2 != 0
  println(booleanExpression)
  println(4 % 2 != 0)// printing boolean expression o/p value
  // Instructions vs Expression in scala
  

  val aCondition: Boolean =true
  //TODO Example of Conditional expression returns a value on condition basis
  // use of if else as ternary operator as expression i.e conditional expression
  //TODO if (aCondition) 5 else 3 this is conditional expression
  val conditionalExpressionOutput: Int = if (aCondition) 5 else 3
  println(conditionalExpressionOutput)
  

  /*var i=0
  while(i<10){
    println(i)
    i+=1 // side effects not advisable in scala
  }*/
  //Never do this in scala

      var aVariable: Int =1
  // This expression doest not return any thing
  //! As discussed Every thing in scala is Expression
  val aWeirdValue: Unit =  aVariable=3  // Unit is equivalent to void in java
  println(aWeirdValue)
  
  
  /*var j=0
  var awhile=while(j<10){
              println(j)
              j+=1
  }
      print(awhile)
      */
      // Side Effects are while loop,reassigning a variable,
      
      
      //code blocks 
     /*
     code blocks are also expression
     Whose returned value is calculated from the last line of code block

      */
      
      val codeBlock :String ={
        var a=5
        var b=a+1
       if(b>2) "hello" else "bye-bye"
             
      }
  
  println(codeBlock)
  
      val someOtherValueCodeBlock: Boolean ={
        2<3
      }
      println(someOtherValueCodeBlock)
      
      val someValueCodeBlock: Int ={
        if (someOtherValueCodeBlock) 239 else 142
        23
      }
      print(someValueCodeBlock)
}
