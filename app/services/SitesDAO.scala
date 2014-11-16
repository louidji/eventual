package services

import models.Site
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

}
