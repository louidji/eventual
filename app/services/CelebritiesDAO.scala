package services

import models.Celebrity
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.bson.{BSONDocument, BSONRegex}

import scala.concurrent.Future

/*
 * Author: Sari Haj Hussein
 */

object CelebritiesDAO extends DataAccess[Celebrity] {
  val collectionName = "celebrities"




  def find(name: String): Future[List[Celebrity]] = {
    if (isWord(name)) {
      val query = BSONDocument("name.last" -> BSONRegex("^" + name + ".*", "i"))
      val cursor = collection.find(query).sort(BSONDocument("name" -> 1)).cursor[Celebrity] // get all the fields of all the celebrities
      cursor.collect[List]() // convert it to a list of Celebrity
    } else {
      Logger.of("security").warn(s"Menace d'injection, la chaine '$name' contient des caractères non autorisés")
      Future(Nil)
    }


  }

  /** update the celebrity for the given id from the JSON body */
  def update(celebrity: Celebrity) = {
    val modifier = if (celebrity.bio.isDefined)
      BSONDocument(// create the modifier celebrity
        "$set" -> BSONDocument(
          "name" -> celebrity.name,
          "website" -> celebrity.website,
          "bio" -> celebrity.bio))
    else
      BSONDocument(
        "$set" -> BSONDocument(
          "name" -> celebrity.name,
          "website" -> celebrity.website),
        "$unset" -> BSONDocument(
          "bio" -> 1))

    collection.update(BSONDocument("_id" -> celebrity.id.get), modifier)
  }


}
