package models

import play.api.libs.json.Json
import reactivemongo.bson._
// necessaire (implict ... Json.format)
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

/**
 * User: Louis TOURNAYRE
 * Date: 29/06/2014
 * Time: 16:51
 */
case class Activity(id: Option[BSONObjectID], name: String, activity: String, adress: String, zipcode: String,
                    city: String, country: String, latitude: Double, longitude: Double)

object Activity {
  implicit val activityFormat = Json.format[Activity]

  /** serialize a Activity into a BSON */
  implicit object ActivityBSONWriter extends BSONDocumentWriter[Activity] {
    def write(activity: Activity): BSONDocument =
      BSONDocument(
        "_id" -> activity.id.getOrElse(BSONObjectID.generate),
        "name" -> activity.name,
        "activity" -> activity.activity,
        "adress" -> activity.adress,
        "zipcode" -> activity.zipcode,
        "city" -> activity.city,
        "country" -> activity.country,
        "latitude" -> activity.latitude,
        "longitude" -> activity.longitude
      )
  }

  /** deserialize a Celebrity from a BSON */
  implicit object ActivityBSONReader extends BSONDocumentReader[Activity] {
    def read(doc: BSONDocument): Activity =
      Activity(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("activity").get,
        doc.getAs[String]("adress").get,
        doc.getAs[String]("zipcode").get,
        doc.getAs[String]("city").get,
        doc.getAs[String]("country").get,
        doc.getAs[Double]("latitude").get,
        doc.getAs[Double]("longitude").get)
  }

}
