package numberProblems

import tailRecursiveList.ListProblems.{RList, RNil}

import scala.annotation.tailrec



object NumberProblem {
  /**
   * this method test if given number is prime or not
   *
   * @param n :Int
   * @return Boolean
   */
  /*
 TODO
      square_root : a number which produces a specified quantity when multiplied by itself:
      7 is square root of 49
      Explanation: isPrime(11)
      = isPrimeTailRec(2)
      now inside isPrimeTailRec we will check  if(currentDivisor > Math.sqrt(Math.abs(n)))
      here 2 > 3.xx which is false hence it will go to else if (n % currentDivisor !=0)
      11 % 2 and it is false , now it will go to else
      isPrimeTailRec(currentDivisor + 1 =3)

 TODO
      now inside isPrimeTailRec we will check  if(currentDivisor > Math.sqrt(Math.abs(n)))
      here 3 > 3.xx which is false hence it will go to else if (n % currentDivisor !=0)
      11 % 2 and it is false , now it will go to else
      isPrimeTailRec(currentDivisor + 1 =4)

 TODO
       now inside isPrimeTailRec we will check  if(currentDivisor > Math.sqrt(Math.abs(n)))
       here 4 > 3.xx which is true hence we will return true


TODO   now lets talk about a number which is not prime
        isPrime(15) =
        isPrimeTailRec(2)
      now inside isPrimeTailRec we will check  if(currentDivisor > Math.sqrt(Math.abs(n)))
      here 2 > 3.xx which is false hence it will go to else if (n % currentDivisor !=0)
      11 % 2 and it is false , now it will go to else
      isPrimeTailRec(currentDivisor + 1 =3)
      now inside isPrimeTailRec we will check  if(3 > 3.xx) false
      15 % 3 and it is true , now it will not go to else
      return false from here
      complexity O(Sqrt(n))

     TODO
      Explanation of this branch  else n % currentDivisor != 0 && isPrimeTailRec(currentDivisor + 1)
      The && (logical AND) operator is used to chain the non-divisibility condition with
      the recursive function call.
      If the left-hand side (n % currentDivisor != 0) is true
      (i.e., n is not divisible by currentDivisor),
      then the right-hand side is evaluated (isPrimeTailRec(currentDivisor + 1)),
      moving on to the next divisor.
      If the left-hand side is false,
      then the function will immediately return false without evaluating the right-hand side.
   */
  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeTailRec(currentDivisor: Int): Boolean = {
      if (currentDivisor > Math.sqrt(Math.abs(n))) true
      else n % currentDivisor != 0 && isPrimeTailRec(currentDivisor + 1)
      //else if (n % currentDivisor == 0) false
      //else isPrimeTailRec(currentDivisor + 1)
    }

    if (n == 1 || n == 0 || n == -1) false
    else isPrimeTailRec(2)
  }

  /**
   * this method decompose the the number into List of its
   * constituent prime divisor
   *
   * @param n :Int
   * @return : RList[Int]
   */

  /*
   todo  decompose(11)
         decomposeTailRec(11,2,[])
          if(currentDivisor > Math.sqrt(n)) false
          bcz 2< 3.8
          else if(11 % 2 ==0) false
          it goes to else
          decomposeTailRec(11,3,[])

  TODO
            decomposeTailRec(11,3,[])
            if(currentDivisor > Math.sqrt(n)) false
              bcz 3< 3.8
                else if(11 % 3 ==0) false
                it goes to else
          decomposeTailRec(11,4,[])

    TODO
           decomposeTailRec(11,4,[])
            if(currentDivisor > Math.sqrt(n)) it is true now
            so we will return 11 :: [] = [11]

    TODO
          decompose(15) = decomposeTailRec(15,2,[])
          decomposeTailRec(15,2,[])
          if(currentDivisor > Math.sqrt(n)) false
          bcz 2< 3.87
          else if(15 % 2 ==0) false
          it goes to else
          decomposeTailRec(15,3,[])

TODO       decomposeTailRec(15,3,[])
          if(currentDivisor > Math.sqrt(n)) false
          bcz 3< 3.87
          else if(15 % 3 ==0) true now
          decomposeTailRec(5,3,[3])


TODO          decomposeTailRec(5,3,[3])
              3> 2.2 hence it is true
              else if(5 % 3 ==0) it is also true
              remaining :: accumulator = 5::[3] = [5,3]


TODO             Lets discuss  the case when decompose(16)
                   decomposeTailRec(16,2,[])
                  if(currentDivisor > Math.sqrt(n)) false
                  bcz 2< 3.87
                  else if(16 % 2 ==0) true
                   decomposeTailRec(8,2,[2])

TODO                decomposeTailRec(8,2,[2])
                  if(currentDivisor > Math.sqrt(n)) false
                  bcz 2< 2.82
                  else if(8 % 2 ==0) true
                  decomposeTailRec(4,2,[2,2])



TODO               decomposeTailRec(4,2,[2,2])
                  if(currentDivisor > Math.sqrt(n)) false
                  bcz 2< 2
                  else if(4 % 2 ==0) true
                  decomposeTailRec(2,2,[2,2,2])




TODO               decomposeTailRec(2,2,[2,2])
                  if(currentDivisor > Math.sqrt(n)) false
                  bcz 2< 3.87
                  else if(4 % 2 ==0) true
                  decomposeTailRec(1,2,[2,2,2,2])

                  Complexity is 0(sqrt(N))
                  this can go as Low as 0(log(n))
                  when we have identical divisors
                  i.e. this case
                   else if (remaining % currentDivisor == 0)
                   if we have decomposeTailRec(16,2,[])
   */
  def decompose(n: Int): RList[Int] = {
    //as we are putting the current divisor into list so we need to make it variable
    assert(n > 0)

    @tailrec
    def decomposeTailRec(remaining: Int, currentDivisor: Int, accumulator: RList[Int]): RList[Int] = {

      if (currentDivisor > Math.sqrt(remaining)) remaining :: accumulator
      else if (remaining % currentDivisor == 0) {
        decomposeTailRec(remaining / currentDivisor, currentDivisor, currentDivisor :: accumulator)
      }
      else decomposeTailRec(remaining, currentDivisor + 1, accumulator)
    }

    decomposeTailRec(n, 2, RNil)
  }

  def main(args: Array[String]): Unit = {
    println(isPrime(15))
    println(isPrime(53611))
    println(isPrime(9661))
    println(isPrime(7))
    println(decompose(15))
    println(decompose(16))
    println(decompose(2003))
    println(decompose(53611))
  }

}
