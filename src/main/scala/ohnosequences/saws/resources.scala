package ohnosequences.saws

import shapeless._

// a good rule of thumb for whether something should be a resource:
// it has an ARN? if not...
trait AnyResource {  
  type Service <: AnyService
  val service: Service
  val arn: ARN
}

trait ARN

object AnyState {
  type of[R <: AnyResource] = AnyStateOf {  type Resource = R  }
}
trait AnyStateOf {  
  type Resource <: AnyResource
  val resource: Resource
}
abstract class StateOf[R <: AnyResource](r: R) {  
  type Resource = R
  val resource = r
}

// just experimenting...
trait AnyAction {

  type Input  <: AnyResource
  type Output <: AnyResource

  type InputState   <: AnyState.of[Input]
  // the point here is that you can match on both 
  // - the "standard" output
  // - the action-specific errors (if any)
  type OutputState  <: AnyState.of[Output] :+: Errors :+: CNil

  // signal those outputs that are considered errors
  type Errors <: AnyState.of[Output]

  // needed?
  // type Handler

  def act(r: Input, s: InputState): (OutputState, Output)
}

// object AnyPolyAction {

//   type Resources <: HList
//   type StateFrom[Rs <: Resources]
// }
// trait AnyPolyAction {

//   import AnyPolyAction._
//   type Input  <: Resources
//   type Output <: Resources

//   type InputState   <: StateFrom[Input]
//   type OutputState  <: StateFrom[Output] :+: Errors :+: CNil
//   // the point here is that you can match on both 
//   // - the "standard" output
//   // - the action-specific errors (if any)

//   // signal those outputs that are considered errors
//   type Errors <: StateFrom[Output]

//   // needed?
//   // type Handler
//   def act(r: Input, s: InputState): (OutputState, Output)
// }
