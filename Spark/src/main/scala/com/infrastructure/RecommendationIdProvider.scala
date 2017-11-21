package com.infrastructure

import java.util.UUID

import com.domain.{RecommendationId, UserBookId}

/**
  * Created by user on 04.11.17.
  */
object IdProvider {

  def generateRecommendationId = RecommendationId(UUID.randomUUID().toString.replace("-", ""))

  def generateUserBookId = UserBookId(UUID.randomUUID().toString.replace("-", ""))
}