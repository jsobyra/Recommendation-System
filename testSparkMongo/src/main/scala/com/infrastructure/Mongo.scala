package com.infrastructure

import reactivemongo.api.MongoDriver

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Mongo {
  private val mongoDriver = MongoDriver()
  private val mongoConnection = mongoDriver.connection(Seq("127.0.0.1:27017"))
  val mongoDb = Await.result(mongoConnection.database("userService"), 5 seconds)
}
