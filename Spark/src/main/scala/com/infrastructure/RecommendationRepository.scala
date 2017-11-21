package com.infrastructure

import com.domain.{Recommendation, RecommendationId, UserId}
import org.bson.Document
import org.bson.types.ObjectId
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

import scala.concurrent.{ExecutionContext, Future}



class RecommendationRepository(db: DefaultDB)(implicit ec: ExecutionContext) {
  private val recommendationServiceCollection: Future[BSONCollection] = Future {
    db("userRecommendation")
  }

  implicit def requestReader: BSONDocumentReader[Recommendation] = RecommendationReader
  implicit def requestWriter: BSONDocumentWriter[Recommendation] = RecommendationWriter

  def deleteRecommendation(userId: UserId) = {
    val query = BSONDocument("userId" -> userId.value)
    recommendationServiceCollection.flatMap(_.remove(query).map(_ => {}))
  }

  def saveRecommendation(userId: UserId, recommendations: List[Document]): RecommendationId = {
    val recommendationId: RecommendationId = IdProvider.generateRecommendationId
    recommendations.foreach(recommendation => {
      recommendationServiceCollection.flatMap(_.insert(Recommendation(
        recommendationId,
        userId,
        new ObjectId(BookHelper.getId(recommendation))
      )).map(_ => {}))
    })
    recommendationId
  }
}
