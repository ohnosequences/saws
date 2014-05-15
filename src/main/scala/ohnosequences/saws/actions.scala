package ohnosequences.saws

import shapeless._

// just experimenting...
trait AnyAction {

  // they will be the same, most likely
  type Input  <: AnyResource
  type Output <: AnyResource

  // no bounds here
  type InputState
  type OutputState

  /*
  Maybe this should go somewhere else; then a particular service could
  
  1. declare a list of actions that can perform, contained in the list of global actions
  2. implement them by providing a type class for each action `service.do(CreateQueue)` or `service please CreateQueue`

  This type class could be generic: `Exec[S <: AnySerice, A <: AnyAction: From[S]#is](s: S, a: A)`
  The abstract `please` method will just require that for `S = this.type` and `A` the particular action; an implicit not found means that your service does not have that action available.

  One way of making client code life easier would be to add a bound on `A` so as to be one of valid actions for that service.

  Looks like a really nice idea!

  What could suppose a problem? the types of resources. 
  The service implementation should be generic, with "syntax" methods wiring the particular action/s.
  */
  // def act(r: Input, s: InputState): (OutputState, Output)
}

trait AnyModifyResource extends AnyAction {

  import AnyState.StateOf

  type Input <: AnyResource
  type Output <: AnyResource

  type InputState <: StateOf[Input]#is
  // the point here is that you can match on both 
  // - the "standard" output
  // - the action-specific errors (if any)
  type OutputState  <: StateOf[Output]#is :+: Errors :+: CNil

  // signal those outputs that are considered errors
  type Errors <: StateOf[Output]#is
}

trait AnyModifyResourceState extends AnyModifyResource {

  type Output = Input
}

// TODO: make action input a nested class inside the action module. This way you get the abstract bounds in the enclosing scope
trait AnyFunnyAction {

  type Input <: AnyResource
  type InputState 

  class input[I <: Input, S <: InputState](val input: I, val state: S) {

    type Output
    type OutputState
  }
}
// after this, you can execute a generic action, which will be a singleton object
// this can be done using statika or just plain objects as in the record case

/*
  
  What do I want here? In the abstract, we have

  1. a `service` with a declared set of actions
  2. Given a particular `action` which `service` implements, we want to execute that action using our service on a particular `input`

  A possible design is `service.please(action(input))` or `service please action(input)`. This means

  - first we bound the types of the action using `input`; we should get a nice singleton type at this stage
  - then, we call the generic implementation that our service provides using its `please` method; there we can do all sort of checks if they're needed

  This way `AnyAction` becomes something which describes the input (both types and values) and informs you about what to expect from it as output.

  Also, we can now implement all actions as case classes! genericity is outside.

  def please[
    A <: AnyAction: Implemented[here]#is
  ](action: A)(implicit impl: Impl.for[action.type]): (action.OutputState, action.Output) = impl.exec(action)

*/

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
