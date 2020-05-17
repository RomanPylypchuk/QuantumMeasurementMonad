package state

import operator.{Operator, OperatorMeasurement}
import pmf.PMF

object QuantumStateMonad {

  //Initial State Monad
  def unitState[T]: State[List[T], PMF] = State.unit[List[T], PMF](PMF(("", 1.0)))

  //Quantum projective measurement as Kleisli arrow for State Monad
  def operatorToKleisliArrow[O <: Operator,T](implicit om: OperatorMeasurement[O,T]): PMF => State[List[T], PMF] = pmf =>
    State{ states =>
      val measuredEach: List[(PMF, List[T])] = states.map(state => OperatorMeasurement[O,T](inState = state))
      val pmfsAfter: List[PMF] = measuredEach.map{case (pmfAfterInState, _) => pmfAfterInState}
      val statesAfter: List[T] = measuredEach.flatMap{case (_, statesAfterInState) => statesAfterInState}
      (pmf.branch(pmfsAfter), statesAfter)
    }

  //Compose previous measurements from State with new measurement from operator [O] to get new State measurement
  def measure[O <: Operator, T](measBefore: State[List[T], PMF])(implicit om: OperatorMeasurement[O,T]): State[List[T], PMF] = {
    measBefore.flatMap(operatorToKleisliArrow[O,T])
  }

  //Measure inState according to specified State transformations (which can be built conveniently from measure method)
  def measureState[T](inState: T)(ops: (State[List[T], PMF] => State[List[T], PMF])*): (PMF, List[T]) = {
    val measurements: State[List[T], PMF] = ops.foldLeft(unitState[T])((beforeState, addStateFn) => addStateFn(beforeState))
    measurements.run(List(inState))
  }

}
