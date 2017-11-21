package com.infrastructure

import com.domain.Recommendation
import com.domain.{RecommendationId, UserId}
import org.bson.types.ObjectId
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

object RecommendationReader extends BSONDocumentReader[Recommendation] {
  def read(bson: BSONDocument): Recommendation = {
    Recommendation(
      RecommendationId(bson.getAs[String]("recommendationId").get),
      UserId(bson.getAs[String]("userId").get),
      new ObjectId(bson.getAs[String]("bookId").get)
    )
  }
}
