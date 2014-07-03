package services

import models.Celebrity
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONRegex}
import reactivemongo.core.commands.LastError

import scala.concurrent.Future

/*
 * Author: Sari Haj Hussein
 */

object CelebritiesDAO {
  val collection: BSONCollection = ReactiveMongoPlugin.db.collection[BSONCollection]("celebrities")
  val pattern = "\\w+".r

  /** list all celebrities */
  def all: Future[List[Celebrity]] = {
    val cursor = collection.find(BSONDocument()).cursor[Celebrity] // get all the fields of all the celebrities
    cursor.collect[List]() // convert it to a list of Celebrity
  }

  /** create a celebrity */
  def create(celebrity: Celebrity) = {
    collection.insert(celebrity)
  }

  def find(name: String): Future[List[Celebrity]] = {
    pattern.findFirstIn(name) match {
      case s: Some[String] => {
        val query = BSONDocument("name.last" -> BSONRegex("^" + s.get + ".*", "i"))
        val cursor = collection.find(query).sort(BSONDocument("name" -> 1)).cursor[Celebrity] // get all the fields of all the celebrities
        cursor.collect[List]() // convert it to a list of Celebrity
      }
      case _ => Future(Nil)
    }


  }

  /** retrieve the celebrity for the given id */
  def show(id: String): Future[Option[Celebrity]] = {
    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    // get the celebrity having this id (there will be 0 or 1 result)
    collection.find(BSONDocument("_id" -> objectID)).one[Celebrity]

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

  /** delete a celebrity for the given id */
  def delete(id: String): Future[LastError] = {
    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    collection.remove(BSONDocument("_id" -> objectID))
  }
}
