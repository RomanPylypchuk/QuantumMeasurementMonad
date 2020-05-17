import operator.{OperatorMeasurement, PauliZ}
import scotty.quantum.Qubit
import state.QuantumStateMonad.{measure, measureState}

object Test extends App {

  implicit val trivialZ: OperatorMeasurement[PauliZ, String]  = operator.TrivialAlwaysEvenPauliZ
  implicit val scottyZ: OperatorMeasurement[PauliZ, Qubit] = operator.ScottyQubitPauliZ

  //Measure |x> state in Z twice using Scotty library
  println(measureState(Qubit.fiftyFifty)(measure[PauliZ, Qubit], measure[PauliZ, Qubit]))

  //Trivial measurement of Z, always fair
  println(measureState("|in>")(measure[PauliZ, String], measure[PauliZ, String]))

}
