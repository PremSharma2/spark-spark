package codingChallenge

object RemoveDuplicatesFromSeq  extends App {

  def removeDuplicates(nums: Array[Int]): Int = {
    if (nums.length == 0) 0
    var i: Int = 0
    for (j <- 1 until nums.length if nums(j) != nums(i)) {
      { i += 1; i - 1 }
      nums(i) = nums(j)
    }
    i + 1
  }

}
