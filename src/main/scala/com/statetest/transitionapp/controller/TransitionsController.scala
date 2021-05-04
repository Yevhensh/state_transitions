package com.statetest.transitionapp.controller

import cats.effect.Async
import com.statetest.transitionapp.controller.handler.TransitionHandler
import com.statetest.transitionapp.model.{StateName, StateTransition}
import cats.implicits._
import org.http4s.circe.jsonOf
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.parser._
import cats.implicits._
import com.statetest.transitionapp.init.Services

class TransitionsController[F[_]: Async](services: Services[F]) extends Http4sDsl[F] {
  implicit val decoderStateTransition: EntityDecoder[F, StateTransition] = jsonOf[F, StateTransition]

  private val transitionHandler = new TransitionHandler[F](services.stateTransitionService)

  def pureRoutes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "transitions" =>
        transitionHandler.getTransitions
      case GET -> Root / "transitions" / stateName =>
        transitionHandler.getTransitionByStateName(StateName(stateName))
      case req @ POST -> Root / "transitions" =>
        req.as[StateTransition].flatMap { stateTransition =>
          transitionHandler.updateTransition(stateTransition)
        }
      case req @ PUT -> Root / "transitions" =>
        req.as[StateTransition].flatMap { stateTransition =>
          transitionHandler.updateTransition(stateTransition)
        }
    }
}
