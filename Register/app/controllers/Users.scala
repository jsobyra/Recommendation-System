package controllers

import javax.inject.Inject

import com.api.Events.{UserCreated, UserForm, UserLogged}
import com.api.Requests.{BookId, UserId}
import models.akkademy.SClient
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
    val client = new SClient("127.0.0.1:2552")
    val user = userForm.bindFromRequest()(request).get
    val future = Await.result(client.logUser(user.email, user.password), 10 seconds)
    Ok(views.html.users.userLogged(new UserLogged(
      future.asInstanceOf[UserLogged].id,
      future.asInstanceOf[UserLogged].token,
      future.asInstanceOf[UserLogged].time
    )));
    }
  }

  def register() = Action { implicit request => {
    val client = new SClient("127.0.0.1:3000")
    val user = userForm.bindFromRequest()(request).get
    val future = Await.result(client.createRecommendation(UserId("6533f835c0a64c5aa543b1015c729783")), 30 seconds)
    println(future.toString)
    Ok(views.html.users.userCreated(new UserCreated(future.asInstanceOf[UserCreated].id, future.asInstanceOf[UserCreated].time)))
    }
  }
}
