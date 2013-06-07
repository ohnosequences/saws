
# resources and their state

In our approach, resources are always declared statically. However, there's a part of them which is transient, time-dependent (including their so to speak "physical" existence there at AWS). 

## resource states

Think of AutoScaling groups, for example. They have state in two seemingly different levels:

1. the group can be declared inside your code, but this doesn't tell you anything about whether it is there at AWS or not. So, you can get the information that this AutoScaling group was there, _at one particular point in time_.
2. once you know that this thing was there at some point, you can try to gather information about its particular transient state, and act on it (number of instances, etc)

It is thus critical to precisely identify what kind of things can we treat as existing at compile time, and what should instead be part of state. A simple criterion is: if you can modify it after creation, state; if not, resource.

### access to state

As state represents something happening at AWS, it is only natural to interact with it from a corresponding service instance. The context is then 

``` scala
// inside service
def action[R : ResourceOf[this.type]](res: R)(state: StateOf[res.type]): Wrapper[StateOf[res.type]]
```

In the end, for each concrete action implementation your types are completely bound _at the abstract level_; for example, you want to create a SQS queue.

### intrinsically transient resources

There are some stuff that is dynamic in itself, such as SQS messages. Even more, their attributes vary depending on their state: a received message has a receipt handle which lets you delete it from the the queue, something which you don't have when you post a message. The naive solution is to parametrize state:

``` scala
// common stuff for all states
class MessageState[M <: Message](val message: M, val id: String, val md5: String) {}

case class SentMessage[M <: Message](message: M)(id: String, md5: String) 
  extends MessageState[M](message, id, md5)
// here you have a receiptHandle
case class ReceivedMessage[M <: Message](message: M)(id: String, md5: String, receiptHandle: String, visibilityTimeout: Int)
  extends MessageState[M](message, id, md5) 
```

Then, at the service level methods require the right types for each action.

### state nesting

resources reference/include/depend/whatever on other resources, and thus it is natural to expect that you would/could get references to their state, nested within the state of another resource.

