package services

import models.Activity
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.api.{QueryOpts, Cursor}
import reactivemongo.bson.{BSONDocument, BSONRegex}

import scala.concurrent.Future

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 17:47
 */
object ActivitiesDAO extends DataAccess[Activity] {
  val collectionName = "activities"






  /**
   * Find an activity by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of activities
   */
  def find(name: String): Future[List[Activity]] = {

    if (conform(name)) {
      val query =
        BSONDocument("name" -> BSONRegex("^" + name + ".*", "i"))
      val cursor: Cursor[Activity] = collection.find(query).
        // sort by name
        sort(BSONDocument("name" -> 1)).
        // perform the query and get a cursor of JsObject
        cursor[Activity]

      cursor.collect[List]()
    } else {
      Logger.of("security").warn(s"Menace d'injection, la chaine '$name' contient des caractères non autorisés")
      Future(Nil)
    }
  }



}
