package models.akkademy

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.api.Requests._

import scala.concurrent.duration._


class SClient(remoteAddress: String){
  private implicit val timeout = Timeout(30 seconds)
  private implicit val system = ActorSystem("PlaySystem")
  private val remoteDb = system.actorSelection(s"akka.tcp://RecommendationServiceSystem@$remoteAddress/user/recommendation_service")


  def logUser(email: String, password: String) = {
    remoteDb ? LogUser(email, password)
  }

  def createUser(email: String, password: String) = {
    remoteDb ? CreateUser(email, password)
  }

  def createDefaultRecommendation(userId: UserId) = {
    remoteDb ? CreateDefaultRecommendation(userId)
  }

  def createRecommendation(userId: UserId) = {
    remoteDb ? CreateRecommendation(userId)
  }

  def addBook(userId: UserId, bookId: BookId) = {
    remoteDb ? AddBook(userId, bookId)
  }
}