package models

import play.api.libs.json.Json
import reactivemongo.bson.Producer.nameValue2Producer
import reactivemongo.bson.{BSONArray, BSONDocument, BSONDocumentReader, BSONDocumentWriter}

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
        "type" -> "Point",
        "coordinates" -> BSONArray(loc.longitude, loc.latitude)
        )
  }

  /** deserialize a Location from a BSON */
  implicit object LocationBSONReader extends BSONDocumentReader[Location] {
    def read(doc: BSONDocument): Location = {
      val coordinates = doc.getAs[List[Double]]("coordinates").get
      Location(
        coordinates(0),
        coordinates(1)
      )
    }
  }
}