package com.statetest.transitionapp.repository

import doobie.Fragment
import doobie.implicits._
import cats.implicits._
import doobie._
import cats.~>
import com.statetest.transitionapp.db.{EntityDb, StateChangeLogDb, StateTransitionDb}
import com.statetest.transitionapp.model.{Entity, EntityId}
import doobie.free.connection.ConnectionIO
import doobie.postgres.implicits._
import doobie._

class EntityRepository[F[_]](transact: ConnectionIO ~> F) {
  private val tableName = Fragment.const("entities")
  private val fields    = sql"entity_id, entity_name, state_name"

  def getEntities: F[List[EntityDb]] = {
    val sql = sql"select $fields from $tableName"
    transact(sql.query[EntityDb].to[List])
  }

  def getEntityById(entityId: EntityId): F[Option[EntityDb]] = {
    val sql = sql"select $fields from $tableName where entity_id = $entityId"
    transact(sql.query[EntityDb].option)
  }

  def insertEntity(entity: Entity): F[EntityId] = {
    val sql =
      sql"""insert into $tableName ($fields)
           values (${entity.entityId}, ${entity.stateName})"""
    transact(sql.update.withUniqueGeneratedKeys[EntityId]("entity_id"))
  }

  def updateEntity(entity: Entity): F[Unit] = {
    val sql = sql"""update $tableName set entity_name = ${entity.entityName}, state_name = ${entity.stateName}"""
    transact(sql.update.run.void)
  }
}
