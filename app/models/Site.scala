package models

import play.Logger
import play.api.libs.json.Json
import reactivemongo.bson._
// necessaire (implict ... Json.format)
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

/**
 * User: Louis TOURNAYRE
 */
case class Site(id: Option[BSONObjectID], name: String, activity: String, adress: Option[String], zipcode: Option[String],
                    city: Option[String], country: Option[String], loc: Location)

object Site {
  implicit val siteFormat = Json.format[Site]


  implicit object SiteBSONWriter extends BSONDocumentWriter[Site] {
    def write(site: Site): BSONDocument =
      BSONDocument(
        "_id" -> site.id.getOrElse(BSONObjectID.generate),
        "name" -> site.name,
        "activity" -> site.activity,
        "adress" -> site.adress,
        "zipcode" -> site.zipcode,
        "city" -> site.city,
        "country" -> site.country,
        "loc" -> site.loc
      )
  }

  implicit object SiteBSONReader extends BSONDocumentReader[Site] {
    def read(doc: BSONDocument): Site =
      Site(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("activity").get,
        doc.getAs[String]("adress"),
        doc.getAs[String]("zipcode"),
        doc.getAs[String]("city"),
        doc.getAs[String]("country"),
        doc.getAs[Location]("loc").get
      )
  }

}
