val l = List(1,2,3,4)
val l1 = List(5,6,7,8)
val s = Set.apply(1,2,3,4)
val s1 = Set(5,6,7,8)
println(l.take(l.size-1).+:(l.last))
println(l ++ l1)
val listOFList= List(l,l1)
val  transposeResult: List[List[Int]] = listOFList.transpose
println("transpose"+ transposeResult)
println(l +:l1)
println(l :: l1)
println(l ::: l1)
val women = List("Wilma", "Betty")
val men = List("Fred", "Barney")
val couples = women zip men
s.filter(x => s1.apply(x))
var x,y,z = (1,2,3)
println(x,y,z)
println(l.mkString("[",",","]"))
val str= "hello"
println(str.takeRight(2))
val string1 = "A"
val string2 = "B"
val string3 = "C"
val padLength = 20
val string4 = string1 + string2.padTo(padLength, ' ') + string3



