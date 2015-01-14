import play.api.GlobalSettings
import scaldi._
import scaldi.play.{ControllerInjector, ScaldiSupport}
import service.{PlacesService, FilePlacesService}

class Services extends Module {
  bind [PlacesService] to new FilePlacesService
}

object Global extends GlobalSettings with ScaldiSupport {
  def applicationModule = new Services :: new ControllerInjector
}
