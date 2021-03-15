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

 case object CNil extends MyList {
  def head :Nothing = throw new  NoSuchElementException
  def tail :Nothing = throw new  NoSuchElementException
  def isEmpty :Boolean = true
  def add(element:Int) :MyList = new Node(element,CNil)
  def printElements :String =""
}

class Node(h: Int, nodeTail: MyList) extends MyList {
  def head: Int = return h
  def tail: MyList =  return nodeTail
  def isEmpty :Boolean = return false
  def add(element:Int):MyList = new Node(element,this)
  def printElements :String ={
    if(nodeTail.isEmpty) "" + h
    else {
      h + "" + nodeTail.printElements
    }
  }
}

class Cons( override val head:Int, override val  tail: MyList) extends MyList {
  def isEmpty :Boolean =  false
  def add(element:Int):MyList = new Node(element,this)
  def printElements :String ={
    if(tail.isEmpty) "" + head
    else {
      head + "" + tail.printElements
    }
  }
}
object ListTest extends App{
 val cons = new Cons(1,new Cons(2,CNil))
  val list=new Node(1,CNil)
  println(list.head)
  println(list.tail)
  println(list)
  val newlist: MyList =list.add(2)
  println(newlist)
  val linkedlist=new Node(1,new Node(2,new Node(3,CNil)))
  println(linkedlist.head)
  println(linkedlist.tail.head)
  println(linkedlist.add(4).head)
  println(linkedlist.toString())
  println(cons.head)
}
