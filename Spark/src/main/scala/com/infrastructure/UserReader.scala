package com.infrastructure

import java.util.Date

import com.api.Events.User
import com.domain.UserId
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

object UserReader extends BSONDocumentReader[User] {
  def read(bson: BSONDocument): User = {
    User(
      UserId(bson.getAs[String]("userId").get),
      bson.getAs[String]("email").get,
      bson.getAs[String]("password").get,
      bson.getAs[Date]("date").get
    )
  }
}
