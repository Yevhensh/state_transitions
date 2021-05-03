package com.statetest.transitionapp.model

case class StateEntityId(id: Int) extends AnyVal

case class State(stateId: StateEntityId, stateValue: StateValue, transitions: Transitions)

case class StateValue(value: String)

case class Transitions(states: Seq[State])
