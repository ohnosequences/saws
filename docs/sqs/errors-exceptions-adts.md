# errors, exceptions, ADTs

We should try to distinguish between errors which correspond to resources' state from those coming from the service (which could be treated as a resource too, at another level).

The SQS API has a list of common errors for all actions, which is supposed to be this:

- [AWS SQS API - Common Errors](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/Query_QueryErrors.html)

Some of them are not specific as far as I understand; `NonExistentQueue` looks like not general/common to me.

## action specific errors

You just need to look at the specific actions and see if there's something there.