package pmf

case class PMass(value: String, probability: Double){
  //This is a Monoid
  def *(that: PMass): PMass = PMass(this.value + "," + that.value, this.probability * that.probability)
}

object PMass{
  def apply(pmassTuple: (String, Double)): PMass = PMass(pmassTuple._1, pmassTuple._2)
  def empty: PMass = PMass("", 0.0)
}