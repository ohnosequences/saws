package ohnosequences.saws.sqs

import ohnosequences.saws.{ResourceAux, StateOf}


object QueueTypes {
  
  // bound this here once formats are available
  type Name = String
}

import QueueTypes._

trait QueueAux extends ResourceAux {
  
  type service <: SQSServiceAux
  // maybe?
  // val service: service
  val name: Name
}

abstract class Queue[S <: SQSServiceAux](service: S)(name: Name) extends QueueAux {
  
  type service = S
}

// TODO: this should be a case class
// with defaults for the default values for attrs
// http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/Query_QueryCreateQueue.html#Query_CreateQueue_RequestParameters
// maybe records here instead of case classes
case class QueueState[Q <: QueueAux](queue: Q)(
  maxMssgSize: Int = 65536,
  delaySeconds: Int = 0,
  mssgRetentionPeriod: Int = 345600,
  visibilityTimeout: Int = 30
) extends StateOf[Q](queue) {}

// then, provide concrete class for Queue
// with package-private constructor
// so that it can only be instantiated through a service.
// this should be an internal rep that the API user will never see
// modifying attributes for example, should be done through the service
// given a queue of the right type. This will return a QueueState or something similar
// the idea is to wrap mutable attrs through StateOf[R <: Resource](r: R)
// why? this way
// 
// - resources are static
// - their state is not
// 
// which is as far as you can get without full dep types


