package ohnosequences.saws.sqs

trait MessageAux {

  type queue <: QueueAux
  val queue: queue

  // crappy type here
  val body: String
}

case class Message[Q <: QueueAux](val queue: Q)(val body: String){ type queue = Q }

class MessageState[M <: MessageAux](message: M)(id: String, md5: String)
case class SentMessage[M <: MessageAux](message: M)(
  id: String,
  md5: String
) extends MessageState[M](message)(id, md5)
// here you have a receiptHandle
case class ReceivedMessage[M <: MessageAux](message: M)(
  id: String, 
  md5: String, 
  visibilityTimeout: Int,
  receiptHandle: String
) extends MessageState[M](message)(id, md5)
