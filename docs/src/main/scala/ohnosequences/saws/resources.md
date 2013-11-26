### Index

+ src
  + test
    + scala
      + ohnosequences
        + saws
          + dynamodb
            + [items.scala](../../../../test/scala/ohnosequences/saws/dynamodb/items.md)
            + [tables.scala](../../../../test/scala/ohnosequences/saws/dynamodb/tables.md)
            + [services.scala](../../../../test/scala/ohnosequences/saws/dynamodb/services.md)
  + main
    + scala
      + ohnosequences
        + [saws.scala](../saws.md)
        + experiments
        + saws
          + sqs
            + [queues.scala](sqs/queues.md)
            + [service.scala](sqs/service.md)
            + [messages.scala](sqs/messages.md)
          + [accounts.scala](accounts.md)
          + [regions.scala](regions.md)
          + [users.scala](users.md)
          + dynamodb
            + [package.scala](dynamodb/package.md)
            + [tables.scala](dynamodb/tables.md)
            + [attributes.scala](dynamodb/attributes.md)
            + [services.scala](dynamodb/services.md)
          + [typeops.scala](typeops.md)
          + [resources.scala](resources.md)
          + [services.scala](services.md)
          + ec2
            + [service.scala](ec2/service.md)

------


```scala
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

object AnyARN {}
trait AnyARN {}

object AnyState {

  type of[R <: AnyResource] = AnyState { type Resource = R }
}
trait AnyState {  
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
```

  Maybe this should go somewhere else; then a particular service could
  
  1. declare a list of actions that can perform, contained in the list of global actions
  2. implement them by providing a type class for each action `service.do(CreateQueue)` or `service please CreateQueue`

  This type class could be generic: `Exec[S <: AnySerice, A <: AnyAction: From[S]#is](s: S, a: A)`
  The abstract `please` method will just require that for `S = this.type` and `A` the particular action; an implicit not found means that your service does not have that action available.

  One way of making client code life easier would be to add a bound on `A` so as to be one of valid actions for that service.

  Looks like a really nice idea!

  What could suppose a problem? the types of resources. 
  The service implementation should be generic, with "syntax" methods wiring the particular action/s.


```scala
  // def act(r: Input, s: InputState): (OutputState, Output)
}

trait AnyImplementation {

  type Action <: AnyAction
  val action: Action
  type Service <: AnyService
  val service: Service

  def exec(r: action.Input, s: action.InputState): (action.OutputState, action.Output)
}
```

  
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



```scala
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

```

