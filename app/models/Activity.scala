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
case class Activity(id: Option[BSONObjectID], name: String, activity: String, adress: Option[String], zipcode: Option[String],
                    city: Option[String], country: Option[String], latitude: Double, longitude: Double)

object Activity {
  implicit val activityFormat = Json.format[Activity]


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

  implicit object ActivityBSONReader extends BSONDocumentReader[Activity] {
    def read(doc: BSONDocument): Activity =
      Activity(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[String]("activity").get,
        doc.getAs[String]("adress"),
        doc.getAs[String]("zipcode"),
        doc.getAs[String]("city"),
        doc.getAs[String]("country"),
        convertToDouble(doc.getAs[BSONValue]("latitude")).get,
        //doc.getAs[Double]("latitude").get,
        convertToDouble(doc.getAs[BSONValue]("longitude")).get)
  }

  /**
   * Convert an Int, String or Double to the Double (Mongo save Data without type, 46.0 is save an Integer)
   * @param value Base Value in BSON encoding
   * @return Double Value
   */
  def convertToDouble(value: Option[BSONValue]): Option[Double] = value match {
    case someValue: Some[BSONValue] => someValue.get match  {
      case aInt: BSONInteger=> Option(aInt.value.toDouble)
      case aDouble: BSONDouble => Option(aDouble.value)
      case sString: BSONString => Option(sString.value.toDouble)
    }

    case _ => None
  }


}
