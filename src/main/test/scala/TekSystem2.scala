object TekSystem2 {


  var unit: Array[String] = new Array[String](12)

  var conv: Array[Array[Int]] = null

  var i_dict: Array[Int] = new Array[Int](12)
  var k_dict: Array[Int] = new Array[Int](12)

  def main(args: Array[String]): Unit = {

    var n: Int = scala.io.StdIn.readLine().toInt
    while (n != 0) {
      unit = scala.io.StdIn.readLine().split(" ")
      conv = Array.ofDim[Int](12, 12)

      for (i <- 0 until n - 1) {

        var a: String = null
        var b: String = null

        var ia: Int = 0
        var ib: Int = 0
        var k: Int = 0
        var eq: Char = 0

        val arr = scala.io.StdIn.readLine().split(" ")
        a = arr(0)
        eq = arr(1).charAt(0)
        k = arr(2).toInt
        b = arr(3)

        for (j <- 0 until n) {
          if (unit(j) == a) {
            ia = j
          }

          if (unit(j) == b) {
            ib = j
          }

        }
        conv(ib)(ia) = k
      }

      for (iter <- 0 until n; i <- 0 until n; j <- 0 until n) {

        if (!(i == j)) {
          for (k <- 0 until n) {
            if (!(i == k || j == k)) {
              if (conv(i)(k) > 0 && conv(k)(j) > 0) {
                conv(i)(j) = conv(i)(k) * conv(k)(j)
              } else if (conv(i)(k) > 0 && conv(j)(k) > 0) {
                if (conv(i)(k) % conv(j)(k) == 0) {
                  conv(i)(j) = conv(i)(k) / conv(j)(k)
                } else if (conv(j)(k) % conv(i)(k) == 0) {
                  conv(j)(i) = conv(j)(k) / conv(i)(k)
                }
              } else if (conv(k)(i) > 0 && conv(k)(j) > 0) {
                if (conv(k)(j) % conv(k)(i) == 0) {
                  conv(i)(j) = conv(k)(j) / conv(k)(i)
                } else if (conv(k)(i) % conv(k)(j) == 0) {
                  conv(j)(i) = conv(k)(i) / conv(k)(j)
                }
              } else if (conv(k)(i) > 0 && conv(j)(k) > 0) {
                conv(j)(i) = conv(j)(k) * conv(k)(i)
              }
            }
          }
        }
      }

      for (i <- 0 until n) {
        var tot: Int = 0
        var k: Int = 1
        for (j <- 0 until n if conv(i)(j) > 0) {
          tot += 1
          if (k < conv(i)(j)) {
            k = conv(i)(j)
          }
        }

        i_dict(tot) = i
        k_dict(tot) = k
      }

      for (i <- 0 until n) {
        if (i != 0) {
          Console.out.print(" = ")
        }
        Console.out.print(k_dict(i))
        Console.out.print(unit(i_dict(i)))
      }

      Console.out.println()
      n = scala.io.StdIn.readLine().toInt
    }

  }


}
