package capgemini

import scala.collection.immutable._


object LazyListExercise  extends App {

/*
filter all words with length >= 3
map to substring(0,3)
call distinct

 withFilter { case (d, _) => d.getMonthValue() == i } .
        map ( t=> t._2)sum

         /*

             if (alist.contains(word.substring(0, n))
               alist.#::(arr(i).substring(0, n))



         */

 */


      val st = "thei moon issss very fara away from earth" // words to be checked
  val words: Seq[String] = "flow" #:: "flowers" #:: "flew" #:: "flag" #:: "fm" #:: Stream.empty
  AllPrefix(3, words).foreach(println)
    def AllPrefix(n: Int, words: Seq[String] ) = { //function to return prefixes

     val alist: Seq[String] = for  {
           word <- words
            if (word.toCharArray.length>=3)
           } yield word.toString.substring(0,3)
      alist.distinct
      }

}
