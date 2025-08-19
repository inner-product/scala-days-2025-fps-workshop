package projects.ui

trait Algebra {
  type Ui[_]
}

final case class Program[-Alg <: Algebra, A] {
  def apply(alg: Alg): alg.Ui[A]
}

object Program {
  def prompt(message: String): Program[IO, Unit] =
    Program {
      def apply(alg: IO): alg.Ui[Unit] =
        alg.prompt(message)
    }
  def read: Program[IO, String] =
    Program {
      def apply(alg: IO): alg.Ui[String] =
        alg.read
    }
  def rule: Program[Separator, Unit]
    Program {
      def apply(alg: Separator): alg.Ui[Unit] =
        alg.rule
    }
  def blank: Program[Separator, Unit]
    Program {
      def apply(alg: Separator): alg.Ui[Unit] =
        alg.blank
    }
}

extension [Alg <: Algebra, A](program: Program[Alg, A]) {
  def and[Alg2 <: Algebra, B](snd: Program[Alg2, B]) = {
    type AllAlg = Alg & Alg2 & Layout, (A, B)
    Program[AllAlg, (A, B)]{
      def apply(alg: AllAlg): alg.Ui[(A, B)] =
        alg.and(program, snd)
    }
  }
}

trait IO extends Algebra {
  def prompt(message: String): Ui[Unit]
  def read: Ui[String]
}

trait Layout extends Algebra {
  def and[A, B](fst: Ui[A], snd: Ui[B]): Ui[(A, B)]
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

val feedback: Program[IO & Layout, (Unit, (Unit, String))] = {
  Program.prompt("Hello Scala Days!")
    .and(Program.prompt("Are you enjoying yourself?"))
    .and(Program.read)
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

val prettyFeedback =
  Program.rule.and(feedback).and(Program.rule)
