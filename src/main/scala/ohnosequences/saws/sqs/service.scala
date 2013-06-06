package ohnosequences.saws.sqs

import ohnosequences.saws._
import ohnosequences.saws.typeOps._

trait SQSServiceAux extends ServiceAux {

  type validRegions = Is[EU.type]#or[US.type]
  val namespace = "sqs"
}

abstract class SQSService[
  R <: RegionAux : oneOf[SQSServiceAux#validRegions]#λ,
  A <: AccountAux
](val region: R, val account: A) extends SQSServiceAux {

  type region = R
  type account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}

// example sqs service
case object sqs_service extends SQSService(EU, intercrossing)