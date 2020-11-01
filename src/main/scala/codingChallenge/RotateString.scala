package codingChallenge

object RotateString  extends  App {

  //remove if not needed



    // function that rotates s towards left by d
    def leftrotate(str: String, d: Int): String = {
      val ans: String = str.substring(d) + str.substring(0, d)
      ans
    }

    // function that rotates s towards right by d
    def rightrotate(str: String, d: Int): String =
      leftrotate(str, str.length - d)

    // Driver code

      val str1: String = "GeeksforGeeks"
      println(leftrotate(str1, 2))
      val str2: String = "GeeksforGeeks"
      println(rightrotate(str2, 2))




}
