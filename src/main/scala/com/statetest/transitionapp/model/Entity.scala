package com.statetest.transitionapp.model

import com.statetest.transitionapp.db.EntityDb

case class EntityId(id: Int) extends AnyVal

case class EntityName(name: String) extends AnyVal

case class Entity(entityId: EntityId, entityName: EntityName, stateName: StateName)

object Entity {
  def fromDb(entityDb: EntityDb): Entity =
    Entity(
      entityId = EntityId(entityDb.entity_id),
      stateName = StateName(entityDb.entity_name),
      entityName = EntityName(entityDb.entity_name)
    )
}
