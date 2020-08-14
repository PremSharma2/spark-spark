package lazyevaluation

import lazyevaluation.CustomStreamsUsingLazyEvaluation.{MyStream, Node}

object FibonacciNumberStream extends App {
/*
Fibonacci Series
0, 1, 1, 2, 3, 5, 8, 13, 21, 34, ...
The next number is found by adding up the two numbers before it:

the 2 is found by adding the two numbers before it (1+1),
the 3 is found by adding the two numbers before it (1+2),
the 5 is (2+3),
and so on!
Explanation of Fibonacci Series stream
Here result is being lazily evaluated of first number i.e on demand
or callby need
[firstnumber, lazyevalutedResult]
Hence fibonacci resulted number of First number And Second number will be a stream
hence it will look like this in terms of stream implemetation is :
[ firstNumber, anotherLazyStream] like Node(head,lazyTail)
and this anotherLazyStream will be a recursive call of  fibonacci function
so it will look like this
[first , fibonacci(second,first + second)] if we simulate this to Node
it will be
1, 1, 2, 3, 5, 8, 13, 21, 34
Node(firstNumber, fibonacci(second,first + second))
hence fibonacci(second,first + second) this Expression which represents tail will be lazily evaluated
That means Expression of calculating the next number fibonacci series is lazily evaluated
 */
  //1, 1, 2, 3, 5, 8, 13, 21, 34
  def fibonacci(firstNumber: Int , secondNumber: Int): MyStream[Int] =
    new Node[Int](firstNumber, fibonacci(secondNumber,firstNumber + secondNumber))
// converting infinite stream to finite stream using take method
  val fibonacciFiniteStream=fibonacci(1,1).take(10)
  val fibonacciSeq=fibonacciFiniteStream.toList()
  println(fibonacciSeq)
/*
  Lets explain this
  [2,3,4,5,6,7,8,9,10,11,12 ....]
  firstly at every iteration we want current head should be prime number i.e
  we want to filter out these that too in lazy fashion
  [2,3,5,7,9,11...] these are the prime numbers we will get this
  so in terms of Stream implementation
  new Node(head , eratosthenes applied to the output of this sub Expression (numbers filtered by n%2!=0)
  LazyEvaluatedExpression= eratosthenes(applied to (numbers filtered by n%2!=0)
  Hence new filtered stream will be constructed on need basis
  i.e the input to eratosthenes should be prime
  [2,3,4,5,6,7,8,9,10,11,12 ....]
  [2,3,5,7,9....]
  [2 ,eratosthenes ( primeNumberstream=[2,3,5,7,9....])
  [ 2,3 ,eratosthenes ( primeNumberstream=[5,7,9....] should be filtered by n%3!=0)]
  [2,3,5 ,eratosthenes ( primeNumberstream=[7,9....] should be filtered by n%5!=0)]
      .
      .
      .
      and so on
      Here as you can see here that we are changing the filter creteria of filter at every
      iteration
      i.e we want each number should be % stream.head!=0
        eachNumber% stream.head!=0
      i.e the filter creteria depends on the current stream head like we gave examples above
      in terms of Lazy evaluation
      LazyTailExpression=eratosthenes(numberStream.tail.filter(n => n% numberStream.head !=0)))
      This LazyExpression will be evaluated lazily i.e callByneed


 */
  def eratosthenes(numberStream:MyStream[Int]): MyStream[Int]=
    if(numberStream.isEmpty) numberStream
    else new Node[Int](numberStream.head,
                       eratosthenes(numberStream.tail.
                         filter(n => n% numberStream.head !=0)))


  val infiniteStreamOfNaturalsNumbersStartingFrom2: MyStream[Int] = MyStream.from(2)(_ + 1)
    val infiniteEratosthenesProcessedStream: MyStream[Int] = eratosthenes(infiniteStreamOfNaturalsNumbersStartingFrom2)
    val finiteEratosthenesProcessedStream= infiniteEratosthenesProcessedStream.take(10)
    val eratosthenes_Finite_ProcessedStream_Converted_toList=  finiteEratosthenesProcessedStream.toList()
   println(eratosthenes_Finite_ProcessedStream_Converted_toList)

}
