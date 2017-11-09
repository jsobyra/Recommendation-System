package com.infrastructure

import com.api.Events.{Recommendation, UserBooks}
import com.domain.{UserBookId, UserId}
import org.bson.types.ObjectId
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

object UserBooksReader extends BSONDocumentReader[UserBooks] {
  def read(bson: BSONDocument): UserBooks = {
    UserBooks(
      UserBookId(bson.getAs[String]("userBookId").get),
      UserId(bson.getAs[String]("userId").get),
      new ObjectId(bson.getAs[String]("bookId").get)
    )
  }
}
