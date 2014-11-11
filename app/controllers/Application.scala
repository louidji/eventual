package controllers

import play.api.mvc.{Action, Controller}

/*
 * Author: Sari Haj Hussein
 */

object Application extends Controller {
  /** serve the index page app/views/index.scala.html */
  def index(any: String) = Action {
    Ok(views.html.index())
  }
  


}


