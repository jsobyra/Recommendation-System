package com.infrastructure

import com.api.Events.{Recommendation}
import reactivemongo.bson.{BSONDocument, BSONDocumentWriter}

object RecommendationWriter extends BSONDocumentWriter[Recommendation] {
  def write(recommendation: Recommendation): BSONDocument =
    BSONDocument(
      "recommendationId" -> recommendation.id.value,
      "userId" -> recommendation.userId.value,
      "bookId" -> recommendation.bookId.toString
    )
}