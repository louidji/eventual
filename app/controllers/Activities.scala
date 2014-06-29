package controllers

import models.Activity
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONRegex, BSONDocument}

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 17:47
 */
object Activities extends Controller with MongoController {
  val collection = db[BSONCollection]("activities")

  /**
   * list all activities
   * @return collection of activities
   */
  def all = Action { implicit request =>
    Async {
      val cursor = collection.find(
        BSONDocument(), BSONDocument()).cursor[Activity] // get all the fields of all the celebrities
      val futureList = cursor.toList // convert it to a list of Celebrity

      futureList.map { celebrities => Ok(Json.toJson(celebrities)) } // convert it to a JSON and return it
    }
  }

  /**
   * Find an activity by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of activities
   */
  def find(name: String) = Action(parse.empty) { request =>
    Async {
      //val nameJSON = request.body.\("name")
      //val name = nameFormat.reads(nameJSON).get
      val query =
        BSONDocument("name" ->  BSONRegex("^" + name + ".*", "i"))
      val cursor = collection.find(
        query).sort(BSONDocument("name" -> 1)).cursor[Activity] // get all the fields of all the celebrities
      val futureList = cursor.toList // convert it to a list of Celebrity
      futureList.map { activities => Ok(Json.toJson(activities)) } // convert it to a JSON and return it
    }
  }

  /**
   * Retrieve the activity for the given id as JSON
   * @param id the Json Id
   * @return
   */
  def show(id: String) = Action(parse.empty) { request =>
    Async {
      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      // get the celebrity having this id (there will be 0 or 1 result)
      val futureCelebrity = collection.find(BSONDocument("_id" -> objectID)).one[Activity]
      futureCelebrity.map { celebrity => Ok(Json.toJson(celebrity)) }
    }
  }

}
