package app

import distributed.server.{PlayerConnectionServerManager, ServerConnection}

object ServerMain extends App{

  ServerConnection.connection
  PlayerConnectionServerManager().start()

  //ServerConnection.close()

}
