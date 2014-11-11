package services



import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.{QueryOpts, Cursor}
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
   * check they are no escape key on the param
   * @param param
   */
  def conform(param: String): Boolean = {
    pattern.findFirstIn(param) match {
      case s: Some[String] => s.get.equals(param)
      case _ => false
    }
  }


  /**
   * list all documents without sort
   * @return collection of activities
   */
  def all(implicit t: BSONDocumentReader[T]): Future[List[T]] = {

    // let's do our query
    val cursor: Cursor[T] = collection.
      // find all
      find(BSONDocument()).
      // perform the query and get a cursor of JsObject
      cursor[T]

    cursor.collect[List]()
  }

  /**
   * list documents in the range [skip, skip + size] (for tabulation)
   * @param sortKey the name of the key (for asc ordering)
   * @param skip the number of document to skip
   * @param size the size of the windows doc
   *
   * @return
   */
  def range(sortKey: String, skip:Int, size: Int)(implicit t: BSONDocumentReader[T]): Future[List[T]] = {

    // let's do our query
    val cursor: Cursor[T] = collection.
      // find all
      find(BSONDocument()).
      // limit & skip
      options(QueryOpts().skip(skip).batchSize(size).exhaust).
      // sort (indexed key is very more efficient)
      sort(BSONDocument(sortKey -> 1)).
      // perform the query and get a cursor of JsObject
      cursor[T]

    cursor.collect[List]()

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
