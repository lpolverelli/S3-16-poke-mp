package distributed.server

import com.google.gson.Gson
import com.rabbitmq.client._
import distributed.ConnectedPlayers
import distributed.messages.{LivenessReplyMessageImpl, LivenessRequestMessage, PlayerLogoutMessage}
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
  /*
  TODO: creare copia dei giocatori connessi
   */
  private var snapshotConnectedPlayers = connectedPlayers.clone().asInstanceOf[ConnectedPlayers]

  /**
    * @inheritdoc
    */
  override def start(): Unit = {
    val channel = connection.createChannel

    import utilities.Settings._
    channel.queueDeclare(Constants.CRASH_HANDLER_CHANNEL_QUEUE, false, false, false, null)
    channel.exchangeDeclare(Constants.LIVENESS_REQUEST_EXCHANGE, "fanout")
    channel.exchangeDeclare(Constants.PLAYER_DISCONNECTED_EXCHANGE, "fanout")

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {

        val livenessReplyMessage = gson.fromJson(new String(body, "UTF-8"), classOf[LivenessReplyMessageImpl])
        livingPlayers = livingPlayers + livenessReplyMessage.userId
        println("received")
        livingPlayers foreach (key => println(key))
      }
    }

    channel.basicConsume(Constants.CRASH_HANDLER_CHANNEL_QUEUE, true, consumer)

    new Thread(() => {
      while(true) {
        sendLivenessRequest(channel)


        Thread sleep 10000

        println("check")
        connectedPlayers.getAll.keySet() forEach (key => println(key))

        snapshotConnectedPlayers.getAll.keySet() forEach (key => if (!livingPlayers.contains(key) && connectedPlayers.containsPlayer(key)) {
          connectedPlayers.remove(key)
          sendDisconnectedMessage(channel, key)
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
    snapshotConnectedPlayers = connectedPlayers.clone().asInstanceOf[ConnectedPlayers]
    val message = gson toJson LivenessRequestMessage
    channel.basicPublish(Constants.LIVENESS_REQUEST_EXCHANGE, "", null, message.getBytes("UTF-8"))
  }

  /**
    * Sends a PlayerLogoutMessage to all players connected
    * @param channel the channel through which the message is delivered
    * @param userId the id of the disconnected user
    */
  private def sendDisconnectedMessage(channel: Channel, userId: Int) = {
    val response = gson toJson PlayerLogoutMessage(userId)
    channel.basicPublish(Constants.PLAYER_DISCONNECTED_EXCHANGE, "", null, response.getBytes("UTF-8"))
  }
}
