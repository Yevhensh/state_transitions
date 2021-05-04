package com.statetest.transitionapp.service

import cats.Monad
import cats.data.OptionT
import com.statetest.transitionapp.model.{Entity, EntityId, StateChangeLog, StateName, StateTransition}
import com.statetest.transitionapp.network.Error
import cats.implicits._

class StateOperationService[F[_]: Monad](
    stateChangeLogService: StateChangeLogService[F],
    entityService: EntityService[F],
    stateTransitionService: StateTransitionService[F]
) {
  def transitionState(entityId: EntityId, stateName: StateName): F[Either[Error, StateChangeLog]] =
    (for {
      entity          <- OptionT(entityService.getEntityById(entityId))
      stateTransition <- OptionT(stateTransitionService.getTransitionsByStateName(entity.stateName))
    } yield {
      if (ensureNewStateIsTransition(stateTransition, stateName)) {
        val (updatedEntity, stateChangeLog) = StateTransition.changeState(stateName).run(entity).value
        Right(recordStateChanges(updatedEntity, stateChangeLog))
      } else Left(Error(s"There are no such state transition ${stateName.value} for entityId: ${entityId.id}"))
    }).value.flatMap {
      case Some(either) => either.sequence
      case None         => Monad[F].pure(Left(Error("Process fail")))
    }

  private def recordStateChanges(entity: Entity, stateChangeLog: StateChangeLog): F[StateChangeLog] =
    for {
      _ <- entityService.updateEntity(entity)
      _ <- stateChangeLogService.recordChangeLog(stateChangeLog)
    } yield stateChangeLog

  private def ensureNewStateIsTransition(stateTransition: StateTransition, newState: StateName): Boolean =
    stateTransition.transitions.states.contains(newState)
}
