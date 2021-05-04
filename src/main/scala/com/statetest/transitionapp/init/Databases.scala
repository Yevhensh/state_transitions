package com.statetest.transitionapp.init

import cats.effect.Sync
import com.statetest.transitionapp.repository.{EntityRepository, StateChangeLogRepository, StateTransitionRepository}
import doobie.hikari.HikariTransactor

case class Databases[F[_]](
    entityRepository: EntityRepository[F],
    stateChangeLogRepository: StateChangeLogRepository[F],
    stateTransitionRepository: StateTransitionRepository[F]
)

object Databases {
  def initialize[F[_]]()(
    implicit M: Sync[F],
    xa: HikariTransactor[F]
  ): Databases[F] = new Databases[F](
    entityRepository = new EntityRepository[F](xa.trans),
    stateChangeLogRepository = new StateChangeLogRepository[F](xa.trans),
    stateTransitionRepository = new StateTransitionRepository[F](xa.trans)
  )
}
