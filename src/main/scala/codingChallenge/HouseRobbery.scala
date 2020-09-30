package codingChallenge

object HouseRobbery extends App {




  def rob1(nums: Seq[Int]): Int = {
    // if length 0 or 1
    if (nums.length <= 1) if (nums.length == 0) 0 else nums(0)
    val dp: Array[Int] = Array.ofDim[Int](nums.length)
    // for house 0, we can only rob house 0
    dp(0) = nums(0)
    // for house 1, we can rub just house 1 or just house 0, we take the max
    dp(1) = Math.max(nums(0), nums(1))
    for (i <- 2 until nums.length) {
      // if we want to rob 3 , the money will become: dp[i-2] + nums[i]
      //        // if not rob, the money will besome: dp[i-1]
      //        // dp[i]: robbed so far
      // This is the logic which robber applies in his mind to rob or not to rob
      // Math.max(dp(i - 2) + nums(i), dp(i - 1))

      dp(i) = Math.max(dp(i - 2) + nums(i), dp(i - 1))
    }
    dp(dp.length - 1)
  }
  println(rob1(Seq(1,2,3,1)))
}
