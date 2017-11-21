package com.api

import java.util.Date

import com.api.Requests.UserId
import com.domain.{BookId, RecommendationId}

object Events {
  final case class UserForm(email: String, password: String)
  final case class User(id: UserId, email: String, password: String, time: Date)

  @SerialVersionUID(30L)
  final case class UserCreated(id: UserId, time: Date)

  @SerialVersionUID(45L)
  final case class UserLogged(id: UserId, token: String, time: Date)
  final case class Book(bookId: BookId, title: String, author: String, isbn: List[String], subjects: List[String],
                        languages: List[String], numberOfPages: Int, publishDate: String, country: String,
                        publishers: List[String])
  final case class Recommendation(recommendationId: RecommendationId, book: Book)
  final case class Books(books: List[Book])
  final case class Recommendations(recommendations: List[Recommendation])
}
