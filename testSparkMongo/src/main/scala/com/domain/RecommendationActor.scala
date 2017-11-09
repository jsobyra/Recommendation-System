package com.domain

import akka.actor.Actor
import com.api.Requests.{AddBook, CreateDefaultRecommendation, CreateRecommendation}
import com.infrastructure.{SparkWorker, UserRepository}
import org.bson.types.ObjectId


class RecommendationActor(userRepository: UserRepository, sparkWorker: SparkWorker) extends Actor{

  override def receive = {
    case AddBook(userId: UserId, bookId: BookId) => addBook(userId, bookId)
    case CreateDefaultRecommendation(userId: UserId) => createDefaultRecommendation(userId)
    case CreateRecommendation(userId: UserId) => createRecommendation(userId)
    case o => sender() ! UnknownRequest(o.toString)
  }

  private def createDefaultRecommendation(userId: UserId) = {
    if(checkIfUserExists(userId)) sender() ! sparkWorker.createDefaultRecommendation(userId)
    else sender() ! ResourceDoesNotExist
  }

  private def createRecommendation(userId: UserId) = {
    if(checkIfUserExists(userId)) sender() ! sparkWorker.createRecommendation(userId)
    else sender() ! ResourceDoesNotExist
  }

  private def checkIfUserExists(userId: UserId): Boolean = userRepository.checkIfUserExists(userId)

  private def addBook(userId: UserId, bookId: BookId) = {
    sender() ! sparkWorker.addBook(userId, bookId)
  }
}
