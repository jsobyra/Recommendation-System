package com.infrastructure

import com.api.Events.{UserBooks}
import com.domain.{BookId, UserBookId, UserId}
import org.bson.types.ObjectId
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter}

import scala.concurrent.{ExecutionContext, Future}

class BookRepository(db: DefaultDB)(implicit ec: ExecutionContext) {
  private val bookServiceCollection: Future[BSONCollection] = Future {
    db("userBooks")
  }

  implicit def requestReader: BSONDocumentReader[UserBooks] = UserBooksReader
  implicit def requestWriter: BSONDocumentWriter[UserBooks] = UserBooksWriter

  def addBook(userId: UserId, bookId: BookId): UserBookId = {
    val userBookId: UserBookId = IdProvider.generateUserBookId

    bookServiceCollection.flatMap(_.insert(UserBooks(
      userBookId,
      userId,
      new ObjectId(bookId.value)
    )).map(_ => {}))

    userBookId
  }
}
