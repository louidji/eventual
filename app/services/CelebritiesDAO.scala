package services

import models.Celebrity
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONRegex}
import reactivemongo.core.commands.LastError

import scala.concurrent.Future

/*
 * Author: Sari Haj Hussein
 */

object CelebritiesDAO extends DataAccess[Celebrity] {
  val collectionName = "celebrities"

  /** list all celebrities */
  def all: Future[List[Celebrity]] = {
    val cursor = collection.find(BSONDocument()).cursor[Celebrity] // get all the fields of all the celebrities
    cursor.collect[List]() // convert it to a list of Celebrity
  }



  def find(name: String): Future[List[Celebrity]] = {
    if (conform(name)) {
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
    val modifier = BSONDocument(// create the modifier celebrity
      "$set" -> BSONDocument(
        "name" -> celebrity.name,
        "website" -> celebrity.website,
        "bio" -> celebrity.bio))
    collection.update(BSONDocument("_id" -> celebrity.id.get.copy()), modifier)
  }


}
