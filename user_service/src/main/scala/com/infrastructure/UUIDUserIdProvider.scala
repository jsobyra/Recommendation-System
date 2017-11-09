package com.infrastructure

import java.util.UUID

import com.domain.UserId
import com.domain.UserService.UserIdProvider


class UUIDUserIdProvider(userRepository: UserRepository) extends UserIdProvider {
  override def newId: UserId = UserId(UUID.randomUUID().toString.replace("-", ""))

  override def getUserId(email: String, password: String): UserId = userRepository.getUserId(email, password)
}
