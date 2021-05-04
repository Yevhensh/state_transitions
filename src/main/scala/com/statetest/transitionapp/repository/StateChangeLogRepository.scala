package com.statetest.transitionapp.repository

import doobie.Fragment
import doobie.implicits._
import cats.implicits._
import doobie._
import cats.~>
import com.statetest.transitionapp.db.StateChangeLogDb
import com.statetest.transitionapp.model.{EntityId, StateChangeLog, TransitionId}
import doobie.free.connection.ConnectionIO
import doobie.postgres.implicits._
import doobie._

class StateChangeLogRepository[F[_]](transact: ConnectionIO ~> F) {
  private val tableName = Fragment.const("state_change_log")
  private val fields    = sql"transition_id, entity_id, from_value, to_value"

  def getStateChangeLogs: F[List[StateChangeLogDb]] = {
    val sql = sql"select $fields from $tableName"
    transact(sql.query[StateChangeLogDb].to[List])
  }

  def getStateChangeLogById(transitionId: TransitionId): F[Option[StateChangeLogDb]] = {
    val sql = sql"select $fields from $tableName where transition_id = $transitionId"
    transact(sql.query[StateChangeLogDb].option)
  }

  def getLatestStateChangeLog(entityId: EntityId): F[Option[StateChangeLogDb]] = {
    val sql =
      sql"""select $fields 
           from $tableName 
            where entity_id = $entityId 
            ORDER BY transition_id 
            DESC LIMIT 1"""
    transact(sql.query[StateChangeLogDb].option)
  }

  def insertChangeLog(stateChangeLog: StateChangeLog): F[TransitionId] = {
    val sql =
      sql"""
        insert into $tableName($fields)
        values (
            ${stateChangeLog.transitionId},
            ${stateChangeLog.entityId},
            ${stateChangeLog.from},
            ${stateChangeLog.to})"""
    transact(sql.update.withUniqueGeneratedKeys[TransitionId]("transition_id"))
  }
}
