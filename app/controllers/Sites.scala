package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import services.SitesDAO

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 17:47
 */
object Sites extends Controller {

  /**
   * list all sites
   * @return collection of sites
   */
  def all = Action.async { implicit request =>
    SitesDAO.all.map { sites => Ok(Json.toJson(sites))} // convert it to a JSON and return it
  }

  /**
   * Find a site by name like 'name%' ignoring case
   * @param name beginning of the name (non sensitive case)
   * @return collection of sites
   */
  def find(name: String) = Action.async(parse.empty) { request =>
    SitesDAO.find(name).map { sites => Ok(Json.toJson(sites))} // convert it to a JSON and return it
  }

  /**
   * Retrieve the site for the given id as JSON
   * @param id the Json Id
   * @return
   */
  def show(id: String) = Action.async(parse.empty) { request =>
    SitesDAO.show(id).map { site => Ok(Json.toJson(site))}
  }

}