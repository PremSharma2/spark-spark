package codingChallenge

object CaptalLetters  extends App {

  def detectCapitalUse(word: String): Boolean = word.matches("[A-Z]*|.[a-z]*")

}
