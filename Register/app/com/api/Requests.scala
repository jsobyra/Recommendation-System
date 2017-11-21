package com.api


object Requests {
  final case class CreateUser(email: String, password: String)
  final case class LogUser(email: String, password: String)
  final case class UserId(value: String) extends AnyVal
  final case class BookId(value: String) extends AnyVal
  final case class CreateDefaultRecommendation(userId: UserId)
  final case class CreateRecommendation(userId: UserId)
  final case class AddBook(userId: UserId, bookId: BookId)
  final case class GetUserBooks(userId: UserId)
  final case class GetUserRecommendation(userId: UserId)
}
