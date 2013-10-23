
# wrapping the AWS SDK

It looks certainly possible. Some reminders for now

- what to do with `Future`s: we can generate them from blocking Java clients (using several such) or try to wrap the async-like client (see [AmazonSQSBufferedAsyncClient](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/sqs/buffered/AmazonSQSBufferedAsyncClient.html) for an example) using standard Scala futures; this old [SO question](http://stackoverflow.com/questions/11529145/how-do-i-wrap-a-java-util-concurrent-future-in-an-akka-future), or [this one](http://stackoverflow.com/questions/14890724/how-to-handle-java-futures-with-akka-actors?lq=1) could be helpful.
- the API is so ugly. I need to check what @evdokim and @laughedelic are doing at ohnosequences/aws-scala-tools.