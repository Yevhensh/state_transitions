package com.statetest.transitionapp.db

case class StateTransitionDb(state_id: Int, state_name: String, transitions: List[String])
