package services

import models.Site
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
object SitesDAO {
  val collection: BSONCollection = ReactiveMongoPlugin.db.collection[BSONCollection]("sites")

  val pattern = "\\w+".r

  /**
   * list all sites
   * @return collection of activities
   */
  def all: Future[List[Site]] = {

    // let's do our query
    val cursor: Cursor[Site] = collection.
      // find all
      find(BSONDocument()).
      // sort by name
      sort(BSONDocument("name" -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[Site]

    cursor.collect[List]()
  }

  /**
   * Find a site by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of activities
   */
  def find(name: String): Future[List[Site]] = {
    pattern.findFirstIn(name) match {
      case s: Some[String] => {
        val query =
          BSONDocument("name" -> BSONRegex("^" + s.get + ".*", "i"))
        val cursor: Cursor[Site] = collection.find(query).
          // sort by name
          sort(BSONDocument("name" -> 1)).
          // perform the query and get a cursor of JsObject
          cursor[Site]

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
    collection.find(BSONDocument("_id" -> new BSONObjectID(id))).one[Site]
  }

}
