package ohnosequences.saws

import shapeless._

// a good rule of thumb for whether something should be a resource:
// it has an ARN? if not...
trait AnyResource {

  type Service <: AnyService
  val service: Service

  type ARN <: AnyARN
  val arn: ARN
}

trait AnyARN

trait AnyState {  

  type Resource <: AnyResource
  val resource: Resource
}
object AnyState {

  type of[R <: AnyResource] = AnyState { type Resource = R }
  type StateOf[R <: AnyResource] = { type is = AnyState { type Resource <: R } }
}

// is this useful??
abstract class State[R <: AnyResource](r: R) {

  type Resource = R
  val resource = r
}