package calculator

import grades.Grades.{A, B, C, FAIL, Grades}

object GradeCalculation {
  class GradeCalculator()

  object GradeCalculator {

    def calculateGrade1(totalNumberOfQuestionsAsked: Double, totalNumberOfCorrectAnswered: Double): Grades = {
      val percentage = (totalNumberOfCorrectAnswered / totalNumberOfQuestionsAsked) * 100
      if (percentage == 85.0) A
      else if (percentage == 75.0) B
      else if (percentage == 60.0) C
      else FAIL
    }


    def calculateGrade(totalNumberOfQuestionsAsked: Double, totalNumberOfCorrectAnswered: Double): Grades = {
      // Input validation: Ensure that the totalNumberOfQuestionsAsked is not zero to avoid division by zero.
      if (totalNumberOfQuestionsAsked <= 0) throw new IllegalArgumentException("totalNumberOfQuestionsAsked should be greater than zero.")

      val percentage = (totalNumberOfCorrectAnswered / totalNumberOfQuestionsAsked) * 100

      // Define a tolerance level for comparing floating-point numbers.
      val tolerance = 0.0001

      val grade = percentage match {
        case p if Math.abs(p - 85.0) < tolerance => A
        case p if Math.abs(p - 75.0) < tolerance => B
        case p if Math.abs(p - 60.0) < tolerance => C
        case _ => FAIL
      }

      grade
    }

  }
}


