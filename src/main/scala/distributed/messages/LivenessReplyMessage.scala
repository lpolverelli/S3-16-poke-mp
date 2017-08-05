package distributed.messages

/**
  * LivenessReplyMessage is used to inform the server that the client is alive
  */
trait LivenessReplyMessage {
  def userId: Int
}

object LivenessReplyMessage {
  def apply(userId: Int): LivenessReplyMessage = new LivenessReplyMessageImpl(userId)
}

/**
  * @inheritdoc
  * @param userId the sender's id
  */
class LivenessReplyMessageImpl(override val userId: Int) extends LivenessReplyMessage