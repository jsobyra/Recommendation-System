package controllers

import java.util.Date
import javax.inject.Inject

import com.api.Events._
import com.api.Requests.{BookId, UserId}
import com.domain.RecommendationId
import models.commmunication.ServicesCommunication
import play.api.Configuration
import play.api.cache._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

import scala.concurrent.duration._
import scala.concurrent.Await

class Users @Inject()(cache: CacheApi, val configuration: Configuration) extends Controller {

  def loginForm = Action { implicit request =>
    Ok(views.html.users.login())
  }

  def registerForm = Action { implicit request =>
    Ok(views.html.users.register())
  }

  val userForm = Form(mapping(
    "login" -> text,
    "password" -> text)(UserForm.apply)(UserForm.unapply))

  def login() = Action { implicit request => {
    val client = new ServicesCommunication("127.0.0.1:2552", "127.0.0.1:3000")
    val user = userForm.bindFromRequest()(request).get
    val userLogged = Await.result(client.logUser(user.email, user.password), 10 seconds)
    val userRecommendation = Await.result(client.getUserRecommendation(userLogged.asInstanceOf[UserLogged].id), 20 seconds)
    val userBooks = Await.result(client.getUserBooks(userLogged.asInstanceOf[UserLogged].id), 20 seconds)

    cache.set("user", userLogged)
    cache.set("books", userBooks.asInstanceOf[Books].books)
    cache.set("recommendations", userRecommendation.asInstanceOf[Recommendations].recommendations)

    Ok(views.html.users.userLogged(
      userLogged.asInstanceOf[UserLogged],
      userBooks.asInstanceOf[Books].books,
      userRecommendation.asInstanceOf[Recommendations].recommendations
    ))
    }
  }

  def addBook() = Action { implicit request => {
    val client = new ServicesCommunication("127.0.0.1:2552", "127.0.0.1:3000")
    val user = cache.get("user").get.asInstanceOf[UserLogged]
    val recommendations = cache.get("recommendations").get.asInstanceOf[List[Recommendation]]
    val userBookId = Await.result(client.addBook(user.id, BookId("59e239cfc828914e5afbcdbd")), 10 seconds)
    val userBooks = Await.result(client.getUserBooks(user.id), 20 seconds)

    cache.set("books", userBooks.asInstanceOf[Books].books)

    Ok(views.html.users.userLogged(
      user,
      userBooks.asInstanceOf[Books].books,
      recommendations
    ))
    }
  }

  def createRecommendation() = Action { implicit request => {
    val client = new ServicesCommunication("127.0.0.1:2552", "127.0.0.1:3000")
    val user = cache.get("user").get.asInstanceOf[UserLogged]
    val userBooks = cache.get("books").get.asInstanceOf[List[Book]]
    val recommendationId = Await.result(client.createRecommendation(user.id), 40 seconds)
    val userRecommendation = Await.result(client.getUserRecommendation(user.id), 20 seconds)
    cache.set("recommendations", userRecommendation.asInstanceOf[Recommendations].recommendations)

    Ok(views.html.users.userLogged(
      user,
      userBooks,
      userRecommendation.asInstanceOf[Recommendations].recommendations
    ))
  }
  }


  def register() = Action { implicit request => {
    val client = new ServicesCommunication("127.0.0.1:2552", "127.0.0.1:3000")
    val user = userForm.bindFromRequest()(request).get
    val userRegistered = Await.result(client.createUser(user.email, user.password), 10 seconds)
    val recommendationId = Await.result(client.createDefaultRecommendation(userRegistered.asInstanceOf[UserCreated].id), 20 seconds)

    Ok(views.html.users.welcome())
    }
  }
}
