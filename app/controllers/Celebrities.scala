package controllers

import models.Celebrity
import models.Name.nameFormat

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.Future
import reactivemongo.api.Cursor
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.api.libs.json._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONRegex, BSONDocument}

/*
 * Author: Sari Haj Hussein
 */

object Celebrities extends Controller with MongoController {
  val collection: BSONCollection = db[BSONCollection]("celebrities")

  /** list all celebrities */
  def index = Action { implicit request =>
    Async {
//      val cursor = collection.find(
//        BSONDocument(), BSONDocument()).cursor[Celebrity] // get all the fields of all the celebrities
//      val futureList = cursor.toList // convert it to a list of Celebrity
//
//      futureList.map { celebrities => Ok(Json.toJson(celebrities)) } // convert it to a JSON and return it
          val cursor = collection.find(BSONDocument()).cursor[Celebrity] // get all the fields of all the celebrities
          val futureList: Future[List[Celebrity]] = cursor.collect[List]() // convert it to a list of Celebrity

          futureList.map { celebrities => Ok(Json.toJson(celebrities)) } // convert it to a JSON and return it
        }
  }
  
  /** create a celebrity from the given JSON */
  def create() = Action(parse.json) { request =>
    Async {
      val nameJSON = request.body.\("name")
      val name = nameFormat.reads(nameJSON).get
      val website = request.body.\("website").toString().replace("\"", "")
      val bio = request.body.\("bio").toString().replace("\"", "")
      val celebrity = Celebrity(Option(BSONObjectID.generate), name, website, bio) // create the celebrity
      collection.insert(celebrity).map(
        _ => Ok(Json.toJson(celebrity))) // return the created celebrity in a JSON
    }
  }

  def find(name: String) = Action(parse.empty) { request =>
      Async {
        //val nameJSON = request.body.\("name")
        //val name = nameFormat.reads(nameJSON).get
        val query =
          BSONDocument("name.last" ->  BSONRegex("^" + name + ".*", "i"))
        val cursor = collection.find(
          query).sort(BSONDocument("name" -> 1)).cursor[Celebrity] // get all the fields of all the celebrities
        val futureList: Future[List[Celebrity]] = cursor.collect[List]() // convert it to a list of Celebrity
        futureList.map { celebrities => Ok(Json.toJson(celebrities)) } // convert it to a JSON and return it
      }
  }
  
  /** retrieve the celebrity for the given id as JSON */
  def show(id: String) = Action(parse.empty) { request =>
    Async {
      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      // get the celebrity having this id (there will be 0 or 1 result)
      val futureCelebrity = collection.find(BSONDocument("_id" -> objectID)).one[Celebrity]
      futureCelebrity.map { celebrity => Ok(Json.toJson(celebrity)) }
    }
  }
  
  /** update the celebrity for the given id from the JSON body */
  def update(id: String) = Action(parse.json) { request =>
    Async {
      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      val nameJSON = request.body.\("name")
      val name = nameFormat.reads(nameJSON).get
      val website = request.body.\("website").toString().replace("\"", "")
      val bio = request.body.\("bio").toString().replace("\"", "")
      val modifier = BSONDocument( // create the modifier celebrity
        "$set" -> BSONDocument(
          "name" -> name,
          "website" -> website,
          "bio" -> bio))
      collection.update(BSONDocument("_id" -> objectID), modifier).map(
        _ => Ok(Json.toJson(Celebrity(Option(objectID), name, website, bio)))) // return the modified celebrity in a JSON
    }
  }
  
  /** delete a celebrity for the given id */
  def delete(id: String) = Action(parse.empty) { request =>
    Async {
      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      collection.remove(BSONDocument("_id" -> objectID)).map( // remove the celebrity
        _ => Ok(Json.obj())).recover { case _ => InternalServerError } // and return an empty JSON while recovering from errors if any
    }
  }
}
