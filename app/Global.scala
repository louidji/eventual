import play.api._
import play.api.mvc._
import play.filters.gzip.GzipFilter

/**
 * User: Louis TOURNAYRE
 * Date: 11/11/2014
 * Time: 23:21
 */


object Global extends WithFilters(new GzipFilter()) with GlobalSettings {
  // onStart, onStop etc...
}
