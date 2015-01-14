package service

import java.io.{PrintWriter, File}
import java.text.SimpleDateFormat
import java.util.Date

import scaldi.Injector
import scaldi.Injectable._

import scala.io.Source
import scala.util.Random

case class Place(name: String)

trait PlacesService {
  def addPlace(p: Place): Unit
  def removePlace(p: Place): Unit
  def listPlaces: List[Place]
  def todaysPlaces(force: Boolean = false): List[Place]
}

class FilePlacesService(implicit inj: Injector) extends PlacesService {
  val folder = {
    val f = inject [File] ("places.folder")
    if (!f.exists()) f.mkdirs()
    f
  }

  def placesFile = new File(folder, "places.txt")
  def todaysFile = new File(folder, s"places-${new SimpleDateFormat("yyyy-MM-dd").format(new Date)}.txt")

  def addPlace(p: Place) =
    this.synchronized(writePlaces(placesFile, loadPlaces(placesFile) :+ p))

  def removePlace(p: Place) =
    this.synchronized(writePlaces(placesFile, loadPlaces(placesFile) filterNot (p ==)))

  def todaysPlaces(force: Boolean = false) = this.synchronized {
    if (!todaysFile.exists() || force) {
      val places = Random.shuffle(loadPlaces(placesFile))
      writePlaces(todaysFile, places)
      places
    } else loadPlaces(todaysFile)
  }

  def listPlaces = loadPlaces(placesFile)

  private def loadPlaces(file: File) =
    if (!file.exists) Nil else Source.fromFile(file).getLines().filterNot(_.trim.isEmpty).map(Place.apply).toList

  private def writePlaces(file: File, places: List[Place]) = {
    val pw = new PrintWriter(file)
    try places foreach(p => pw.println(p.name)) finally pw.close()
  }
}
