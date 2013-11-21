# actions and services

What do I want here? In the abstract, we have

1. a `service` with a declared set of actions
2. Given a particular `action` which `service` implements, we want to execute that action using our service on a particular `input`

A possible design is `service.please(action(input))` or `service please action(input)`. This means

- first we bound the types of the action using `input`; we should get a nice singleton type at this stage
- then, we call the generic implementation that our service provides using its `please` method; there we can do all sort of checks if they're needed

This way `AnyAction` becomes something which describes the input (both types and values) and informs you about what to expect from it as output.

Also, we can now implement all actions as case classes! genericity is outside.

``` scala
def please[
  A <: AnyAction: Implemented[here]#is
](action: A)(implicit impl: Impl.for[action.type]): (action.OutputState, action.Output) = impl.exec(action)
```

### declaring service actions

Think of creating a queue; you need something like

``` scala
// and why not a trait without args?
// because the output types depend on the input queue type
case class CreateQueue[Q <: AnyQueue, S <: QueueState[Q]](val queue: Q, val state: S) extends AnyCreateQueue {

  // all this needs to be moved to AnyCreateQueue!!
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
  // errors! possible due to open unions
  case class AlreadyCreated(queue: Input, state: InputState) 
    extends Errors { val resource = queue }
  case class RecentlyDeleted(queue: Input, state: InputState) 
    extends Errors { val resource = queue }
}
```

Note that there's not a single mention of a service here. Now, how can you say that your service provides this? An added difficulty is that what you return can be wrapped into an "execution context"; think of `Future[(action.Output, action.OutputState)]`. Given Scala limitations, this could work as a general pattern (if needed). Anyway, assuming that you want to return just the declared output type what you need is something from the action type to the corresponding output output state

``` scala
// the bound here is for getting the input types right
def exec[CQ <: AnyCreateQueueAction](val input: CQ): (input.Output, input.OutputState)
```

``` scala

```