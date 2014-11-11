package services


import models.Celebrity
import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * User: Louis TOURNAYRE
 * Date: 11/11/2014
 * Time: 15:45
 */
abstract class DataAccess[T] {

  val collectionName: String
  lazy val collection: BSONCollection = ReactiveMongoPlugin.db.collection[BSONCollection](collectionName)
  val pattern = "\\w+".r

  /**
   * Verifie que la chaine de texte ne contient pas un caractere "interdit"
   * @param param
   */
  def conform(param: String): Boolean = {
    pattern.findFirstIn(param) match {
      case s: Some[String] => s.get.equals(param)
      case _ => false
    }
  }

  /** retrieve the document for the given id */
  def findById(id: String)(implicit t: BSONDocumentReader[T]): Future[Option[T]] = {
    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    collection.find(BSONDocument("_id" -> objectID)).one[T]

  }

  /** create a document */
  def create(document: T)(implicit t: BSONDocumentWriter[T]) = {
    collection.insert(document)
  }

  /** delete a document for the given id */
  def delete(id: String): Future[LastError] = {
    val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    collection.remove(BSONDocument("_id" -> objectID))
  }

}
