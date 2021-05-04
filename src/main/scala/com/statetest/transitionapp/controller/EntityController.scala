package com.statetest.transitionapp.controller

import cats.effect.Async
import cats.implicits._
import com.statetest.transitionapp.model.{Entity, EntityId}
import org.http4s.circe.jsonOf
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.parser._
import cats.implicits._
import com.statetest.transitionapp.controller.handler.EntityHandler
import com.statetest.transitionapp.init.Services

class EntityController[F[_]: Async](services: Services[F]) extends Http4sDsl[F] {
  implicit val decoderEntity: EntityDecoder[F, Entity] = jsonOf[F, Entity]

  private val entityHandler = new EntityHandler[F](services.entityService)

  def pureRoutes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "entities" => entityHandler.getEntities
      case req @ POST -> Root / "entities" =>
        req.as[Entity].flatMap { entity =>
          entityHandler.createEntity(entity)
        }
      case GET -> Root / "entities" / IntVar(entityId) => entityHandler.getEntityById(EntityId(entityId))
    }
}
