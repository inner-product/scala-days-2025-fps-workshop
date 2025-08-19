package projects.ui

trait IO[Program[_]] {
  def prompt(message: String): Program[Unit]
  def read: Program[String]
}

trait Layout[Program] {
  def and[A, B](fst: Program[A], snd: Program[B]): Program[(A, B)]
}

final case class ConsoleProgram[A](run: () => A)

object ConsoleIO extends IO[ConsoleProgram] {
  def prompt(message: String): ConsoleProgram[String] =
    ConsoleProgram(() =>
      println(message)
      ()
    )
}
object ConsoleLayout extends IO[ConsoleProgram] {
  // etc ...
}

def feedback[Program[_]](io: IO[Program], layout: Layout[Program]): Program[(Unit, (Unit, String))] = {
  layout.and(
    io.prompt("Hello Scala Days!")
    layout.and(
      io.prompt("Are you enjoying yourself?"),
      io.read
    )
  )
}

trait Separator[Program[_]] {
  def rule: Program[Unit]

  def blank: Program[Unit]
}

object ConsoleSeparator extends Separator[ConsoleProgram] {
  def rule: ConsoleProgram[Unit] =
    println("-------------------------------------------")

  def blank: ConsoleProgram[Unit] =
    println()
}


def prettyFeedback[Program[_]](sep: Separator[Program], io: IO[Program], layout: Layout[Program]) =
  layout.and(
    sep.rule
    layout.and(
      feedback,
      layout.rule
    )
  )
