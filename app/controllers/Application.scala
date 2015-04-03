package controllers

import play.api.mvc._
import org.joda.time.DateTime
import play.api._
import play.api.libs.json.Json

import scala.concurrent.Future

object Application extends Controller {
  implicit val personFormat = Json.format[Person]
  implicit val resultFormat = Json.format[PersonResult]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def ping = LoggingAction {
    request =>
      Ok(Json.obj("results" -> "success", "dateTime" -> DateTime.now()))
  }

  def hello(name : String) = Action {
    Ok(Json.obj("message" -> s"Hello $name"))
  }

  def savePerson = Action(parse.json[Person]) {
    request =>
      if (request.body.age > 21)
        Ok(Json.toJson(PersonResult(true, "Welcome to the party")))
      else
        Forbidden(Json.toJson(PersonResult(false, "You're much too young...")))
  }

}

object LoggingAction extends ActionBuilder[Request] {
  def invokeBlock[A](request : Request[A], block : (Request[A] => Future[Result])) = {
    Logger.info("Calling special method.")
    block(request)
  }
}

case class Person(firstName : String, lastName : String, age : Int)
case class PersonResult(success : Boolean, message : String)