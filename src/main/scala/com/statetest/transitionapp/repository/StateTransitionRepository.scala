package com.statetest.transitionapp.repository

import cats.implicits._
import cats.~>
import com.statetest.transitionapp.db.StateTransitionDb
import com.statetest.transitionapp.model.{StateId, StateName, StateTransition, TransitionId}
import doobie.Fragment
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._
import doobie._
import doobie.util.ExecutionContexts

class StateTransitionRepository[F[_]](transact: ConnectionIO ~> F) {
  private val tableName = Fragment.const("transitions_matrix")
  private val fields    = sql"state_id, state_name, transitions"

  def getTransitions: F[List[StateTransitionDb]] = {
    val sql = sql"""SELECT $fields from $tableName"""
    transact(sql.query[StateTransitionDb].to[List])
  }

  def getTransitionByName(stateName: StateName): F[Option[StateTransitionDb]] = {
    val sql = sql"""SELECT $fields from $tableName where state_name = $stateName"""
    transact(sql.query[StateTransitionDb].option)
  }

  def updateTransition(stateTransition: StateTransition): F[Unit] = {
    val transitions = stateTransition.transitions.states.map(_.value).toList
    val sql =
      sql"""UPDATE $tableName set state_name = ${stateTransition.stateName}, transitions = $transitions"""
    transact(sql.update.run.void)
  }

  def insertTransition(stateTransition: StateTransition): F[StateId] = {
    val transitions = stateTransition.transitions.states.map(_.value).toList
    val sql =
      sql"""insert into $tableName ($fields) 
           values (${stateTransition.stateId}, ${stateTransition.stateName}, $transitions)"""
    transact(sql.update.withUniqueGeneratedKeys[StateId]("state_id"))
  }
}
