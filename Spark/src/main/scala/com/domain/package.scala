package com

import org.bson.types.ObjectId

/**
  * Created by user on 04.11.17.
  */
package object domain {
  trait DomainException extends Exception

  final case class UserId(value: String) extends AnyVal
  final case class BookId(value: String) extends AnyVal
  final case class UserBookId(value: String) extends AnyVal
  final case class RecommendationId(value: String) extends AnyVal
  final case class Recommendation(id: RecommendationId, userId: UserId, bookId: ObjectId)

  final case class UnknownRequest(name: String) extends DomainException
  case object ResourceDoesNotExist extends DomainException
}
