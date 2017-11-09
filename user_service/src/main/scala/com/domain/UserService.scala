package com.domain

object UserService {
  trait UserIdProvider {
    def newId: UserId
    def getUserId(email: String, password: String): UserId
  }
}
