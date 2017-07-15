package distributed

import model.environment.Coordinate

trait User {
  def userId: Int

  def username: String

  def idImage: Int

  def position: Coordinate
}

object User {
  def apply(userId: Int, username: String, idImage: Int, position: Coordinate): User =
    new UserImpl(userId, username, idImage, position)
}

class UserImpl(override val userId: Int, override val username: String,
               override val idImage: Int, override val position: Coordinate) extends User
