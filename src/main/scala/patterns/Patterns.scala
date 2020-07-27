package patterns

object Patterns extends App {

  case class Person(name: String, age: Int)
  val bob = Person.apply("Bob", 20)
  val greeting: String = bob match {
    case Person(n, a) => s"Hi, my name is $n and my age is $a"
    case _            => "I dont know who i am"
  }
  println(greeting)

  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr
  def show(e: Expr): String = e match {
    case Number(n)   => s"$n"
    case Sum(e1, e2) => show(e1) + " + " + show(e2)
    case Prod(e1, e2) => {
      def maybeShowParanthesis(expr: Expr) = expr match {
        case Prod(_, _) => show(expr)
        case Number(_)  => show(expr)
        case _          => "(" + show(expr) + ")"
      }
      maybeShowParanthesis(e1) + " * " + maybeShowParanthesis(e2)
    }
  }
  println(show(Sum(Number(2), Number(3))))
  println(show(Prod(Sum(Number(2), Number(1)), Number(3))))


}