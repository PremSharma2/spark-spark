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
println(l.mkString("[",",","]"))

val i = 102119
var rem = -1
var num = i
var lstBuffer = scala.collection.mutable.ListBuffer[(Int,Int)]()
while(num > 0) {
  rem = num % 10
  if (!lstBuffer.exists(t => t._1 == rem)) {
    lstBuffer.append((rem, 1))
  } else {
    var cnt = lstBuffer.filter(t => t._1 == rem).head._2
    lstBuffer -= ((rem, cnt))
    cnt += 1
    lstBuffer.append((rem, cnt))
  }
}

println(lstBuffer.toList.head)