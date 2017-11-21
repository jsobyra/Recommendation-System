package models.commmunication

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.api.Requests._
import com.domain.RecommendationId

import scala.concurrent.duration._


class ServicesCommunication(remoteAddressUserService: String, remoteAddressRecommendationService: String){
  private implicit val timeout = Timeout(30 seconds)
  private implicit val system = ActorSystem("PlaySystem")
  private val userService = system.actorSelection(s"akka.tcp://UserServiceSystem@$remoteAddressUserService/user/user_service")
  private val recommendationService = system.actorSelection(s"akka.tcp://RecommendationServiceSystem@$remoteAddressRecommendationService/" +
    s"user/recommendation_service")


  def logUser(email: String, password: String) = {
    userService ? LogUser(email, password)
  }

  def createUser(email: String, password: String) = {
    userService ? CreateUser(email, password)
  }

  def createDefaultRecommendation(userId: UserId) = {
    recommendationService ? CreateDefaultRecommendation(userId)
  }

  def createRecommendation(userId: UserId) = {
    recommendationService ? CreateRecommendation(userId)
  }

  def addBook(userId: UserId, bookId: BookId) = {
    recommendationService ? AddBook(userId, bookId)
  }

  def getUserBooks(userId: UserId) = {
    recommendationService ? GetUserBooks(userId)
  }

  def getUserRecommendation(userId: UserId) = {
    recommendationService ? GetUserRecommendation(userId)
  }
}