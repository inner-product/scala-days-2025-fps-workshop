package projects.stream

trait Program[A] {
  def next: Option[A]

  def toList: List[A] = {
    def loop(): List[A] =
      this.next match {
        case None    => Nil
        case Some(a) => a :: loop()
      }

    loop()
  }
}
