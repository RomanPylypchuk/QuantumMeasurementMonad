package operator

import pmf.PMF

//This is typeclass pattern to represent measurement of different quantum operators [type O] on
//state space of type T
trait OperatorMeasurement[O <: Operator, T] {
  def measure(inState: T): (PMF, List[T])
}

object OperatorMeasurement{
  def apply[O <: Operator,T](inState: T)(implicit om: OperatorMeasurement[O,T]): (PMF, List[T]) =
    om.measure(inState)
}
