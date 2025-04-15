package numberProblems

object LargestNumber {

  /**
   TODO
    Given a list of non-negative integers, arrange them such that they form the largest number.
    The result might be huge so return a string.
    List(10 ,  2) => "210"
    List(3,  30,  5,  9,  34) => "9534330"
 **/

  def largestNumber(numbers: List[Int]): String = {

  implicit val newOrderingComparator:Ordering[Int] =
    Ordering.fromLessThan {
      (a,b) =>


      // concatenate a with b => ab
      // concatenate b with a => ba
      // comparison: a comes before b if ab >= ba and vice versa
      val aString = a.toString
      val bString = b.toString

    (aString + bString).compareTo(bString + aString) >= 0
  }



    /*
      - reflexive: a <= a
      - anti-symmetrical: if a <= b AND b <= a then a == b
        THIS IS NOT THE CASE for proper sorting
        List(1010, 10) counterexample
        for our problem IT DOES NOT MATTER
      - transitive: if a <= b AND b <= c then a <= c
     */

    val largest = numbers.sorted.mkString("")
    if (numbers.isEmpty || largest.charAt(0) == '0') "0"   //edge case: ->  List(0, 0, 0) => "000"
    else largest
  }
  def main(args: Array[String]): Unit = {
    println(largestNumber(List(10, 2))) // 210
    println(largestNumber(List(3, 30, 5, 9, 34))) // 9534330
    println(largestNumber(List(2020, 20, 1010, 10, 2, 22))) // 222202020101010
    println(largestNumber(List(1))) // 1
    println(largestNumber(List())) // 0
    println(largestNumber(List(0, 0, 0))) // 0
  }

  /*
  Example List: [3, 30, 34, 5, 9]
We need to sort this list so that the concatenated result forms the largest possible number.

Step 1: Initial List

[3, 30, 34, 5, 9]
We will now compare and swap elements based on our custom order.

Step 2: Comparing and Sorting
1st Comparison → 3 vs 30
Two possible concatenations:
"3" + "30" = "330"
"30" + "3" = "303"
Since "330" > "303", we keep 3 before 30.
List stays the same:


[3, 30, 34, 5, 9]
2nd Comparison → 34 vs 3
Two possible concatenations:
"34" + "3" = "343"
"3" + "34" = "334"
Since "343" > "334", 34 should come before 3.
List updates:

[34, 3, 30, 5, 9]
3rd Comparison → 5 vs 34(as 34 is pivot element)
Two possible concatenations:
"5" + "34" = "534"
"34" + "5" = "345"
Since "534" > "345", 5 should come before 34.
List updates:


[5, 34, 3, 30, 9]
4th Comparison → 9 vs 5
Two possible concatenations:
"9" + "5" = "95"
"5" + "9" = "59"
Since "95" > "59", 9 should come before 5.
Final sorted list:


[9, 5, 34, 3, 30]


 [3, 30, 34, 5, 9]

Let’s assume QuickSort picks 34 as the pivot.
Left:  [3, 30]
Right: [34, 5, 9]

Now, elements are compared relative to the pivot.

Step 3: Partitioning the List
We compare all numbers with 34:

Compare 3 with 34 → "343" vs "334"
Since "343" > "334", 34 stays before 3.
Compare 30 with 34 → "3430" vs "3034"
Since "3430" > "3034", 34 stays before 30.
Compare 5 with 34 → "534" vs "345"
Since "534" > "345", 5 comes before 34.
Compare 9 with 34 → "934" vs "349"
Since "934" > "349", 9 comes before 34.
After this partitioning, the list now looks like:
   */
}
