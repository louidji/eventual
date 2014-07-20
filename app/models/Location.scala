package models

import play.api.libs.json.Json
import reactivemongo.bson.Producer.nameValue2Producer
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

/**
 * User: Louis TOURNAYRE
 */
case class Location(longitude: Double, latitude: Double)

object Location {
  /** serialize/deserialize a Name into/from JSON value */
  implicit val locationFormat = Json.format[Location]

  /** serialize a Location into a BSON */
  implicit object LocationBSONWriter extends BSONDocumentWriter[Location] {
    def write(loc: Location): BSONDocument =
      BSONDocument(
        "lon" -> loc.longitude,
        "lat" -> loc.latitude)
  }

  /** deserialize a Location from a BSON */
  implicit object LocationBSONReader extends BSONDocumentReader[Location] {
    def read(doc: BSONDocument): Location =
      Location(
        doc.getAs[Double]("lon").get,
        doc.getAs[Double]("lat").get)
  }
}