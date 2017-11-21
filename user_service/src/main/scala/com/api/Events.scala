package com.api

import java.util.Date

import com.domain.UserId

sealed trait Events {
  def id: UserId
  def time: Date
}

object Events {
  final case class User(id: UserId, email: String, password: String, time: Date) extends Events
  @SerialVersionUID(30L)
  final case class UserCreated(id: UserId, time: Date) extends Events
  @SerialVersionUID(45L)
  final case class UserLogged(id: UserId, token: String, time: Date) extends Events
}
