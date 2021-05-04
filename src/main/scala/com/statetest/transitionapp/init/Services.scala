package com.statetest.transitionapp.init

import cats.effect.Sync
import com.statetest.transitionapp.service._

case class Services[F[_]](
    stateOperationService: StateOperationService[F],
    stateTransitionService: StateTransitionService[F],
    entityService: EntityService[F],
    stateChangeLogService: StateChangeLogService[F]
)

object Services {
  def initialize[F[_]: Sync](databases: Databases[F]): Services[F] = {
    val stateChangeLogService  = new StateChangeLogService[F](databases.stateChangeLogRepository)
    val entityService          = new EntityService[F](databases.entityRepository)
    val stateTransitionService = new StateTransitionService[F](databases.stateTransitionRepository)
    new Services[F](
      stateOperationService = new StateOperationService[F](stateChangeLogService, entityService, stateTransitionService),
      stateTransitionService = new StateTransitionService[F](databases.stateTransitionRepository),
      entityService = new EntityService[F](databases.entityRepository),
      stateChangeLogService = stateChangeLogService
    )
  }
}
