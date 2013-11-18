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

  /*
  Maybe this should go somewher else; then a particular service could
  
  1. declare a list of actions that can perform, contained in the list of global actions
  2. implement them by providing a type class for each action `service.do(CreateQueue)` or `service please CreateQueue`

  This type class could be generic: `Exec[S <: AnySerice, A <: AnyAction: From[S]#is](s: S, a: A)`
  The abstract `please` method will just require that for `S = this.type` and `A` the particular action; an implicit not found means that your service does not have that action available.

  One way of making client code life easier would be to add a bound on `A` so as to be one of valid actions for that service.

  Looks like a really nice idea!
  */
  // def act(r: Input, s: InputState): (OutputState, Output)
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
