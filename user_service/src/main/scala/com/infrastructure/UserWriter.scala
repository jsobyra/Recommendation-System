package com.infrastructure

import com.api.Events.User
import reactivemongo.bson.{BSONDocument, BSONDocumentWriter}

object UserWriter extends BSONDocumentWriter[User] {
  def write(user: User): BSONDocument =
    BSONDocument(
      "userId" -> user.id.value,
      "email" -> user.email,
      "password" -> user.password,
      "date" -> user.time
    )
}
