package com.statetest.transitionapp.controller

import cats.effect.Async
import org.http4s.circe.jsonOf
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.parser._
import cats.implicits._
import com.statetest.transitionapp.controller.handler.StateHandler
import com.statetest.transitionapp.init.Services
import com.statetest.transitionapp.model.{EntityId, StateId, TransitionId}
import com.statetest.transitionapp.network.ChangeStateReq

class StateController[F[_]: Async](services: Services[F]) extends Http4sDsl[F] {
  implicit val decoderChangeStateReq: EntityDecoder[F, ChangeStateReq] = jsonOf[F, ChangeStateReq]

  private val stateHandler = new StateHandler[F](services.stateOperationService, services.stateChangeLogService)

  def pureRoutes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "states"                        => stateHandler.fetchChangeLogs
      case GET -> Root / "states" / IntVar(transitionId) => stateHandler.fetchChangeLogById(TransitionId(transitionId))
      case req @ POST -> Root / "states" / IntVar(entityId) =>
        req.as[ChangeStateReq].flatMap { changeStateReq =>
          stateHandler.transitionEntityState(EntityId(entityId), changeStateReq)
        }
    }
}