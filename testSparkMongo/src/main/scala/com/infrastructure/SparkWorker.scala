package com.infrastructure

import com.domain.{BookId, RecommendationId, UserBookId, UserId}
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.bson.Document
import org.slf4j.LoggerFactory



//TODO not looking at which books I have written
class SparkWorker(userRepository: UserRepository, recommendationRepository: RecommendationRepository,
                  bookRepository: BookRepository){

  lazy val logger = LoggerFactory.getLogger(getClass)

  private val spark = SparkSession.builder()
    .master("local")
    .appName("MongoSparkConnectorIntro")
    .getOrCreate()
  private val sc = spark.sparkContext
  private val bookRDD = MongoSpark.load(sc, ReadConfig(Map("uri" -> "mongodb://127.0.0.1/recommendation.books")))
  private val recommendationRDD = MongoSpark.load(sc, ReadConfig(Map("uri" -> "mongodb://127.0.0.1/userService.userRecommendation")))

  def getRecommendation(userId: UserId): RDD[Document] = {
    val savedRecommendation = RecommendationHelper.getRecommendation(recommendationRDD, userId)
    val savedID = savedRecommendation.map(doc => (doc.getString("bookId"))).collect().toList

    sc.parallelize(bookRDD.filter(doc => savedID.contains(Book.getId(doc))).take(savedID.size))
  }

  def createRecommendation(userId: UserId): RecommendationId = {
    val savedRecommendation = getRecommendation(userId)
    val recommendationProvider = new RecommendationProvider(
      RecommendationHelper.getAuthors(savedRecommendation),
      RecommendationHelper.getLanguages(savedRecommendation),
      RecommendationHelper.getSubjects(savedRecommendation),
      Some(savedRecommendation),
      bookRDD)
    recommendationRepository.deleteRecommendation(userId)
    val recommendation = recommendationProvider.createRecommendation
    recommendationRepository.saveRecommendation(userId, recommendation)
  }

  def createDefaultRecommendation(userId: UserId): RecommendationId = {
    val recommendationProvider = new RecommendationProvider(Nil, Nil, Nil, None, bookRDD)
    val recommendation = recommendationProvider.createDefaultRecommendation
    recommendationRepository.saveRecommendation(userId, recommendation)
  }

  def addBook(userId: UserId, bookId: BookId): UserBookId = {
    bookRepository.addBook(userId, bookId)
  }
}