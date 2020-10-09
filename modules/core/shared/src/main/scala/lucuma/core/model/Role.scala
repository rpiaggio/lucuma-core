// Copyright (c) 2016-2020 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.core.model

import cats.implicits._
import eu.timepit.refined.refineMV
import eu.timepit.refined.types.numeric.PosLong
import lucuma.core.util.Gid

/**
 * Each user has a current `Role` and a set of other roles they may assume. A role has (at least)
 * an `Access` level.
 */
sealed abstract class Role(val access: Access, elaboration: Option[String] = None) {
  final def name = elaboration.foldLeft(access.name)((n, e) => s"$n ($e)")
}

// Special roles

/** `GuestRole` allows limited access to temporary programs. */
final case object GuestRole extends Role(Access.Guest)

/** `ServiceRole` is used only for inter-service communication. */
final case class  ServiceRole(serviceName: String) extends Role(Access.Service, Some(serviceName))

/** The class of roles taken on by authenticated users. */
sealed abstract class StandardRole(access: Access, elaboration: Option[String] = None) extends Role(access, elaboration) {
  def id: StandardRole.Id
}
object StandardRole {

  /** The `Pi` role gives access to programs on which the user is a collaborator. */
  final case class Pi(id: StandardRole.Id) extends StandardRole(Access.Pi)

  /** The `Ngo` role is associated with a `Partner` and gives access to programs with affiliated users. */
  final case class Ngo(id: StandardRole.Id, partner: Partner) extends StandardRole(Access.Ngo, Some(partner.name))

  /** The `Staff` role gives access to all programs, as well as telescope facilities. */
  final case class Staff(id: StandardRole.Id) extends StandardRole(Access.Staff)

  /** The `Admin` role is a superuser role that allows access to all functionality. */
  final case class Admin(id: StandardRole.Id) extends StandardRole(Access.Admin)

  /** `StandardRole`s have unique ids and are associated with individual users. */
  case class Id(value: PosLong) {
    override def toString = this.show
  }
  object Id {
    implicit val GidId: Gid[Id] = Gid.instance(refineMV('r'), _.value, apply)
  }

}