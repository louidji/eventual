package controllers

import java.io.File

import play.Play
import play.api.mvc.{Action, Controller}

/*
 * Author: Sari Haj Hussein
 */

object Application extends Controller {

  lazy val projectRoot = Play.application().path()
  /** serve the index page app/views/index.scala.html */
  def index(any: String) = Action {
    Ok(views.html.index())
  }
  
//  /** resolve "any" into the corresponding HTML page URI */
//  def getURI(any: String): String = any match {
//    case "main" => "/public/html/main.html"
//    case "detail" => "/public/html/detail.html"
//    case "search" => "/public/html/search.html"
//    case _ => "error"
//  }
//
//
//  /** load an HTML page from public/html */
//  def loadPublicHTML(any: String) = Action {
//    val file = new File(projectRoot + getURI(any))
//    if (file.exists())
//      Ok(scala.io.Source.fromFile(file.getCanonicalPath()).mkString).as("text/html");
//    else
//      NotFound
//  }

}


