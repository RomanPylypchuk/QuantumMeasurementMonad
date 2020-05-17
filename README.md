# Quantum Measurement Monad
This is a simple implementation of Projective Measurement in Quantum Mechanics as a State Monad in Scala.

When measuring system in some state by quantum operator, 
one obtains a PMF with corresponding quantum numbers and probabilities (e.g. "+" with p1 and "-" with p2, p1+p2=1 for Qubit),
and corresponding states after(since we consider only projective measurement for now, these are of course just eigenvectors of the operator).
If we chain(sequence) few measurements, we will obtain a tree with corresponding probabilities, results, and states. 
The leaves of this tree can again be combined into pair (PMF, States), where each PMF labels will contain all consequent steps 
(quantum numbers after measurements, e.g. +-+ for Qubit), and final probability and state after for particular leaf. 
For that, PMF elements (label, probability) can be conveniently turned into Monoid,
with label concatenation and multiplication of probabilities as operation and ("", 1.0) as unit.

I've observed that this measurement can be implemented as a State[S,A] Monad, where type S is a List of states and type A is a PMF, as
described above. The only conceptual thing needed to achieve this is to implement a corresponding Kleisli arrow for particular operator
being measured. Here, this is done also using typeclass pattern, so one is not confined to representation (type) of operator and state, and
can use different libraries with different types for that. State Monad implementation was copied from the Red Book, not to use any libraries.

I'll add more comperehensive document about this and some real implementations using Scotty(https://www.entangled.xyz/scotty/) later.
