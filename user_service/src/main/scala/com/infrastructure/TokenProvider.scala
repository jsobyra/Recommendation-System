package com.infrastructure

import java.io.File
import java.nio.file.Files
import java.security.{KeyFactory, PrivateKey, PublicKey}
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}

import com.domain.UserId
import com.typesafe.config.Config
import org.json4s.DefaultFormats
import org.json4s.JsonAST.{JLong, JObject, JString}
import pdi.jwt.{JwtAlgorithm, JwtJson4s, JwtTime}

import scala.util.{Failure, Success}

class TokenProvider(config: Config) {
  private val algorithm = JwtAlgorithm.RS256
  private val privateKey = getPrivateKey
  private val publicKey = getPublicKey


  def createToken(id: String): String = {
    val expirationTime = config.getInt("token.expiration-time")
    val claim = JObject(("iat", JLong(JwtTime.nowSeconds)), ("exp", JLong(JwtTime.nowSeconds + expirationTime)), ("user", JString(id)))
    JwtJson4s.encode(claim, privateKey, algorithm)
  }

  def isValid(token: String): Boolean = {
    JwtJson4s.isValid(token, publicKey, Seq(algorithm))
  }

  def decodeToken(token: String): Option[UserId] = {
    JwtJson4s.decodeJson(token, publicKey, Seq(algorithm)) match {
      case Success(jObject) =>
        implicit val formats = DefaultFormats
        val userId = (jObject \\ "user").extract[String]
        Option(UserId(userId))
      case Failure(_) => Option.empty
    }
  }

  private def getPrivateKey: PrivateKey = {
    val path = getClass.getResource("/private_key.der").getPath
    val bytes = Files.readAllBytes(new File(path).toPath)

    val spec = new PKCS8EncodedKeySpec(bytes)
    val factory = KeyFactory.getInstance("RSA")

    factory.generatePrivate(spec)
  }

  private def getPublicKey: PublicKey = {
    val path = getClass.getResource("/public_key.der").getPath
    val bytes = Files.readAllBytes(new File(path).toPath)

    val spec = new X509EncodedKeySpec(bytes)
    val factory = KeyFactory.getInstance("RSA")

    factory.generatePublic(spec)
  }
}










