import pmf.{PMF, PMass}
import scotty.quantum.StateProbabilityReader.StateData
import scotty.quantum.{Circuit, Qubit, State, StateProbabilityReader}
import scotty.simulator.QuantumSimulator

package object operator {

  //Don't do any actual measurement, but always return even + or -, when "measuring" trivial String, not actual ket
  object TrivialAlwaysEvenPauliZ extends OperatorMeasurement[PauliZ, String]{
    def measure(inState: String): (PMF, List[String]) = {
      val pmf = PMF(List(PMass("+", 0.5), PMass("-", 0.5)))
      (pmf, List("|0> after " + inState, "|1> after " + inState))
    }
  }

  //This is simulated Z measurement of Qubit from Scotty library
  object ScottyQubitPauliZ extends OperatorMeasurement[PauliZ, Qubit]{
    implicit val defaultSimulator: QuantumSimulator = QuantumSimulator()

    def measure(inState: Qubit): (PMF, List[Qubit]) = {
      val inStateCircuit: Circuit = Circuit().withRegister(inState)
      val stateRead: State = QuantumSimulator().run(inStateCircuit)
      val statesAfter: Seq[StateData] = StateProbabilityReader(stateRead).read

      val pMassesAndQubitsAfter = statesAfter.map{case StateData(stateLabel, _ , probability) =>
        val (standardLabel, qubitAfter) = stateLabel match{
          case "0" => ("+", Qubit.zero)
          case _ => ("-", Qubit.one)
        }
        (PMass(standardLabel, probability), qubitAfter)
      }

      val (pMasses, qubits) = pMassesAndQubitsAfter.unzip
      (PMF(pMasses.toList), qubits.toList)
    }
  }

}
