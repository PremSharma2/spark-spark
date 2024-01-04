package scalaBasics

object Expressions extends App {
  //TODO In scala every thing is an expression
  // and every expression has some value associated with it
  val x: Int = 1+2 // Expression
  //TODO : -> printing expression value
  println(x)
  val booleanExpression1: Boolean = x==1
  println(booleanExpression1)
  println(x==1)// Boolean Expression
  // false scenario boolean expression
  println(!(1==x)) // Boolean Expression  ex: !(1==x)
  //TODO : assignment and declaration of an variable in scala they both goes hand in hand
  var a:Int=3
  //Side effect in variable
  a+=3
  println(a)
  //TODO : ->  one more boolean expression  4 % 2 != 0
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
  

  //TODO
  // Loops are not advisable in scala
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


  TODO
      /
     code blocks are also expression
     Whose returned value is calculated from the last line of code block
     which is an Conditional Expression
     if(b>2) "hello" else "bye-bye"
      */

      
      val codeBlock :String ={
        val a = 5
        val b = a + 1
       if(b>2) "hello" else "bye-bye"
             
      }
  /*
  TODO
    val codeblock: String = {
    val userExists = checkUserExists(userId)
    val accountActive = checkAccountStatus(userId)
    if (userExists && accountActive) "User is active" else "User not found or inactive"
      }

 if (checkUserExists(userId) && checkAccountStatus(userId)) "User is active" else "User not found or inactive"

   */

  def financialCalculation(): Double = {
    // Code block for encapsulation
    //scope of these variables made local
    val codeBlock = {
      val principal = 10000
      val rate = 0.05
      val time = 5
      principal * rate * time // Last expression is the value of the block
    }

    codeBlock // Using the result of the code block
  }

  val interest = financialCalculation()
  println(s"Calculated interest: $interest")



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

  /*
  TODO
      Transactional Operations: Code blocks in Scala can be used to perform
      transactional operations,
      where a series of operations need to be executed as a single atomic unit.
      By using code blocks along with database transactions
      or other transaction management mechanisms,
       you can ensure that either all operations succeed or none of them take effect.
      def transferFunds(fromAccount: Account, toAccount: Account, amount: Double): Unit = {
     // Start a transaction
  TODO
     // Code block for transferring funds
       {
       fromAccount.withdraw(amount)
        toAccount.deposit(amount)
       } // The transaction is committed here if no exceptions occur, otherwise rolled back
}

   */
}
