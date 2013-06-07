package ohnosequences.saws

import typeOps._

trait ServiceAux {
  
  type region <: RegionAux
  type account <: AccountAux

  val region: region
  val account: account

  val host = "amazonaws.com"
  val namespace: String
  def endpoint: String
}

trait EC2ServiceAux extends ServiceAux {

  type validRegions = Is[EU.type]#or[US.type]#or[US_WEST_1.type]
  val namespace = "ec2"
}

abstract class EC2Service[
  R <: RegionAux : oneOf[EC2ServiceAux#validRegions]#λ,
  A <: AccountAux
](val region: R, val account: A) extends EC2ServiceAux {

  type region = R
  type account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}


object test {

  def isOk[R <: RegionAux : oneOf[EC2ServiceAux#validRegions]#λ](region: R) = true

  isOk(EU)
  isOk(US)
  isOk(US_WEST_1)
  // no no
  // isOk(US_WEST_2)
}