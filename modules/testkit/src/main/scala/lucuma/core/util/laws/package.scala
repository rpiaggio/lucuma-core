// Copyright (c) 2016-2021 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.core.util

package object laws {

  type IsEq[A] = cats.kernel.laws.IsEq[A]
  val IsEq: cats.kernel.laws.IsEq.type = cats.kernel.laws.IsEq

  implicit final class IsEqArrow[A](val lhs: A) extends AnyVal {
    def <->(rhs: A): IsEq[A] = IsEq(lhs, rhs)
  }

}
