package lazyevaluation

object WithFilter {
  /**
   *
   * TODO
   * The primary difference between filter and withFilter is
   * that filter will immediately produce a new collection
   * satisfying the filtering condition,
   * whereas withFilter will not produce a new collection
   * but will keep the filtering condition to be applied
   * later during other operations like map or flatMap.
   */


  implicit class MyCollection[A](val data: Seq[A]) {
    def withFilter(p: A => Boolean) = new WithFilter(p)

    class WithFilter(p: A => Boolean) {
      def map[B](f: A => B): MyCollection[B] = {
        val newData = data.filter(p).map(f)
        new MyCollection(newData)
      }

      def flatMap[B](f: A => MyCollection[B]): MyCollection[B] = {
        val newData = data.filter(p).flatMap(a => f(a).data)
        new MyCollection(newData)
      }

      def forEach(f: A => Unit): Unit = {
        data.filter(p).foreach(f)
      }
    }
  }

}
