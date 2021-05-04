package com.statetest.transitionapp.controller.handler

import cats.Monad
import com.statetest.transitionapp.service.EntityService
import org.http4s.Response
import org.http4s.dsl.Http4sDsl
import cats.implicits._
import com.statetest.transitionapp.model.{Entity, EntityId}
import com.statetest.transitionapp.network.Error
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

class EntityHandler[F[_]: Monad](entityService: EntityService[F]) extends Http4sDsl[F] {
  def getEntities: F[Response[F]] = entityService.getEntities.flatMap(entities => Ok(entities.asJson))

  def getEntityById(entityId: EntityId): F[Response[F]] =
    entityService.getEntityById(entityId).flatMap {
      case Some(entity) => Ok(entity.asJson)
      case None         => BadRequest(Error(s"Entity not found with id: $entityId").asJson)
    }

  def createEntity(entity: Entity): F[Response[F]] =
    entityService.createEntity(entity).flatMap(entityId => Ok(entityId.id.asJson))
}
