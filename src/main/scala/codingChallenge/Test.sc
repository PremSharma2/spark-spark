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