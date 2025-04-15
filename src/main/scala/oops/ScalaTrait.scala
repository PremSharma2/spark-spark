package oops

object ScalaTrait  extends App {
/*
TODO
    Trait:
     a trait is a fundamental unit of code reuse.
     Abstract methods (like an interface)
     Concrete methods with implementations
     Fields/variables
      where traits are commonly used is in logging.
      Suppose we are building an application where different components
      need to log messages in different ways (console, file, database).
      Code Reusability – Avoids code duplication by providing common behavior.
     Multiple Inheritance – A class can extend multiple traits, unlike classes.
     Modular Design – Helps separate concerns like logging, security, etc.
 */

  // Define a reusable Logging trait
  trait Logger {
    def log(message: String): Unit
  }

  // Console logging
  class ConsoleLogger extends Logger {
    def log(message: String): Unit = println(s"Console Log: $message")
  }

  // File logging (simulated)
  class FileLogger extends Logger {
    def log(message: String): Unit = println(s"File Log: $message (writing to file)")
  }

  // A class that needs logging
  class Application(logger: Logger) {
    def run(): Unit = {
      logger.log("Application started!")
    }
  }

  // Using different loggers
  val app1 = new Application(new ConsoleLogger)
  app1.run()  // Output: Console Log: Application started!

  val app2 = new Application(new FileLogger)
  app2.run()  // Output: File Log: Application started! (writing to file)


}
