package com.statetest.transitionapp.controller.handler

import cats.Monad
import com.statetest.transitionapp.service.StateTransitionService
import org.http4s.Response
import cats.implicits._
import com.statetest.transitionapp.model.{StateName, StateTransition}
import com.statetest.transitionapp.network.Error
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

class TransitionHandler[F[_]: Monad](transitionService: StateTransitionService[F]) extends Http4sDsl[F] {
  def getTransitions: F[Response[F]] = transitionService.getTransitions.flatMap(transitions => Ok(transitions.asJson))

  def getTransitionByStateName(stateName: StateName): F[Response[F]] =
    transitionService.getTransitionsByStateName(stateName).flatMap {
      case Some(stateTransition) => Ok(stateTransition.asJson)
      case None                  => BadRequest(Error(s"Transition not found with name: $stateName").asJson)
    }

  def updateTransition(stateTransition: StateTransition): F[Response[F]] =
    transitionService.updateTransitions(stateTransition).flatMap(_ => Ok("Updated"))

  def createTransition(stateTransition: StateTransition): F[Response[F]] =
    transitionService.createTransition(stateTransition).flatMap(stateId => Ok(stateId.id.asJson))
}
