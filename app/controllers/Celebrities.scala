package controllers

import models.Celebrity
import services.CelebritiesDAO

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


  /** list all celebrities */
  def index = Action.async { implicit request =>

    CelebritiesDAO.all.map { celebrities => Ok(Json.toJson(celebrities))} // convert it to a JSON and return it

  }

  /** create a celebrity from the given JSON */
  def create() = Action.async(parse.json) { request =>

    val nameJSON = request.body.\("name")
    val name = nameFormat.reads(nameJSON).get
    val website = request.body.\("website").toString().replace("\"", "")
    val bio = request.body.\("bio").toString().replace("\"", "")
    val celebrity = Celebrity(Option(BSONObjectID.generate), name, website, bio) // create the celebrity
    CelebritiesDAO.create(celebrity).map(
      _ => Ok(Json.toJson(celebrity))) // return the created celebrity in a JSON

  }

  def find(name: String) = Action.async(parse.empty) { request =>
    CelebritiesDAO.find(name).map { celebrities => Ok(Json.toJson(celebrities))} // convert it to a JSON and return it
  }

  /** retrieve the celebrity for the given id as JSON */
  def show(id: String) = Action.async(parse.empty) { request =>
    CelebritiesDAO.show(id).map { celebrity => Ok(Json.toJson(celebrity))}
  }

  /** update the celebrity for the given id from the JSON body */
  def update(id: String) = Action.async(parse.json) { request =>

    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    val nameJSON = request.body.\("name")
    val name = nameFormat.reads(nameJSON).get
    val website = request.body.\("website").toString().replace("\"", "")
    val bio = request.body.\("bio").toString().replace("\"", "")

    val celebrity = new Celebrity(Option(objectID), name, website, bio)
    CelebritiesDAO.update(celebrity).map(
      _ => Ok(Json.toJson(Celebrity(Option(objectID), name, website, bio))))

  }

  /** delete a celebrity for the given id */
  def delete(id: String) = Action.async(parse.empty) { request =>
    CelebritiesDAO.delete(id).map(// remove the celebrity
      _ => Ok(Json.obj())).recover { case _ => InternalServerError}

  }
}
