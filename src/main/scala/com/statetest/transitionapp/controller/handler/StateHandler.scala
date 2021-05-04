package com.statetest.transitionapp.controller.handler

import cats.Monad
import com.statetest.transitionapp.model.{EntityId, StateId, TransitionId}
import com.statetest.transitionapp.network.{ChangeStateReq, ChangeStateResp, Error}
import com.statetest.transitionapp.service.{StateChangeLogService, StateOperationService}
import org.http4s.Response
import org.http4s.dsl.Http4sDsl
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

class StateHandler[F[_]: Monad](
    stateOperationService: StateOperationService[F],
    stateChangeLogService: StateChangeLogService[F]
) extends Http4sDsl[F] {
  def transitionEntityState(entityId: EntityId, changeStateReq: ChangeStateReq): F[Response[F]] =
    stateOperationService.transitionState(entityId, changeStateReq.state).flatMap {
      case Right(stateChangeLog) => Ok(ChangeStateResp.fromModel(stateChangeLog).asJson)
      case Left(err)             => BadRequest(err.asJson)
    }

  def fetchChangeLogs: F[Response[F]] =
    stateChangeLogService.getStateChangeLogs.flatMap(stateChangeLogs => Ok(stateChangeLogs.asJson))

  def fetchChangeLogById(transitionId: TransitionId): F[Response[F]] =
    stateChangeLogService.getStateChangeLogById(transitionId).flatMap {
      case Some(stateChangeLog) => Ok(stateChangeLog.asJson)
      case None                 => BadRequest(Error(s"Change log not found with transitionId: $transitionId").asJson)
    }
}
