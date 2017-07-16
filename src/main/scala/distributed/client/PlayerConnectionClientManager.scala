package distributed.client

import com.google.gson.Gson
import com.rabbitmq.client.{AMQP, Channel, DefaultConsumer, Envelope}
import distributed.{ConnectedUsers, ConnectedUsersImpl, User}
import model.entities.TrainerSprites
import model.environment.Coordinate
import utilities.Settings

object PlayerConnectionClientManager {

  private val gson: Gson = new Gson()

  private var userQueue: String = _

  private val channel: Channel = ClientConnection.connection.createChannel()
  channel.queueDeclare(Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, false, false, false, null)

  val consumer = new DefaultConsumer(channel) {

    override def handleDelivery(consumerTag: String,
                                envelope: Envelope,
                                properties: AMQP.BasicProperties,
                                body: Array[Byte]) {
      println(" [x] Received message")
      val message = new String(body, "UTF-8")
      val serverUsers: ConnectedUsers = gson.fromJson(message, classOf[ConnectedUsers])
      ConnectedUsersImpl.map.putAll(serverUsers.map)
      ConnectedUsersImpl.map.values() forEach (user => println(""+user.userId+ ""+user.username))
    }
  }
  channel.basicConsume(userQueue, true, consumer)

  def sendUserInformation(userId: Int, username: String, sprites: TrainerSprites, position: Coordinate): Unit = {
    userQueue = Settings.PLAYERS_CONNECTED_CHANNEL_QUEUE + userId
    channel.queueDeclare(userQueue, false, false, false, null)
    val user = User(userId, username, sprites, position)
    channel.basicPublish("", Settings.PLAYER_CONNECTION_CHANNEL_QUEUE, null, gson.toJson(user).getBytes("UTF-8"))
    println(" [x] Sent message")
  }

}
