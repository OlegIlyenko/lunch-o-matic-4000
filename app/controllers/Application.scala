package controllers

import play.api._
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{JsString, JsError, Json}
import play.api.mvc._
import scaldi.Injector
import scaldi.Injectable._
import service.{Place, PlacesService}

class Application(implicit inj: Injector) extends Controller {

  val placesService = inject [PlacesService]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def placesToJson(places: List[Place]) = Json.arr(places map (p => Json.obj("name" -> p.name): JsValueWrapper): _*)

  def places = Action {
    Ok(placesToJson(placesService.listPlaces))
  }

  def deletePlace = Action { request =>
    request.body.asJson.map { json =>
      val JsString(place) = json \ "name"

      placesService.removePlace(Place(place))

      Ok("Done!")
    } getOrElse BadRequest("Expecting Json data")
  }

  def addPlace = Action { request =>
    request.body.asJson.map { json =>
      val JsString(place) = json \ "name"

      placesService.addPlace(Place(place))

      Ok("Done!")
    } getOrElse BadRequest("Expecting Json data")
  }

  def todaysPlaces = Action {
    Ok(placesToJson(placesService.todaysPlaces()))
  }

  def regeneratePlaces = Action {
    Ok(placesToJson(placesService.todaysPlaces(force = true)))
  }
}