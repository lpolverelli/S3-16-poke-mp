package distributed.messages

/**
  * LivenessRequestMessage is used to inform clients of acknowledge their presence
  */
trait LivenessRequestMessage {
}

object LivenessRequestMessage {
  def apply(): LivenessRequestMessage = new LivenessRequestMessageImpl()
}

class LivenessRequestMessageImpl extends LivenessRequestMessage
