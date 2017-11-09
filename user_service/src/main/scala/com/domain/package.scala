package com
import scala.concurrent.ExecutionContext.Implicits.global

package object domain {
  trait DomainException extends Exception
  trait CredentialsValidationException extends Exception
  final case class UserId(value: String) extends AnyVal

  case object ResourceDoesNotExist extends DomainException
  case object ResourceAlreadyExists extends DomainException
  final case class UnknownRequest(name: String) extends DomainException

  case object IncorrectEmail extends CredentialsValidationException
  case object IncorrectPassword extends CredentialsValidationException
  case object EmptyEmail extends CredentialsValidationException
  case object EmptyPassword extends CredentialsValidationException

  case object UnknownRequest
}
