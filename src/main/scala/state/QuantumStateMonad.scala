package state

import operator.{Operator, OperatorMeasurement}
import pmf.PMF

object QuantumStateMonad {

  //Quantum projective measurement as Kleisli arrow for State Monad
  def operatorToKleisliArrow[O <: Operator,T](implicit om: OperatorMeasurement[O,T]): PMF => State[List[T], PMF] = pmf =>
    State{ states =>
      val measuredEach: List[(PMF, List[T])] = states.map(state => OperatorMeasurement[O,T](inState = state))
      val pmfsAfter: List[PMF] = measuredEach.map{case (pmfAfterInState, _) => pmfAfterInState}
      val statesAfter: List[T] = measuredEach.flatMap{case (_, statesAfterInState) => statesAfterInState}
      (pmf.branch(pmfsAfter), statesAfter)
    }

}
