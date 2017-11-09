package com

import akka.actor.{ActorSystem, Props}
import com.domain.UserActor
import com.infrastructure.{Mongo, TokenProvider, UUIDUserIdProvider, UserRepository}
import com.typesafe.config.Config

class Application(config: Config) {
  private implicit val actorSystem = ActorSystem("UserServiceSystem", config)
  private implicit val dispatcher = actorSystem.dispatcher

  private val tokenProvider = new TokenProvider(config)
  private val userRepository = new UserRepository(Mongo.mongoDb)
  private val userIdProvider = new UUIDUserIdProvider(userRepository)

  actorSystem.actorOf(Props(new UserActor(userIdProvider, userRepository, tokenProvider)), name = "userService1")
}
