package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.messages.LivenessReplyMessage
import utilities.Settings

trait CrashHandlerClientManager {
 def start()
}

object CrashHandlerClientManager {
  def apply(connection: Connection, userId: Int): CrashHandlerClientManager = new CrashHandlerClientManagerImpl(connection, userId)
}

class CrashHandlerClientManagerImpl(private val connection: Connection,
                                    private val userId: Int) extends CrashHandlerClientManager {

  override def start(): Unit = {
    val channel: Channel = connection.createChannel()
    val crashHandlerQueue = channel.queueDeclare.getQueue

    import Settings._
    channel.queueDeclare(Constants.CRASH_HANDLER_CHANNEL_QUEUE, false, false, false, null)

    channel.exchangeDeclare(Constants.LIVENESS_REQUEST_EXCHANGE, "fanout")
    channel.queueBind(crashHandlerQueue, Constants.LIVENESS_REQUEST_EXCHANGE, "")

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {

        val gson: Gson = new Gson()
        val message = gson toJson LivenessReplyMessage(userId)
        channel.basicPublish("", Constants.CRASH_HANDLER_CHANNEL_QUEUE, null, message.getBytes("UTF-8"))

      }
    }

    channel.basicConsume(crashHandlerQueue, true, consumer)
  }
}