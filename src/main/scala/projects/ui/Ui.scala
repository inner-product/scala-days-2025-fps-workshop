package projects.ui

trait IO[Program[_]] {
  def prompt(message: String): Program[Unit]
  def read: Program[String]
}

trait Layout[Program[_]] {
  def and[A, B](fst: Program[A], snd: Program[B]): Program[(A, B)]
}

final case class ConsoleProgram[A](run: () => A)

object ConsoleIO extends IO[ConsoleProgram] {
  def prompt(message: String): ConsoleProgram[Unit] =
    ConsoleProgram(() =>
      println(message)
      ()
    )

  def read: ConsoleProgram[String] = {
    ConsoleProgram(() => scala.io.StdIn.readLine())
  }
}
object ConsoleLayout extends Layout[ConsoleProgram] {
  def and[A, B](
      fst: ConsoleProgram[A],
      snd: ConsoleProgram[B]
  ): ConsoleProgram[(A, B)] = {
    ConsoleProgram { () =>
      val a = fst.run()
      val b = snd.run()

      (a, b)
    }
  }
}

def feedback[Program[_]](
    io: IO[Program],
    layout: Layout[Program]
): Program[(Unit, (Unit, String))] = {
  layout.and(
    io.prompt("Hello Scala Days!"),
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
    ConsoleProgram(() => println("-------------------------------------------"))

  def blank: ConsoleProgram[Unit] =
    ConsoleProgram(() => println())
}

def prettyFeedback[Program[_]](
    sep: Separator[Program],
    io: IO[Program],
    layout: Layout[Program]
) =
  layout.and(
    sep.rule,
    layout.and(
      feedback(io, layout),
      sep.rule
    )
  )
