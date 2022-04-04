package numberProblems
/*
 909 sums to 18, which sums to 9 .
 */
object SingledigitSum extends App{
  def sumOfDigits(n: Int): Int = {
    val modulo = n % 9
    if (n < 1) 0 else if (modulo == 0) 9 else modulo
  }

  assert(sumOfDigits(0) == 0)
  assert(sumOfDigits(2) == 2)
  assert(sumOfDigits(8) == 8)
  assert(sumOfDigits(10) == 1)
  assert(sumOfDigits(16) == 7)
  assert(sumOfDigits(32) == 5)
  assert(sumOfDigits(64) == 1)
  assert(sumOfDigits(101) == 2)
  assert(sumOfDigits(109) == 1)
  assert(sumOfDigits(128) == 2)
  assert(sumOfDigits(256) == 4)
  assert(sumOfDigits(512) == 8)
  assert(sumOfDigits(999) == 9)
  assert(sumOfDigits(1024) == 7)
  assert(sumOfDigits(2048) == 5)
  assert(sumOfDigits(4096) == 1)
}
