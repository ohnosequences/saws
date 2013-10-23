package ohnosequences.saws.sqs

import ohnosequences.saws.{AnyResource, StateOf}


object QueueTypes {
  
  // bound this here once formats are available
  type Name = String
}

import QueueTypes._

// queue resource
// region etc are derived from service
trait AnyQueue extends AnyResource {
  
  type Service <: AnySQSService
  // maybe?
  val service: Service
  val name: Name
  
  val url = "http://"+ service.namespace +"."+ service.region.name +"."+ service.host +
            "/"+ service.account.id +"/"+ name

  val arn: String

}

abstract class Queue[S <: AnySQSService](val service: S)(val name: Name) extends AnyQueue {
  
  type Service = S
}

// TODO: this should be a case class
// with defaults for the default values for attrs
// http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/Query_QueryCreateQueue.html#Query_CreateQueue_RequestParameters
// maybe records here instead of case classes?
// should we include approxMssgCount and similar stuff here?

trait AnyQueueState {

  type Queue <: AnyQueue
  val queue: Queue
}
case class QueueState[Q <: AnyQueue](queue: Q)(
  maxMssgSize: Int = 65536,
  delaySeconds: Int = 0,
  mssgRetentionPeriod: Int = 345600,
  visibilityTimeout: Int = 30
) extends AnyQueueState {

  type Queue = Q
  // val queue = queue
}

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