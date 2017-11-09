package controllers

import javax.inject.Inject

import play.api.Configuration
import play.api.mvc.{Action, Controller}

class Application @Inject()(val configuration: Configuration) extends Controller {

  def index = Action { implicit request =>
    Redirect(routes.Application.welcome())
  }

  def welcome = Action { implicit request =>
    Ok(views.html.users.welcome())
  }
}
