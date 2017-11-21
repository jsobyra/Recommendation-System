package com.infrastructure

import com.api.Events.User
import com.domain.UserId
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class UserRepository(db: DefaultDB)(implicit ec: ExecutionContext) {
  private val userServiceCollection: Future[BSONCollection] = Future {
    db("users")
  }

  implicit def requestReader: BSONDocumentReader[User] = UserReader
  implicit def requestWriter: BSONDocumentWriter[User] = UserWriter

  def checkIfUserCreated(email: String, password: String): Boolean = {
    val query = BSONDocument("email" -> email, "password" -> password)
    val response = userServiceCollection.flatMap(_.find(query).cursor[User]().collect[List]())

    val result = Await.result(response, 2 seconds)
    result.size == 1
  }

  def checkIfUserExists(email: String): Boolean = {
    val query = BSONDocument("email" -> email)
    val response = userServiceCollection.flatMap(_.find(query).cursor[User]().collect[List]())

    val result = Await.result(response, 2 seconds)
    result.size == 1
  }

  def registerUser(id: UserId, email: String, password: String) = {
    userServiceCollection.flatMap(_.insert(User(id, email, password, TimeProvider.now)).map(_ => {}))
    println("DONE!")
  }

  def getUserId(email: String, password: String): UserId = {
    val query = BSONDocument("email" -> email, "password" -> password)
    val response = userServiceCollection.flatMap(_.find(query).cursor[User]().collect[List]())

    val result = Await.result(response, 2 seconds)
    result match {
      case users: List[User] => users.head.id
      case _ => UserId("IncorrectId")
    }
  }

}

