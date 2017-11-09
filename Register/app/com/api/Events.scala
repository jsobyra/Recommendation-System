package com.api

import java.util.Date

import com.api.Requests.UserId

sealed trait Events {
  def id: UserId
  def time: Date
}

object Events {
  final case class UserForm(email: String, password: String)
  final case class User(id: UserId, email: String, password: String, time: Date) extends Events
  final case class UserCreated(id: UserId, time: Date) extends Events
  final case class UserLogged(id: UserId, token: String, time: Date) extends Events
}
