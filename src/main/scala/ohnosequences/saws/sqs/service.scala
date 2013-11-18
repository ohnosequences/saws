package ohnosequences.saws.sqs

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._

import shapeless._

object AnySQSService {

  type validRegions = Is[EU]#or[US]
  type validActions = Is[AnyCreateQueue]#or[DeleteQueue]
}
trait AnySQSService extends AnyService {

  type here = this.type
  val namespace = "sqs"


  // def please[A <: AnyAction](action: A)
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

  def timeout[Q <: AnyQueue.from[here]](message: ReceivedMessage[Message[Q]], visibilityTimeout: Int):
    ReceivedMessage[Message[Q]]

  def delete[Q <: AnyQueue.from[here]](message: ReceivedMessage[Message[Q]]): 
    Unit

}

abstract class SQSService[
  R <: AnyRegion: oneOf[AnySQSService.validRegions]#is,
  A <: AnyAccount
](val region: R, val account: A) extends AnySQSService {

  type Region = R
  type Account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}

// example sqs service
// case object sqs_service extends SQSService(EU, intercrossing)

/////////////////////////////////////////////////////////////////////////////////////////


/*
### Actions

The main issue I have with this is how to declare that through a particular service you can perform a set of actions.

I wrote a bit about this in [resources](../resources.md).

*/
sealed trait AnyCreateQueue extends AnyAction
case class CreateQueue[Q <: AnyQueue, S <: QueueState[Q]](queue: Q, state: S) extends AnyCreateQueue {

  type Input = Q
  type InputState = S

  // note how here we're bounding the implementation to return the very same queue
  type Output = Input
  type OutputState  = AnyQueueState.of[Output] :+: Errors :+: CNil

  // signal those outputs that are considered errors
  type Errors = Error
  trait Error extends AnyQueueState {
    type Resource = Output
  }
  // error! possible due to open unions
  case class AlreadyCreated(queue: Input, state: InputState) 
    extends Errors { val resource = queue }
  case class RecentlyDeleted(queue: Input, state: InputState) 
    extends Errors { val resource = queue }
}


trait DeleteQueue extends AnyAction {


  type Input  <: AnyQueue
  type Output = Input

  type InputState  = Unknown[Input]
  type OutputState = Deleted[Output] :+: Errors :+: CNil

  // signal those outputs that are considered errors
  type Errors = Error
  trait Error extends AnyQueueState {
    type Resource = Output
  }
  // error! possible due to open unions
  case class NonExistent(queue: Input, state: InputState) 
    extends Error { val resource = queue }
}


