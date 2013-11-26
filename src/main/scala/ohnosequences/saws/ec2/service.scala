package ohnosequences.saws.ec2

import ohnosequences.saws.{AnyService, AnyRegion, AnyAccount}
import ohnosequences.saws.regions._
import ohnosequences.saws.typeOps._

trait AnyEC2Service extends AnyService {

  type validRegions = Is[EU]#or[US]#or[US_WEST_1]
  val namespace = "ec2"
}

abstract class EC2Service[
  R <: AnyRegion : oneOf[AnyEC2Service#validRegions]#λ,
  A <: AnyAccount
](val region: R, val account: A) extends AnyEC2Service {

  type Region = R
  type Account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}

/////////////////////////////////

object test {

  def isOk[R <: AnyRegion : oneOf[AnyEC2Service#validRegions]#λ](region: R) = true

  isOk(EU)
  isOk(US)
  isOk(US_WEST_1)
  // no no
  // isOk(US_WEST_2)
}