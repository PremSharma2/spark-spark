package generics
// this is also an action hence it is also Contravrient
trait MyTransformer[-A,B] {
  
  def transform(element:A):B
  
  
}