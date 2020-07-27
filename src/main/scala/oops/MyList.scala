package oops

trait MyList {
  /*
   * 
   * head: first element of list
   * tail: remainder of list
   * isEmpty:Is this list empty
   * add(int)=> return new List with this element added i.e to support immutability we always return New List
   * toString:string representation of the list
   */
  
  def head : Int
  def tail : MyList
  def isEmpty :Boolean
  def add(element:Int):MyList
  def printElements : String
  override def toString: String= "[" + printElements+"]"
}

 case object EmptyNode extends MyList {
  def head :Nothing = throw new  NoSuchElementException
  def tail :Nothing = throw new  NoSuchElementException
  def isEmpty :Boolean = true
  def add(element:Int) :MyList = new Node(element,EmptyNode)
  def printElements :String =""
}
class Node( h: Int, t: MyList) extends MyList {
  def head: Int = return h
  def tail: MyList =  return t
  def isEmpty :Boolean = return false
  def add(element:Int):MyList = new Node(element,this)
  def printElements :String ={
    if(t.isEmpty) "" + h
    else {
      h + "" + t.printElements
    }
  }
}

object ListTest extends App{

  val list=new Node(1,EmptyNode)
  println(list.head)
  println(list.tail)
  println(list)
  val newlist=list.add(2)
  println(newlist)
  val linkedlist=new Node(1,new Node(2,new Node(3,EmptyNode)))
  println(linkedlist.head)
  println(linkedlist.tail.head)
  println(linkedlist.add(4).head)
  println(linkedlist.toString())
}
