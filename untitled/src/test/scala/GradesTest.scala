
import calculator.GradeCalculation
import grades.Grades.{A, B, C, FAIL}
import org.scalatest.{FlatSpec, Matchers}
import calculator.GradeCalculation._
class GradesTest extends FlatSpec with Matchers{

  "When attempted correct ans are 40% of total " should "return the Fail Grade " in {
    // Given the input
    val totalNumberOfQuestionsAsked=10
    val totalNumberOfCorrectAns= 4


    // When api is invoked
    val calculator= GradeCalculator
    val grade= calculator.calculateGrade(totalNumberOfQuestionsAsked, totalNumberOfCorrectAns)

    //Then result shd be like this
    grade should be(FAIL)

  }

  "When attempted correct ans are 60% of total " should "return the C Grade " in {
    // Given that
    val totalNumberOfQuestionsAsked=10.0
    val totalNumberOfCorrectAns= 6.0


    // When
    val calculator= GradeCalculator
    val grade= calculator.calculateGrade(totalNumberOfQuestionsAsked, totalNumberOfCorrectAns)

    //Then
    grade should be(C)

  }

  "When attempted correct ans are 75% of total " should "return the B Grade " in {
    // Given that
    val totalNumberOfQuestionsAsked=10.0
    val totalNumberOfCorrectAns= 7.5


    // When
    val calculator= GradeCalculator
    val grade= calculator.calculateGrade(totalNumberOfQuestionsAsked, totalNumberOfCorrectAns)

    //Then
    grade should be(B)

  }

  "When attempted correct ans are 85% of total " should "return the A Grade " in {
    // Given that
    val totalNumberOfQuestionsAsked=10.0
    val totalNumberOfCorrectAns= 8.5


    // When
    val calculator= GradeCalculator
    val grade=calculator.calculateGrade(100, 85) shouldBe A


    //Then
    grade should be(A)

  }

  "When attempted correct ans are 90% of total " should "return the A Grade " in {
    // Given that
    val totalNumberOfQuestionsAsked=10.0
    val totalNumberOfCorrectAns= 8.5


    // When
    val calculator= GradeCalculator
    val grade=calculator.calculateGrade(100, 85) shouldBe A


    //Then
    grade should be(A)

  }

  it should "handle edge cases" in {
    val calculator= GradeCalculator

    calculator.calculateGrade(100, 0) shouldBe FAIL
    calculator.calculateGrade(100, 100) shouldBe A
  }

}
