package pmf

case class PMF(pmf: List[PMass]){

  def isNormalized: Boolean = pmf.map(_.probability).sum == 1.0
  def prob(value: String): Option[Double] = pmf.find(pMass => pMass.value == value).map(_.probability)

  def *:(beforePmass: PMass): PMF = PMF(pmf.map(pMass => beforePmass * pMass))
  def branch(leavesBranches: List[PMF]): PMF = {
    val outcomesPMFs: List[PMF] = pmf.zip(leavesBranches).map{case (pMass, branchPMF) =>
    pMass *: branchPMF
   }
   PMF(outcomesPMFs.flatMap(_.pmf))
  }
}

object PMF{
  def apply(pmasses: (String, Double)*): PMF = PMF(pmasses.map(pmassTuple => PMass(pmassTuple)).toList)
}
