package com

import akka.actor.{ActorSystem, Props}
import com.domain.RecommendationActor
import com.infrastructure.{Mongo, RecommendationRepository, BookRepository, SparkWorker, UserRepository}
import com.typesafe.config.ConfigFactory

object Main extends App {

  val config = ConfigFactory.load("application.conf")

  implicit val actorSystem = ActorSystem("RecommendationServiceSystem", config)
  implicit val dispatcher = actorSystem.dispatcher

  private val userRepository = new UserRepository(Mongo.mongoDb)
  private val recommendationRepository = new RecommendationRepository(Mongo.mongoDb)
  private val bookRepository = new BookRepository(Mongo.mongoDb)
  private val sparkWorker = new SparkWorker(userRepository, recommendationRepository, bookRepository)

  actorSystem.actorOf(Props(new RecommendationActor(userRepository, sparkWorker)), name = "recommendation_service")
}