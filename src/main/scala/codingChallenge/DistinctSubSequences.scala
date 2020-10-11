package codingChallenge

object DistinctSubSequences  extends App {


  private var memo: Map[(Integer, Integer), Integer] = _

  private def recurse(s: String, t: String, i: Int, j: Int): Int = {
    val M: Int = s.length
    val N: Int = t.length
    // Base case
    if (i == M || j == N || M - i < N - j) {
      if (j == t.length) 1 else 0
    }
    val key = Tuple2[Integer, Integer](i, j)
    // call is already cached
    if (memo contains(key)) memo(key)
    // required for both the cases
    var ans: Int = recurse(s, t, i + 1, j)
    // recursion call and add the result to "ans"
    if (s.charAt(i) == t.charAt(j)) {
      ans += recurse(s, t, i + 1, j + 1)
    }
    // Cache the result
    memo + (key ->  ans)
    ans
  }
  // Check to see if the result for this recursive
  // Always calculate this result since it's
  // If the characters match, then we make another
  // Check to see if the result for this recursive
  // Always calculate this result since it's
  // If the characters match, then we make another

  def numDistinct(s: String, t: String): Int = {
    memo =  Map[(Integer, Integer), Integer]()
    recurse(s, t, 0, 0)
  }



}
