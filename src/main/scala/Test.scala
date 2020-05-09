import operator.PauliZ
import pmf.PMF
import state.State
import state.QuantumStateMonad.operatorToKleisliArrow

object Test extends App{

  implicit val trivialZ = operator.TrivialAlwaysEvenPauliZ

  val measureTwice = State.unit[List[String], PMF](PMF(("", 1.0))).flatMap(operatorToKleisliArrow[PauliZ, String]).flatMap(operatorToKleisliArrow[PauliZ, String])
  val resultTwice = measureTwice.run(List("|0in>"))
  println(resultTwice)

}
