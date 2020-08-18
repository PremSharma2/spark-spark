package concurrency

object ProducerConsumer extends App {


class SimpleContainer{
  private var value: Int = 0

  def isEmpty : Boolean = value == 0

  def get: Int = {
   val  result:Int = value
    value= 0
    result
  }
  def set(newValue:Int) = value = newValue
}
}
