package com.api


import com.domain.{BookId, UserId}
import org.bson.types.ObjectId

/**
  * Created by user on 04.11.17.
  */
object Requests {
  final case class CreateDefaultRecommendation(userId: UserId)
  final case class CreateRecommendation(userId: UserId)
  final case class AddBook(userId: UserId, bookId: BookId)
}
