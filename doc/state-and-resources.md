
# resources and their state

In our approach, resources are always declared statically. However, there's a part of them which is transient, time-dependent (including their so to speak "physical" existence there at AWS). 

## resource states

Think of AutoScaling groups, for example. They have state in two seemingly different levels:

1. the group can be declared inside your code, but this doesn't tell you anything about whether it is there at AWS or not. So, you can get the information that this AutoScaling group was there, _at one particular point in time_.
2. once you know that this thing was there at some point, you can try to gather information about its particular transient state, and act on it (number of instances, etc)

### state nesting

resources reference/include/depend/whatever on other resources, and thus it is natural to expect that you would/could get references to their state, nested within the state of another resource.

### access to state

As state represents something happening at AWS, it is only natural to interact with it from a corresponding service instance. The context is then 

``` scala
// inside service
def action[R : ResourceOf[this.type]](res: R)(state: StateOf[res.type]): Wrapper[StateOf[res.type]]
```

In the end, for each concrete action implementation your types are completely bound _at the abstract level_; for example, you want to create a SQS queue.