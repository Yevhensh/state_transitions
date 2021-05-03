package com.statetest.transitionapp.model

case class TransitionId(id: Int)

case class StateOperation(transitionId: TransitionId, entityId: StateEntityId, from: StateValue, to: StateValue)
