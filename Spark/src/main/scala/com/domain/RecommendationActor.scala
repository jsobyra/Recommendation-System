package com.domain

import akka.actor.Actor
import com.api.Requests._
import com.infrastructure.{SparkWorker, UserRepository}


class RecommendationActor(userRepository: UserRepository, sparkWorker: SparkWorker) extends Actor{

  override def receive = {
    case AddBook(userId: UserId, bookId: BookId) => addBook(userId, bookId)
    case GetUserBooks(userId: UserId) => getUserBooks(userId)
    case GetUserRecommendation(userId: UserId) => getUserRecommendation(userId)
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

  private def getUserBooks(userId: UserId) = {
    sender() ! sparkWorker.getUserBooks(userId)
  }

  private def getUserRecommendation(userId: UserId) = {
    sender() ! sparkWorker.getUserRecommendation(userId)
  }
}
