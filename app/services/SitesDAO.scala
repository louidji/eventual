package services

import models.Location.LocationBSONWriter
import models.{Location, Site}
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.api.Cursor
import reactivemongo.bson.{BSONDocument, BSONRegex}

import scala.concurrent.Future

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 17:47
 */
object SitesDAO extends DataAccess[Site] {


  val collectionName = "sites"
  val DEFAULT_MAX = 5000

  def findFirst(name: String): Future[Option[Site]] =  {
    if (isWord(name))   {
      val query =
        BSONDocument("name" -> BSONRegex("^" + name + "$", "i"))
        collection.find(query).one[Site]
    } else {
      Future(None)
    }
  }

  /**
   * Find a site by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of activities
   */
  def find(name: String): Future[List[Site]] = {
    if (isWord(name)) {
      val query =
        BSONDocument("name" -> BSONRegex("^" + name + ".*", "i"))
      val cursor: Cursor[Site] = collection.find(query).
        // sort by name
        sort(BSONDocument("name" -> 1)).
        // perform the query and get a cursor of JsObject
        cursor[Site]

      cursor.collect[List]()
    } else {
      Logger.of("security").warn(s"Menace d'injection, la chaine '$name' contient des caractères non autorisés")
      Future(Nil)
    }


  }


  /**
   * Find nearest sites
   * @param location base point
   * @param max distance max
   * @return the nearest sites
   */
  def findNear(location: Location, max:Int): Future[List[Site]] = {

    val query =  BSONDocument(
      "loc" -> BSONDocument("$nearSphere" ->
        BSONDocument(
          "$geometry" -> LocationBSONWriter.write(location),
          "$maxDistance" -> (if (max > 0) max else DEFAULT_MAX)
        )
      )
    )

    val cursor: Cursor[Site] = collection.find(query).
      // perform the query and get a cursor of JsObject
      cursor[Site]

    cursor.collect[List]()

    //     EX
    //    db.sites.find ( { loc: {
    //      $nearSphere: {
    //      $geometry: { type: "Point", coordinates : [ -0.083333, 43.283333 ] },
    //      $minDistance:1000,
    //      $maxDistance:5000 }
    //    } } )



  }

}
