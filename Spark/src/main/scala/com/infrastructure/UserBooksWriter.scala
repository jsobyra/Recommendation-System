package com.infrastructure

import com.api.Events.{Recommendation, UserBooks}
import reactivemongo.bson.{BSONDocument, BSONDocumentWriter}

object UserBooksWriter extends BSONDocumentWriter[UserBooks] {
  def write(userBook: UserBooks): BSONDocument =
    BSONDocument(
      "userBookId" -> userBook.id.value,
      "userId" -> userBook.userId.value,
      "bookId" -> userBook.bookId.toString
    )
}
