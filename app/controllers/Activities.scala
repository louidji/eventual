package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import services.ActivitiesDAO

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 17:47
 */
object Activities extends Controller {

  /**
   * list all activities
   * @return collection of activities
   */
  def all = Action.async { implicit request =>
    ActivitiesDAO.all.map { activities => Ok(Json.toJson(activities))} // convert it to a JSON and return it
  }

  /**
   * Find an activity by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of activities
   */
  def find(name: String) = Action.async(parse.empty) { request =>
    ActivitiesDAO.find(name).map { activities => Ok(Json.toJson(activities))} // convert it to a JSON and return it


  }

  /**
   * Retrieve the activity for the given id as JSON
   * @param id the Json Id
   * @return
   */
  def show(id: String) = Action.async(parse.empty) { request =>
    ActivitiesDAO.findById(id).map { activity => Ok(Json.toJson(activity))}
  }

}
