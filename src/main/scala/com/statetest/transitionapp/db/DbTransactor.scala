package com.statetest.transitionapp.db

import cats.effect.{Async, Blocker, ContextShift, Resource}
import com.statetest.transitionapp.config.JdbcConfig
import doobie._
import doobie.implicits._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

case class DbTransactor[F[_]: ContextShift: Async](jdbcConfig: JdbcConfig)(implicit B: Blocker) {
  val transactor: Resource[F, HikariTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](jdbcConfig.fixedThreadPoolSize)
      xa <- HikariTransactor.newHikariTransactor[F](
        jdbcConfig.driver,
        jdbcConfig.url,
        jdbcConfig.user,
        jdbcConfig.pass,
        ce,
        B
      )
    } yield xa
}
