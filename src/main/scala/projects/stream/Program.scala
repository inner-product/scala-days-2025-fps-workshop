package projects.stream

trait Program[A] {
  protected def next: Option[A]

  def toList: List[A]
}
