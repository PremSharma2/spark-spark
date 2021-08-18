package catz.datamanipulation

import java.util.UUID

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}

import scala.util.Try

object DataValidationExercise extends App {
/*
TODO
      Cats provides an extension type of the main Validated type,
       that we are going to use in our example:
     type ValidatedNel[+E, +A] = Validated[NonEmptyList[E], A]
      The cats.data.ValidatedNel models the error accumulation use case,
      that it is exactly what we need.
 */

  case class Property(listing: Listing)
  case class Listing(id: Option[String], contact: Contact, address: Address, location: GeoLocation)
  case class Contact(phone: String, formattedPhone: String)
  case class Address(address: String, postalCode: String, countryCode: String, city: String, state: Option[String], country: String)
  case class GeoLocation(lat: String, lng: String)
  def validateId(idOpt: Option[String], emptyID: Boolean): Validated[String, Option[String]] = {
    emptyID match {
      case true => idOpt match {
        case None => Valid(Some("iOpt"))
        case Some(_) => Invalid("IdNonEmptyError")
      }
      case false => idOpt match {
        case None => Invalid("IdEmptyError")
        case Some(id) => Try(UUID.fromString(id)).isSuccess match {
          case true => Valid(idOpt)
          case false => Invalid("NonUUIDFormatError")
        }
      }
    }
  }

  def validateGeoLocation(location: GeoLocation): Validated[String, GeoLocation] = {
    val tryLat = Try(location.lat.toDouble)
    val tryLong = Try(location.lng.toDouble)
    if (tryLat.isSuccess && tryLong.isSuccess)
      Valid(location)
    else Invalid("GeoLocationNonExistingError")
  }
  /*
import cats.instances._
  def validate(listing: Listing, emptyID: Boolean): ValidatedNel[String, Listing] = {
    Apply[ValidatedNel[String, ?]].map4(
      validateId(listing.id, emptyID).toValidatedNel,
      ContactValidator.validate(listing.contact),
      AddressValidator.validate(listing.address),
      validateGeoLocation(listing.location).toValidatedNel
    ) {
      case (id, contact, address, location) => Listing(id, contact, address, location)
    }
  }



   */

}


