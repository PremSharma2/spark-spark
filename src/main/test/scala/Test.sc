import scala.collection.SeqView

val l = List(1,2,3,4)
val l1 = List(5,6,7,8)
val s = Set.apply(1,2,3,4)
val s1 = Set(5,6,7,8)
println(l ++ l1)

println(l :: l1)
println(l ::: l1)

s.filter(x => s1.apply(x))
var x,y,z = (1,2,3)
println(x,y,z)
//val threads= (1 to 100).map( _ => new Thread(() => x += 1))
val filesHere= Seq("abc.scala","xyz.java","def.scala")


val scalaFiles: Seq[String] =
  for {
    file <- filesHere
     if file.endsWith(".scala")
  } yield file

val scalaFiles =
  for {
    file <- filesHere
    expression= file.endsWith(".scala")
  } yield  expression

val monthlyConsumptionAmount = Seq(437.8,3339.5,0.0,0.0,0.0,0.0,75.0,99.0,0.0,20.0,66.0)
//feedback(monthlyConsumptionAmount)
val feedback: SeqView[Map[String, String], Seq[_]] = {
  val monthNames: Array[String] = Array("Jan", "Feb", "Mar", "Apr", "May",
    "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
  for {
    (xs, i) <- monthlyConsumptionAmount.view.zipWithIndex
       } yield
    Map(s"Energy use for ${monthNames(i)}:" -> s"${"%.2f".format(xs)}")
}