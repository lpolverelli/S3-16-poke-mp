package distributed.server

import java.util
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import com.google.gson.reflect.TypeToken
import com.google.gson.{Gson, GsonBuilder}
import com.rabbitmq.client._
import distributed._
import distributed.deserializers.UserMessageDeserializer
import distributed.messages.{ConnectedUsersMessage, UserMessage, UserMessageImpl}
import utilities.Settings

object PlayerLoginServerService {
  def apply(connection: Connection, connectedPlayers: ConcurrentMap[Int, Player]): CommunicationService = new PlayerLoginServerService(connection, connectedPlayers)
}

class PlayerLoginServerService(private val connection: Connection, private val connectedPlayers: ConcurrentMap[Int, Player]) extends CommunicationService {
  override def start(): Unit = {
    val channel: Channel = connection.createChannel
    channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        println("server: received new user")
        println(new String(body, "UTF-8"))
        val gson = new GsonBuilder().registerTypeAdapter(classOf[UserMessageImpl], UserMessageDeserializer).create()
        val userMessage = gson.fromJson(new String(body, "UTF-8"), classOf[UserMessageImpl])
        val user = userMessage.user

        val response = gson.toJson(ConnectedUsersMessage(connectedPlayers))
        channel.basicPublish("", Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + user.userId, null, response.getBytes("UTF-8"))
        println("server: send connected user")
        connectedPlayers.put(user.userId, user)

        channel.exchangeDeclare(Settings.NEW_PLAYER_EXCHANGE, "fanout")
        val newPlayerResponse = gson.toJson(userMessage)
        channel.basicPublish(Settings.NEW_PLAYER_EXCHANGE, "", null, newPlayerResponse.getBytes("UTF-8"))
        println("server: send new user")
      }
    }

    channel.basicConsume(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, true, consumer)
  }

}