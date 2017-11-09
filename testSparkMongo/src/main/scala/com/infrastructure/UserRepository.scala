package com.infrastructure

import com.api.Events.User
import com.domain.UserId
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by user on 04.11.17.
  */
class UserRepository(db: DefaultDB)(implicit ec: ExecutionContext) {
  private val userServiceCollection: Future[BSONCollection] = Future {
    db("users")
  }

  implicit def requestReader: BSONDocumentReader[User] = UserReader

  def checkIfUserExists(userId: UserId): Boolean = {
    val query = BSONDocument("userId" -> userId.value)
    val response = userServiceCollection.flatMap(_.find(query).cursor[User]().collect[List]())

    val result = Await.result(response, 2 seconds)
    result.size == 1
  }
}
