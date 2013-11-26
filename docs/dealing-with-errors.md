# dealing with errors

The idea is that coproducts can serve as good way of dealing with error handling.

``` scala
type Err <: Coproduct
type Y <: HList
def do(i: X): Err :+: Y
```

Now you can abstract over your errors (exceptions too of course) and such in an open way.

## _really_ WIP

A nice way of dealing with this _could_ be to encapsulate this into actions

``` scala
trait AnyAction { self =>

  type InputResource <: AnyResource
  type OutputResource <: AnyResource
  // we could need vals here or typeclasses to model type dependency
  type InputState   <: AnyStateOf {  type Resource <: self.InputResource   }
  type OutputState  <: AnyStateOf {  type resource <: self.OutputResource  }

  // local errors!
  type Errors <: AnyError

  // nonono make errors part of the state
  def act(r: InputResource, s: InputState): (OutputResource, OutputState :+: Errors)
}
// nonono make errors part of the state
class Exec[A <: AnyAction](action: A)(implicit handler: AnyHandler.of[action.Errors]) {

  def apply(r: action.InputResource, s: action.InputState): 
    (action.OutputResource, action.OutputState :+: handler.Out) = {
      (
        action.act(r,s)._1, 
        (id[OutputState] :+: handler)(action.act(r,s)._2)
      )
    }
}

trait AnyHandler {

  type Error <: AnyError
  type Out
  // handle it!
  def apply(err: Error): Out
}
```

Then you could plug a error handler in a type-dependent way, even implicitly!

### error handlers

Imagine that you try to create a queue, but the queue is there already. Now, should you care about it? the answer is of course _it depends_. If in your model the queue being there means something, then you probably want to do something with it. If not, you can just return the already existing queue. But the important principle here is to dissociate your queue (you really want that queue, right?) from its shadow there in the real, external world; either way (no pun intended), you can

1. return the queue, together with its state saying that the queue is there (the external part); the error handler decides!
2. return the queue, together with an _error_ describing what's going on; this error is indexed by the action, of course

> what follows is pretty important I think

Note the similarity in the _"return the queue"_ part! So a handler will have to deal with possible errors of that action, and will report them as part of what you can observe of the state. Or should this be decoupled from the state? Probably things are simpler _if_ we express it as part of the state, making it a coproduct there if needed. The challenge here is how to declare that this action can only produce these states as a result; if the corresponding generic state type is an ADT, a practical compromise could be to leave it _open_ and add constructors at the action level. This looks like a pretty good Scala-esque design.

## References

- [Exception handlers as extensible cases](http://people.cs.uchicago.edu/~blume/papers/aplas08-exceptions.pdf)
- [Open data types and open functions](http://www.andres-loeh.de/OpenDatatypes.pdf)
- [Learning Scalaz - Validation](http://eed3si9n.com/learning-scalaz/Validation.html)
- [Haskell is exceptionally unsafe](http://existentialtype.wordpress.com/2012/08/14/haskell-is-exceptionally-unsafe/)