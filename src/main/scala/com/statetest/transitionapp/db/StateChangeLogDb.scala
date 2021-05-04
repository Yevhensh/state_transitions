package com.statetest.transitionapp.db

case class StateChangeLogDb(transition_id: Int, entity_id: Int, from_value: String, to_value: String)
