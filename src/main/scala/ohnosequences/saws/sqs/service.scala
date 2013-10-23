package ohnosequences.saws.sqs

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._


trait AnySQSService extends AnyService {

  type validRegions = Is[EU]#or[US]
  val namespace = "sqs"

  // add here bound for queue matching this service
  // maybe queue.type??
  // it would be nice if we could bound the state to be just of Q
  def create[Q <: AnyQueue, S <: StateOf[Q]](queue: Q): (Q, S)
}

abstract class SQSService[
  R <: AnyRegion : oneOf[AnySQSService#validRegions]#λ,
  A <: AnyAccount
](val region: R, val account: A) extends AnySQSService {

  type Region = R
  type Account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}

// example sqs service
// case object sqs_service extends SQSService(EU, intercrossing)