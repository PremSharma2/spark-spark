package codingChallenge

import codingChallenge.CapcoweightedMean.weightedMean
import codingChallenge.LazyListExercise.{AllPrefix, words}

import scala.collection.immutable.{Seq, Stream}
import scala.io.Source
import scala.util.{Failure, Success, Try}

object CapcoTest  extends App {
//---------------------------------Question1-------------------------------------
  def readFirstLine(path: String): Try[String] = {
    Try {
      var file = Source.fromFile(path)
      var buffer = file.getLines().next
      file.close
      buffer
    }
  }
  //Ans:-> Failure(java.io.FileNotFoundException: )
//-------------Question2---------------------------------------------------------------------------------
    def numbersToLetters(input:String):String={

      var table = Map(1->"A",2->"B",3->"C",4->"D",5->"E",6->"F",7->"G",
        8->"H",9->"I",10->"J",11->"K",12->"L",13->"M",14->
          "N",15->"O",16->"P",17->"Q",18->"R",19->"S",20->"T",21->"U",22->"V"
        ,23->"W",24->"X",25->"Y",26->"Z")

      var data=input.split(" ")

      var output:String=""

      for(number <- data)
      {
        if(number.indexOf("+")<0) //if it is number represent it by letter

          output+=table(number.toInt)

        else
        {
          //parse the word

          data=number.split("\\+")

          for(i <- data)

            output+=table(i.toInt)+" "

        }

        output=output.trim()

      }

      return output

    }



      println(numbersToLetters("20 5 19 20+4 15 13 5"))

   println(readFirstLine(""))




//--------------------------------Question4------------------------------------------------------------------

object LanguageTeaching {
  class LanguageStudent {
    var langs = List[String]()

    def addLanguage(language: String) = {
      langs = language :: langs
    }

    def getLanguages(): List[String] = {
      langs
    }
  }

  class LanguageTeacher extends LanguageStudent {
    def teach(student: LanguageStudent, language: String): Boolean = {
      if(getLanguages().contains(language)) {
        student.addLanguage(language)
        return true
      }
      return false
    }
  }

}

  val teacher = new LanguageTeaching.LanguageTeacher
  teacher.addLanguage("English")
  val student = new LanguageTeaching.LanguageStudent
  teacher.teach(student, "English")
  for(language <- student.getLanguages())
    System.out.println(language);


  //------------------Question3----------------------------------------------------------


  //val st = "thei moon issss very fara away from earth" // words to be checked
  val words: Seq[String] = "flow" #:: "flowers" #:: "flew" #:: "flag" #:: "fm" #:: Stream.empty
  AllPrefix(3, words).foreach(println)
  def AllPrefix(n: Int, words: Seq[String] ) = { //function to return prefixes

    val alist: Seq[String] = for  {
      word <- words
      if (word.length>=3)
    } yield word.toString.substring(0,3)
    alist.distinct
  }

  //----------------------------------------Question5---------------------------------------------------


  def weightedMean(X: Array[Int], W: Array[Int]) = {
    Try {
      var sum: Int = 0
      var numWeight: Int = 0
      for (i <- 0 until X.length) {
        numWeight = numWeight + X(i) * W(i)
        sum = sum + W(i)
      }
      ((numWeight).toFloat / sum).toDouble
    }
  }

  val X: Array[Int] = Array(3,6)
  val W: Array[Int] = Array(4,2)

  val result: Try[Double] =weightedMean(X, W)
  result match {
    case Success(value) => println(value)
    case _  => Failure(throw new IllegalArgumentException)
  }



}
