package com.statetest.transitionapp.network

import com.statetest.transitionapp.model.{EntityId, StateChangeLog, StateName}

case class ChangeStateResp(entityId: EntityId, from: StateName, to: StateName)

object ChangeStateResp {
  def fromModel(stateChangeLog: StateChangeLog): ChangeStateResp = ChangeStateResp(
    entityId = stateChangeLog.entityId,
    from = stateChangeLog.from,
    to = stateChangeLog.to
  )
}
