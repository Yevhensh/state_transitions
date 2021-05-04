package com.statetest.transitionapp.service

import cats.Monad
import cats.data.OptionT
import com.statetest.transitionapp.model.{EntityId, StateChangeLog, TransitionId}
import com.statetest.transitionapp.repository.StateChangeLogRepository
import cats.implicits._

class StateChangeLogService[F[_]: Monad](stateChangeLogRepository: StateChangeLogRepository[F]) {
  def getStateChangeLogs: F[List[StateChangeLog]] =
    stateChangeLogRepository.getStateChangeLogs.map(_.map(StateChangeLog.fromDb))

  def getStateChangeLogById(transitionId: TransitionId): F[Option[StateChangeLog]] =
    OptionT(stateChangeLogRepository.getStateChangeLogById(transitionId)).map(StateChangeLog.fromDb).value

  def getLatestStateChangeLog(entityId: EntityId): F[Option[StateChangeLog]] =
    OptionT(stateChangeLogRepository.getLatestStateChangeLog(entityId)).map(StateChangeLog.fromDb).value

  def recordChangeLog(stateChangeLog: StateChangeLog): F[TransitionId] =
    stateChangeLogRepository.insertChangeLog(stateChangeLog)
}
