package com.api

import java.util.Date

import com.domain._
import org.bson.types.ObjectId

/**
  * Created by user on 04.11.17.
  */

object Events {
  final case class User(id: UserId, email: String, password: String, time: Date)
  final case class UserBooks(id: UserBookId, userId: UserId, bookId: ObjectId)
  final case class Book(bookId: BookId, title: String, author: String, isbn: List[String], subjects: List[String],
                        languages: List[String], numberOfPages: Int, publishDate: String, country: String,
                        publishers: List[String])
  final case class Recommendation(recommendationId: RecommendationId, book: Book)
  final case class Books(books: List[Book])
  final case class Recommendations(recommendations: List[Recommendation])
}
