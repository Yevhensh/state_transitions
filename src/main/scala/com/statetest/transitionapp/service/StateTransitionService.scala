package com.statetest.transitionapp.service

import cats.Monad
import cats.effect.Sync
import com.statetest.transitionapp.model.{StateId, StateName, StateTransition}
import com.statetest.transitionapp.repository.StateTransitionRepository
import cats.implicits._

import scala.collection.concurrent.TrieMap

class StateTransitionService[F[_]](transitionMatrixRepository: StateTransitionRepository[F])(implicit F: Sync[F]) {
  private val transitionsCache: F[TrieMap[StateName, StateTransition]] =
    getTransitions.map { transitions =>
      val trieMap = new TrieMap[StateName, StateTransition]()
      transitions.foreach(transition => trieMap.put(transition.stateName, transition))
      trieMap
    }

  def getTransitions: F[List[StateTransition]] =
    transitionsCache.map(_.values.toList)

  def getTransitionsByStateName(stateName: StateName): F[Option[StateTransition]] =
    transitionsCache.map(_.get(stateName))

  def updateTransitions(stateTransition: StateTransition): F[Unit] =
    for {
      _ <- transitionsCache.map(_.replace(stateTransition.stateName, stateTransition))
      _ <- transitionMatrixRepository.updateTransition(stateTransition)
    } yield ()

  def createTransition(stateTransition: StateTransition): F[StateId] = {
    for {
      _       <- transitionsCache.map(_.put(stateTransition.stateName, stateTransition))
      stateId <- transitionMatrixRepository.insertTransition(stateTransition)
    } yield stateId
  }
}
