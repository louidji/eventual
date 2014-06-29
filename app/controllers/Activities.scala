package controllers

import models.Activity

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.Future
import reactivemongo.api.Cursor
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.api.libs.json._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONRegex, BSONDocument}

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 17:47
 */
object Activities extends Controller with MongoController {
  val collection: BSONCollection = db[BSONCollection]("activities")

  /**
   * list all activities
   * @return collection of activities
   */
  def all = Action.async { implicit request =>

    // let's do our query
    val cursor: Cursor[Activity] = collection.
      // find all
      find(BSONDocument()).
      // sort by name
      sort(BSONDocument("name" -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[Activity]


    val futureActivities: Future[List[Activity]] = cursor.collect[List]()
    futureActivities.map { activities => Ok(Json.toJson(activities))} // convert it to a JSON and return it

  }

  /**
   * Find an activity by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of activities
   */
  def find(name: String) = Action.async(parse.empty) { request =>
    val query =
      BSONDocument("name" -> BSONRegex("^" + name + ".*", "i"))
    val cursor: Cursor[Activity] = collection.find(query).
      // sort by name
      sort(BSONDocument("name" -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[Activity]

    val futureActivities: Future[List[Activity]] = cursor.collect[List]()
    futureActivities.map { activities => Ok(Json.toJson(activities))} // convert it to a JSON and return it


  }

  /**
   * Retrieve the activity for the given id as JSON
   * @param id the Json Id
   * @return
   */
  def show(id: String) = Action.async(parse.empty) { request =>
    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
  // get the celebrity having this id (there will be 0 or 1 result)
  val futureCelebrity = collection.find(BSONDocument("_id" -> objectID)).one[Activity]
    futureCelebrity.map { celebrity => Ok(Json.toJson(celebrity))}
  }

}
