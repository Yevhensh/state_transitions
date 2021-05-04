package com.statetest.transitionapp.model

import cats.data.State
import com.statetest.transitionapp.db.StateTransitionDb

case class StateId(id: Int) extends AnyVal

case class StateTransition(stateId: StateId, stateName: StateName, transitions: Transitions)

case class StateName(value: String) extends AnyVal

case class Transitions(states: Seq[StateName]) extends AnyVal

object StateTransition {
  def changeState(newState: StateName): State[Entity, StateChangeLog] =
    State { entity =>
      (entity.copy(stateName = newState), StateChangeLog(entity.entityId, entity.stateName, newState))
    }

  def fromDb(stateTransitionDb: StateTransitionDb): StateTransition =
    StateTransition(
      stateId = StateId(stateTransitionDb.state_id),
      stateName = StateName(stateTransitionDb.state_name),
      transitions = Transitions(stateTransitionDb.transitions.map(StateName.apply))
    )
}
