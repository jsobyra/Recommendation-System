package com.infrastructure

import java.time.{LocalDateTime, ZoneId}
import java.util.Date

object TimeProvider {
  def now = Date.from(current.toInstant)
  def current = LocalDateTime.now().atZone(ZoneId.systemDefault())
}
