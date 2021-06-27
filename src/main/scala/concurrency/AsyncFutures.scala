package concurrency




object AsyncFutures extends App{
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  Future {
    println(s"the future is here")
  }

  println(s"the future is coming")

}


object FuturesDataType extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

  val buildFile: Future[String] = Future {
    val f = Source.fromFile("build.sbt")
    try f.getLines.mkString("\n") finally f.close()
  }

  println(s"started reading build file asynchronously in main thread")
  println(s"status: ${buildFile.isCompleted}")
  Thread.sleep(250)
  println(s"status: ${buildFile.isCompleted}")
  println(s"status: ${buildFile.value}")

}


object FuturesCallbacks extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source
//AsyncAPI
  def getUrlSpec(): Future[Seq[String]] = Future {
    val f = Source.fromURL("http://www.w3.org/Addressing/URL/url-spec.txt")
    try f.getLines.toList finally f.close()
  }

  val urlSpec: Future[Seq[String]] = getUrlSpec()

  def find(lines: Seq[String], word: String) = lines.zipWithIndex collect {
    case (line, n) if line.contains(word) => (n, line)
  } mkString("\n")
/*
TODO
     Asynchronously processes the value in the future once the value becomes available.
     i.e it uses onComplete call back
      WARNING: Will not be called if this future is never completed or if it is completed with a failure.
   Since this method executes asynchronously and does not produce a return value,
    any non-fatal exceptions thrown will be reported to the ExecutionContext.
 */
  urlSpec foreach {
    lines => println(s"Found occurrences of 'telnet'\n${find(lines, "telnet")}\n")
  }

  urlSpec foreach {
    lines => println(s"Found occurrences of 'password'\n${find(lines, "password")}\n")
  }

  println("callbacks installed, continuing with other work")

}


object FuturesFailure extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

  val urlSpec: Future[String] = Future {
    Source.fromURL("http://www.w3.org/non-existent-url-spec.txt").mkString
  }
/*

TODO
  Note:
   Calling Future { throw ex } and Future.failed(ex) will create an equivalent result.
    However, using Future.failed is more efficient
   ------------------------------------------------------------------------------------
 TODO
   def failed: Future[Throwable] =
    transform({
      case Failure(t) => Success(t)
      case Success(v) => Failure(new NoSuchElementException("Future.failed not completed with a throwable."))
    })(internalExecutor)
    The returned Future will be successfully completed
    with the Throwable of the original Future if the original Future fails.
    If the original Future is successful,
    the returned Future is failed with a NoSuchElementException.

    TODO
     Creates a new Future by applying the specified function to the result of this Future.
     If there is any non-fatal exception thrown
     when 'f' is applied then that exception will be propagated to the resulting future.
   TODO def transform[S](f: Try[T] => Try[S])(implicit executor: ExecutionContext): Future[S]
 */
  urlSpec.failed foreach {
    case t => println(s"exception occurred - $t")
  }
  Thread.sleep(2000)
}


object FuturesExceptions extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

  val file: Future[String] = Future { Source.fromFile(".gitignore-SAMPLE").getLines.mkString("\n") }

  file foreach {
    text => println(text)
  }
// use failed when u want to log the exception actually for the future task
  file.failed foreach {
    case fnfe: java.io.FileNotFoundException => println(s"Cannot find file - $fnfe")
    case t => println(s"Failed due to $t")
  }

  import scala.util.{Failure, Success}

  file onComplete {
    case Success(text) => println(text)
    case Failure(t) => println(s"Failed due to $t")
  }

}


object FuturesTry extends App {
  import scala.util._

  val threadName: Try[String] = Try(Thread.currentThread.getName)
  val someText: Try[String] = Try("Try objects are created synchronously")
  val message: Try[String] = for {
    tn <- threadName
    st <- someText
  } yield s"$st, t = $tn"

  message match {
    case Success(msg) => println(msg)
    case Failure(error) => println(s"There should be no $error here.")
  }

}


object FuturesNonFatal extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  val f = Future { throw new InterruptedException }
  val g = Future { throw new IllegalArgumentException }
  //TODO Both are same actually in nature
  f.failed foreach { case t => println(s"error - $t") }
  g.failed foreach { case t => println(s"error - $t") }
}


object FuturesClumsyCallback extends App {
  import java.io._

  import org.apache.commons.io.FileUtils._

  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.collection.convert.decorateAsScala._
  import scala.io.Source

  def blacklistFile(filename: String) = Future {
    val lines: Iterator[String] = Source.fromFile(filename).getLines
    lines.filter(!_.startsWith("#")).toList
  }

  def findFiles(patterns: List[String]): List[String] = {
    val root = new File(".")
    for {
      f <- iterateFiles(root, null, true).asScala.toList
      pat <- patterns
      abspat = root.getCanonicalPath + File.separator + pat
      if f.getCanonicalPath.contains(abspat)
    } yield f.getCanonicalPath
  }

  blacklistFile(".gitignore") foreach {
    case lines =>
      val files = findFiles(lines)
      println(s"matches: ${files.mkString("\n")}")
  }
}


object FuturesMap extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source
  import scala.util.Success

  val buildFile: Future[Iterator[String]] = Future { Source.fromFile("build.sbt").getLines }
  val gitignoreFile = Future { Source.fromFile(".gitignore-SAMPLE").getLines }
  /*
  TODO
       Creates a new future by applying a function to the successful result of this future.
       If this future is completed with an exception then the new future will also contain this exception
   */
  val longestBuildLine: Future[String] = buildFile.map(lines => lines.maxBy(_.length))
  val longestGitignoreLine: Future[String] = for (lines <- gitignoreFile) yield lines.maxBy(_.length)

  longestBuildLine onComplete {
    case Success(line) => println(s"the longest line is '$line'")
  }

  longestGitignoreLine.failed foreach {
    case t => println(s"no longest line, because ${t.getMessage}")
  }
}


object FuturesFlatMapRaw extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

  val netiquette = Future { Source.fromURL("http://www.ietf.org/rfc/rfc1855.txt").mkString }
  val urlSpec = Future { Source.fromURL("http://www.w3.org/Addressing/URL/url-spec.txt").mkString }
  val answer: Future[String] = netiquette.flatMap { nettext =>
    urlSpec.map { urltext =>
      "First, read this: " + nettext + ". Now, try this: " + urltext
    }
  }

  answer foreach {
    case contents => println(contents)
  }
}


object FuturesFlatMap extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

  val netiquette = Future { Source.fromURL("http://www.ietf.org/rfc/rfc1855.txt").mkString }
  val urlSpec = Future { Source.fromURL("http://www.w3.org/Addressing/URL/url-spec.txt").mkString  }
  val answer = for {
    nettext <- netiquette
    urltext <- urlSpec
  } yield {
    "First of all, read this: " + nettext + " Once you're done, try this: " + urltext
  }

  answer foreach {
    case contents => println(contents)
  }

}
