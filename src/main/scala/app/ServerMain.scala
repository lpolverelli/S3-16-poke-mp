package app

import distributed.{ConnectedPlayers, DistributedConnectionImpl}
import distributed.server._

object ServerMain extends App{
  val connection = DistributedConnectionImpl().connection
  val connectedPlayers = ConnectedPlayers()

  CommunicationService(CommunicationService.Service.PlayerLogin, connection, connectedPlayers).start()
  CommunicationService(CommunicationService.Service.PlayerPosition, connection, connectedPlayers).start()
  CommunicationService(CommunicationService.Service.PlayerInBuilding, connection, connectedPlayers).start()
  CommunicationService(CommunicationService.Service.PlayerLogout, connection, connectedPlayers).start()
  CommunicationService(CommunicationService.Service.PlayerIsBusy, connection, connectedPlayers).start()
  CommunicationService(CommunicationService.Service.CrashHandler, connection, connectedPlayers).start()
}
