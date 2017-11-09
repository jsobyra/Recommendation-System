package com.domain

import akka.actor.{Actor, Status}
import com.api.Events.{UserCreated, UserLogged}
import com.api.Requests.{CreateUser, LogUser}
import com.domain.UserService.UserIdProvider
import com.infrastructure.{TimeProvider, TokenProvider, UserRepository}

import scala.concurrent.Future


class UserActor(userIdProvider: UserIdProvider, userRepository: UserRepository, tokenProvider: TokenProvider) extends Actor {

  override def receive = {
    case LogUser(email, password) => logUser(email, password)
    case CreateUser(email, password) => createUser(email, password)
    case o => sender ! UnknownRequest(o.toString)
  }

  private def logUser(email: String, password: String) = {
    validateUserCredentials(email, password)
    if(checkIfUserCreated(email, password))
      sender ! UserLogged(userIdProvider.getUserId(email, password), tokenProvider.createToken(userIdProvider.getUserId(email, password).value), TimeProvider.now)
    else sender ! ResourceDoesNotExist
  }

  private def createUser(email: String, password: String) = {
    validateUserCredentials(email, password)
    if(!checkIfUserExists(email)) {
      val userId = userIdProvider.newId
      registerUser(userId, email, password)
      sender ! UserCreated(userId, TimeProvider.now)
    }
    else sender ! ResourceAlreadyExists
  }

  private def validateUserCredentials(email: String, password: String) = {
    if(password.isEmpty) sender ! Status.Failure(EmptyPassword)
    else if(email.isEmpty) sender ! Status.Failure(EmptyEmail)
    else if("""(\w+)@([\w\.]+)""".r.unapplySeq(email).isEmpty) sender ! Status.Failure(IncorrectEmail)
  }

  private def checkIfUserCreated(email: String, password: String): Boolean = userRepository.checkIfUserCreated(email, password)

  private def checkIfUserExists(email: String): Boolean = userRepository.checkIfUserExists(email)

  private def registerUser(id: UserId, email: String, password: String) = userRepository.registerUser(id, email, password)
}
