package services

import models.Activity
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.Cursor
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONRegex}

import scala.concurrent.Future

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 17:47
 */
object ActivitiesDAO {
  val collection: BSONCollection = ReactiveMongoPlugin.db.collection[BSONCollection]("activities")
  val pattern = "\\w+".r

  /**
   * list all activities
   * @return collection of activities
   */
  def all: Future[List[Activity]] = {

    // let's do our query
    val cursor: Cursor[Activity] = collection.
      // find all
      find(BSONDocument()).
      // sort by name
      sort(BSONDocument("name" -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[Activity]

    cursor.collect[List]()
  }

  /**
   * Find an activity by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of activities
   */
  def find(name: String): Future[List[Activity]] = {
    pattern.findFirstIn(name) match {
      case s: Some[String] => {
        val query =
          BSONDocument("name" -> BSONRegex("^" + s.get + ".*", "i"))
        val cursor: Cursor[Activity] = collection.find(query).
          // sort by name
          sort(BSONDocument("name" -> 1)).
          // perform the query and get a cursor of JsObject
          cursor[Activity]

        cursor.collect[List]()
      }
      case _ => Future(Nil)
    }


  }

  /**
   * Retrieve the activity for the given id as JSON
   * @param id the Json Id
   * @return
   */
  def show(id: String) = {
    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    // get the celebrity having this id (there will be 0 or 1 result)
    collection.find(BSONDocument("_id" -> objectID)).one[Activity]

  }

}
