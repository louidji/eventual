package controllers

import models.Celebrity
import models.Name.nameFormat
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import services.CelebritiesDAO

/*
 * Author: Sari Haj Hussein
 */

object Celebrities extends Controller {


  /** list all celebrities */
  def index = Action.async { implicit request =>

    CelebritiesDAO.all.map { celebrities => Ok(Json.toJson(celebrities))} // convert it to a JSON and return it

  }

  /** create a celebrity from the given JSON */
  def create() = Action.async(parse.json) { request =>

    val nameJSON = request.body.\("name")
    val name = nameFormat.reads(nameJSON).get
    val website = request.body.\("website").toString().replace("\"", "")

    val bio = (request.body \ "bio").asOpt[String].map { bio =>
      if (!bio.trim.isEmpty) Option(bio.replace("\"", "")) else None
    }.getOrElse {
      None
    }

    val celebrity = Celebrity(Option(BSONObjectID.generate), name, website, bio) // create the celebrity
    CelebritiesDAO.create(celebrity).map(
      _ => Ok(Json.toJson(celebrity))) // return the created celebrity in a JSON

  }

  def find(name: String) = Action.async(parse.empty) { request =>
    CelebritiesDAO.find(name).map { celebrities => Ok(Json.toJson(celebrities))} // convert it to a JSON and return it
  }

  /** retrieve the celebrity for the given id as JSON */
  def show(id: String) = Action.async(parse.empty) { request =>
    CelebritiesDAO.findById(id).map { celebrity => Ok(Json.toJson(celebrity))}
  }

  /** update the celebrity for the given id from the JSON body */
  def update(id: String) = Action.async(parse.json) { request =>

    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    val nameJSON = request.body.\("name")
    val name = nameFormat.reads(nameJSON).get
    val website = request.body.\("website").toString().replace("\"", "")
    val bio = (request.body \ "bio").asOpt[String].map { bio =>
      if (!bio.trim.isEmpty) Option(bio.replace("\"", "")) else None
    }.getOrElse {
      None
    }

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
