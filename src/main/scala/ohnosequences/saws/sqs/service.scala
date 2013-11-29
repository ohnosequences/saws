package ohnosequences.saws.sqs

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._

import shapeless._

trait AnySQSService extends AnyService {

  type here = this.type
  val namespace = "sqs"

  def create[
      Q <: AnyQueue.from[here], 
      S <: AnyQueueState.of[Q]
    ](arg: AnySQSService.CreateQueue[Q,S]): (arg.Output, arg.OutputState)

  def delete[
      Q <: AnyQueue.from[here], 
      S <: Unknown[Q]
    ](arg: AnySQSService.DeleteQueue[Q,S]): (arg.Output, arg.OutputState)
}

abstract class SQSService[
  R <: AnyRegion: oneOf[AnySQSService.validRegions]#is,
  A <: AnyAccount
](val region: R, val account: A) extends AnySQSService {

  type Region = R
  type Account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}


object AnySQSService {

  type validRegions = either[EU]#or[US]
  type validActions = either[AnyCreateQueue]#or[AnyDeleteQueue]

  trait AnyCreateQueue extends AnyAction {

    // note how here we're bounding the implementation to return the very same queue
    type Input        <: AnyQueue
    type InputState   <: AnyQueueState.of[Input]
    type Output        = Input
    type OutputState  <: AnyQueueState.of[Output] :+: Errors :+: CNil

    type Errors       <: AnyQueueState.of[Output]
  }

  case class CreateQueue[Q <: AnyQueue, S <: AnyQueueState.of[Q]]
    (val queue: Q, val state: S) extends AnyCreateQueue {

    type Input = Q
    type InputState = S
    type OutputState  = QueueState[Output] :+: Errors :+: CNil

    // signal those outputs that are considered errors
    type Errors = Error

    trait Error extends AnyQueueState {  type Resource = Output  }

      case class AlreadyCreated(queue: Input, state: InputState) 
        extends Error { val resource = queue }
  
      case class RecentlyDeleted(queue: Input, state: InputState) 
        extends Error { val resource = queue }
  }

  trait AnyDeleteQueue extends AnyAction {

    type Input        <: AnyQueue
    type Output        = Input
    type InputState   <: Unknown[Input]
    type OutputState  <: Deleted[Output] :+: Errors :+: CNil
    // signal those outputs that are considered errors
    type Errors       <: AnyQueueState.of[Output]
  }

  case class DeleteQueue[Q <: AnyQueue, S <: Unknown[Q]]
    (val queue: Q, val state: S) extends AnyDeleteQueue {

    type Input        = Q
    type InputState   = S
    type OutputState  = Deleted[Output] :+: Errors :+: CNil
    type Errors       = Error

    trait Error extends AnyQueueState {  type Resource = Output  }
      
      case class NonExistent(queue: Input, state: InputState) 
        extends Error { val resource = queue }
  }
}