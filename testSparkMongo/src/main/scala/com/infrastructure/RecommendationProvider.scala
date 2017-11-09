package com.infrastructure

import java.util

import com.domain.UserId

import scala.collection.JavaConverters._
import org.apache.spark.rdd.RDD
import org.bson.Document


@SerialVersionUID(15L)
class RecommendationProvider(authorList: List[String], languageList: List[String], subjectList: List[String],
                             savedRecommendation: Option[RDD[Document]], bookRDD: RDD[Document])
  extends Serializable {

  private val authors = authorList
  private val languages = languageList
  private val subjects = subjectList
  private val books = bookRDD
  private val recommendedBooks = savedRecommendation match {
    case Some(rdd) => rdd.collect().toList
    case None => Nil
  }


  def createRecommendation: List[Document] = {
    books.filter(book =>
      (authors.contains(Book.getAuthor(book)) || subjects.exists(subject => Book.getSubjects(book).contains(subject))) &&
        languages.exists(language => Book.getLanguages(book).contains(language))
    ).filter(book =>
      !recommendedBooks.contains(book)
    ).take(10).toList
  }

  def createDefaultRecommendation: List[Document] = {
    val r = scala.util.Random
    r.shuffle(books.take(100).toList).take(10)
  }
}

object RecommendationHelper {

  def getRecommendation(rdd: RDD[Document], userId: UserId): RDD[Document] = {
    rdd.filter(doc => doc.getString("userId").equals(userId.value)).distinct().cache()
  }

  def getAuthors(rdd: RDD[Document]): List[String] =
    rdd.map(doc => Book.getAuthor(doc)).distinct().collect().toList

  def getTitles(rdd: RDD[Document]): List[String] =
    rdd.map(doc => Book.getTitle(doc)).distinct().collect().toList

  def getLanguages(rdd: RDD[Document]): List[String] =
    rdd.flatMap(doc => Book.getLanguages(doc)).distinct().
      //map(language => language.split("/")(2).split("}")(0)).
      collect().toList

  def getNumberOfPages(rdd: RDD[Document]): List[Int] =
    rdd.map(doc => Book.getNumberOfPages(doc)).collect().toList

  def getPublishDate(rdd: RDD[Document]): List[String] =
    rdd.map(doc => Book.getPublishDate(doc)).distinct().collect().toList

  def getCountry(rdd: RDD[Document]): List[String] =
    rdd.map(doc => Book.getCountry(doc)).distinct().collect().toList

  def getSubjects(rdd: RDD[Document]): List[String] =
    rdd.flatMap(doc => Book.getSubjects(doc)).distinct().collect().toList

  def getPublishers(rdd: RDD[Document]): List[String] =
    rdd.flatMap(doc => Book.getPublishers(doc)).distinct().collect().toList
}

object Book {
  def getSubjects(doc: Document): List[String] = doc.get("subjects").asInstanceOf[util.ArrayList[String]].asScala.toList

  def getsISBNs(doc: Document): List[String] = doc.get("isbn_10").asInstanceOf[util.ArrayList[String]].asScala.toList

  def getAuthor(doc: Document): String = doc.getString("by_statement")

  def getTitle(doc: Document): String = doc.getString("title")

  def getId(doc: Document): String = doc.getObjectId("_id").toString

  def getLanguages(doc: Document): List[String] =
    doc.get("languages").asInstanceOf[util.ArrayList[Document]].
      asScala.toList.map(language => language.toString.split("/")(2).split("}")(0))

  def getNumberOfPages(doc: Document): Int = doc.getInteger("number_of_pages")

  def getPublishDate(doc: Document): String = doc.getString("publishDate")

  def getCountry(doc: Document): String = doc.getString("publish_country")

  def getPublishers(doc: Document): List[String] = doc.get("publishers").asInstanceOf[util.ArrayList[String]].asScala.toList

  def parseLanguage(language: String): String = "Document{{key=/languages/" + language + "}}"
}
//final case class Book(_id: String, isbn_10: List[String], title: String, )