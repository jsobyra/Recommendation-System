package controllers

import javax.inject.Inject

import com.api.Events._
import com.domain.RecommendationId
import models.commmunication.ServicesCommunication
import play.api.Configuration
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

import scala.concurrent.duration._
import scala.concurrent.Await

class Users @Inject()(val configuration: Configuration) extends Controller {

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
    println(userLogged.asInstanceOf[UserLogged].id)
    val userBooks = Await.result(client.getUserBooks(userLogged.asInstanceOf[UserLogged].id), 20 seconds)
    val userRecommendation = Await.result(client.getUserRecommendation(userLogged.asInstanceOf[UserLogged].id), 20 seconds)
    Ok(views.html.users.userLogged(
      userLogged.asInstanceOf[UserLogged],
      userBooks.asInstanceOf[Books].books,
      userRecommendation.asInstanceOf[Recommendations].recommendations
    ))
    }
  }

  def register() = Action { implicit request => {
    val client = new ServicesCommunication("127.0.0.1:2552", "127.0.0.1:3000")
    val user = userForm.bindFromRequest()(request).get
    val userRegistered = Await.result(client.createUser(user.email, user.password), 10 seconds)
    println(userRegistered.asInstanceOf[UserCreated].id)
    val recommendationId = Await.result(client.createDefaultRecommendation(userRegistered.asInstanceOf[UserCreated].id), 20 seconds)
    println(recommendationId.asInstanceOf[RecommendationId].value)
    val userRecommendation = Await.result(client.getUserRecommendation(userRegistered.asInstanceOf[UserCreated].id), 20 seconds)
    println(userRecommendation.asInstanceOf[Recommendations].recommendations.length.toString)

    Ok(views.html.users.userCreated(
      userRegistered.asInstanceOf[UserCreated],
      userRecommendation.asInstanceOf[Recommendations].recommendations
    ))
    }
  }
}
