package duality.bool

trait Bool {
  def `if`[A](t: A)(f: A): A
}

val True = new Bool {
  def `if`[A](t: A)(f: A): A = t
}
val False = new Bool {
  def `if`[A](t: A)(f: A): A = f
}
