package com.statetest.transitionapp.model

import com.statetest.transitionapp.db.StateChangeLogDb

case class TransitionId(id: Int) extends AnyVal

case class StateChangeLog(transitionId: TransitionId, entityId: EntityId, from: StateName, to: StateName)

object StateChangeLog {
  def apply(entityId: EntityId, from: StateName, to: StateName): StateChangeLog =
    new StateChangeLog(TransitionId(0), entityId, from, to)

  def fromDb(stateChangeLogDb: StateChangeLogDb): StateChangeLog =
    StateChangeLog(
      transitionId = TransitionId(stateChangeLogDb.transition_id),
      entityId = EntityId(stateChangeLogDb.entity_id),
      from = StateName(stateChangeLogDb.from_value),
      to = StateName(stateChangeLogDb.to_value)
    )
}
