package state

case class State[S,+A](run: S => (A, S)) {
  import State._

  def map[B](f: A => B): State[S, B] = flatMap(a => unit(f(a)))

  def map2[B,C](sb: State[S, B])(f: (A, B) => C): State[S, C] = flatMap(a =>
    sb.map(b => f(a,b))
  )

  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s =>
  {
    val (a, ns) = run(s)
    f(a).run(ns)
  }
  )
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a,s))

  def sequence[S,A](sas: List[State[S,A]]): State[S, List[A]] =
    sas.foldRight(unit[S,List[A]](List()))( (sa, sla) => sa.map2(sla)(_ :: _) )

  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get // Gets the current state and assigns it to `s`.
    _ <- set(f(s)) // Sets the new state to `f` applied to `s`.
  } yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))

}