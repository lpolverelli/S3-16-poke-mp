package distributed.server

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.messages.{LivenessReplyMessage, LivenessRequestMessage, PlayerLogoutMessage}
import utilities.Settings.Constants

import scala.collection.immutable.HashSet

object CrashHandlerServerService {
  def apply(connection: Connection, connectedPlayers: ConnectedPlayers): CrashHandlerServerService =
    new CrashHandlerServerService(connection,connectedPlayers)
}

/**
  * CrashHandlerServerService checks if there are players who disconnected unexpectedly, if so it sends all connected players
  * that someone has disconnected
  * @param connection instance of the connection to the RabbitMQ Broker
  * @param connectedPlayers all the players currently connected as ConnectedPlayers
  */
class CrashHandlerServerService(private val connection: Connection,
                               private val connectedPlayers: ConnectedPlayers) extends CommunicationService {
  private var livingPlayers = HashSet[Int]()
  private val gson = new Gson()

  /**
    * @inheritdoc
    */
  override def start(): Unit = {
    val channel = connection.createChannel

    import utilities.Settings._
    channel.queueDeclare(Constants.CRASH_HANDLER_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {

        val livenessReplyMessage = gson.fromJson(new String(body, "UTF-8"), classOf[LivenessReplyMessage])
        livingPlayers = livingPlayers + livenessReplyMessage.userId
      }
    }

    channel.basicConsume(Constants.CRASH_HANDLER_CHANNEL_QUEUE, true, consumer)

    new Thread(() => {
      while(true) {
        sendLivenessRequest(channel)


        Thread sleep 30000

        println("check")
        connectedPlayers.getAll.keySet() forEach (key => println(key))

        connectedPlayers.getAll.keySet() forEach (key => if (!livingPlayers.contains(key)) {
          connectedPlayers.remove(key)
          sendLogoutMessage(channel, key)
        })
      }
    }).start()

  }

  /**
    * Sends a LivenessRequestMessage to all players connected
    * @param channel the channel through which the message is delivered
    */
  private def sendLivenessRequest(channel: Channel) = {
    livingPlayers = livingPlayers.empty
    channel.exchangeDeclare(Constants.LIVENESS_REQUEST_EXCHANGE, "fanout")
    val message = gson toJson LivenessRequestMessage
    channel.basicPublish(Constants.LIVENESS_REQUEST_EXCHANGE, "", null, message.getBytes("UTF-8"))
  }

  /**
    * Sends a PlayerLogoutMessage to all players connected
    * @param channel the channel through which the message is delivered
    * @param userId the id of the disconnected user
    */
  private def sendLogoutMessage(channel: Channel, userId: Int) = {
    channel.exchangeDeclare(Constants.PLAYER_LOGOUT_EXCHANGE, "fanout")
    val response = gson toJson PlayerLogoutMessage(userId)
    channel.basicPublish(Constants.PLAYER_LOGOUT_EXCHANGE, "", null, response.getBytes("UTF-8"))
  }
}
