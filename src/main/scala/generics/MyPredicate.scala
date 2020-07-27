package generics
// this is an action hence it is contravarient
trait MyPredicate[-T] {
  
  def test(element: T): Boolean
  
}

