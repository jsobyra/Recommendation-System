package com.api

object Requests {
  final case class CreateUser(email: String, password: String)
  final case class LogUser(email: String, password: String)
}
