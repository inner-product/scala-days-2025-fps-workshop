package codata.stream

trait Stream[A] {
  def head: Int
  def tail: Stream[Int]
}
