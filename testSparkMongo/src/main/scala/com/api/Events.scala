package com.api

import java.util.Date

import com.domain.{RecommendationId, UserBookId, UserId}
import org.bson.types.ObjectId

/**
  * Created by user on 04.11.17.
  */

object Events {
  final case class User(id: UserId, email: String, password: String, time: Date)
  final case class Recommendation(id: RecommendationId, userId: UserId, bookId: ObjectId)
  final case class UserBooks(id: UserBookId, userId: UserId, bookId: ObjectId)
}
