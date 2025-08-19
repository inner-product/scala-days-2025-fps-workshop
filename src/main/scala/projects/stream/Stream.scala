package projects.stream

// APIs
//
// Familiar with similar stuff (e.g List)
// Common patterns of compositions
// - Monoid: add (A, A) => A, 1 + 1, 1 * 1, "foo" ++ "bar"
// - Functor: map
// - Applicative: pure and product
// - Monad: flatMap

enum Stream[A] { self =>
  case Concat(left: Stream[A], right: Stream[A])
  case Map[A, B](source: Stream[A], f: A => B) extends Stream[B]
  case Product[A, B](left: Stream[A], right: Stream[B]) extends Stream[B]
  case FlatMap[A, B](source: Stream[A], f: A => Stream[B]) extends Stream[B]
  case Pure(value: A)
  case FromIterator(value: Iterator[A])

  def ++(that: Stream[A]): Stream[A] =
    Concat(this, that)

  def map[B](f: A => B): Stream[B] =
    Map(this, f)

  def product[B](that: Stream[B]): Stream[(A, B)] =
    Product(this, that)

  def flatMap[B](f: A => Stream[B]): Stream[B] =
    FlatMap(this, f)

  def toList: List[A] = {
    compile.toList
  }

  private def compile: Program =
    ???
}
object Stream {
  def pure[A](value: A): Stream[A] =
    Pure(value)

  def fromIterator[A](iterator: Iterator[A]): Stream[A] =
    FromIterator(iterator)
}
