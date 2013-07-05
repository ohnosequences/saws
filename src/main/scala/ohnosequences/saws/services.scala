package ohnosequences.saws

import typeOps._

trait ServiceAux {
  
  type region <: RegionAux
  type account <: AccountAux
  val region: region
  val account: account

  val host = "amazonaws.com"
  val namespace: String
  // add here isSecure or something similar
  def endpoint: String
}


