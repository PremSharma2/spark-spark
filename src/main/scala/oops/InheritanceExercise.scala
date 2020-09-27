package oops

object InheritanceExercise extends App {


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

}
