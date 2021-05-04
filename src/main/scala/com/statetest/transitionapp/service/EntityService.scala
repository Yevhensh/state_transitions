package com.statetest.transitionapp.service

import cats.Monad
import cats.data.OptionT
import com.statetest.transitionapp.model.{Entity, EntityId}
import com.statetest.transitionapp.repository.EntityRepository
import cats.implicits._

class EntityService[F[_]: Monad](entityRepository: EntityRepository[F]) {
  def getEntities: F[List[Entity]] =
    entityRepository.getEntities.map(_.map(Entity.fromDb))

  def getEntityById(entityId: EntityId): F[Option[Entity]] =
    OptionT(entityRepository.getEntityById(entityId)).map(Entity.fromDb).value

  def createEntity(entity: Entity): F[EntityId] = entityRepository.insertEntity(entity)

  def updateEntity(entity: Entity): F[Unit] = entityRepository.updateEntity(entity)
}
