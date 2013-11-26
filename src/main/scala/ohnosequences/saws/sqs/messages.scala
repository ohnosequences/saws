package ohnosequences.saws.sqs

// should this be a resource? NO, no ARNs for messages
object AnyMessage {
  type from[Q <: AnyQueue] = AnyMessage { type Queue = Q }
}
trait AnyMessage {

  type Queue <: AnyQueue
  val queue: Queue

  // crappy type here
  val body: String
}

case class Message[Q <: AnyQueue](val queue: Q)(val body: String) extends AnyMessage {
 type Queue = Q 
} 


sealed trait AnyMessageState {

  type Message <: AnyMessage
  
  val message: Message
  val id: String
  val md5: String
}
  case class SentMessage[M <: AnyMessage](
    val message: M,
    val id: String,
    val md5: String
  ) extends AnyMessageState {  type Message = M  }
  // here you have a receiptHandle
  case class ReceivedMessage[M <: AnyMessage](
    val message: M,
    val id: String, 
    val md5: String, 
    val visibilityTimeout: Int,
    val receiptHandle: String
  ) extends AnyMessageState {  type Message = M  }

// missing some state here?
