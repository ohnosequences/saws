package ohnosequences.saws.sqs

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._


trait SQSServiceAux extends ServiceAux {

  type validRegions = Is[EU.type]#or[US.type]
  val namespace = "sqs"

  // add here bound for queue matching this service
  // maybe queue.type??
  // it would be nice if we could bound the state to be just of Q
  def create[Q <: QueueAux, S <: StateOf[Q]](queue: Q): (Q, S)
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
// case object sqs_service extends SQSService(EU, intercrossing)