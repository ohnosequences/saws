package ohnosequences.saws.sqs

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._


trait AnySQSService extends AnyService {

  type here = this.type
  type validRegions = Is[EU]#or[US]
  val namespace = "sqs"

  // add here bound for queue matching this service
  // maybe queue.type??
  // it would be nice if we could bound the state to be just of Q
  def create[
      Q <: AnyQueue.from[here], 
      S <: AnyQueueState.of[Q]
    ](queue: Q): 
      (queue.type, S)

  // this is all crap, but works as a start
  def sendTo[Q <: AnyQueue.from[here], M <: AnyMessage.from[Q]](message: M, queue: Q): 
    SentMessage[M]

  def receive[Q <: AnyQueue.from[here]](queue: Q): 
    ReceivedMessage[Message[Q]]

  def timeout[Q <: AnyQueue](message: ReceivedMessage[Message[Q]], visibilityTimeout: Int):
    ReceivedMessage[Message[Q]]

  def delete[Q <: AnyQueue](message: ReceivedMessage[Message[Q]]): 
    Unit

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