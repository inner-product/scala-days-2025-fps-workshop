package interpreters.regexp

enum Regexp {
  case Empty
  case Concat(left: Regexp, right: Regexp)

  def ++(that: Regexp): Regexp = Concat(this, that)
  def orElse(that: Regexp): Regexp = ???
  def repeat: Regexp = ???

  def matches(s: String): Boolean = ???
}
object Regexp {
  val empty: Regexp = Empty
  def apply(s: String): Regexp = ???
}
