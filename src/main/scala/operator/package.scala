import pmf.{PMF, PMass}

package object operator {

  //Don't do any actual measurement, but always return even + or -, when "measuring" trivial String, not actual ket
  object TrivialAlwaysEvenPauliZ extends OperatorMeasurement[PauliZ, String]{
    def measure(inState: String): (PMF, List[String]) = {
      val pmf = PMF(List(PMass("+", 0.5), PMass("-", 0.5)))
      (pmf, List("|0> after " + inState, "|1> after " + inState))
    }
  }
}
