object TekSystem {


    def main(args: Array[String]): Unit = {
      val n = scala.io.StdIn.readLine().toInt
      if(n <= 0 || n > 20) {
        Console.err.println("please enter number of items between 1 and 20")
        System.exit(1)
      } else {
        var counter = 0
        while(counter < n) {
          val input = scala.io.StdIn.readLine().toInt
          if(input < -10 || input > 10) {
            Console.err.println("please enter input between -10 and 10")
            Console.out.println(s"$input is invalid input")
          } else {
            if(input % 2 == 0) Console.out.println(s"$input is even")
            else Console.out.println(s"$input is odd")
          }
          counter += 1
        }
      }
    }




}
