package com.statetest.transitionapp

import cats.effect.{Blocker, ConcurrentEffect, ExitCode, IO, IOApp, Timer}
import com.statetest.transitionapp.config.ConfigReader
import com.statetest.transitionapp.controller.{EntityController, StateController, TransitionsController}
import com.statetest.transitionapp.db.DbTransactor
import com.statetest.transitionapp.init.{Databases, Services}
import com.typesafe.scalalogging.StrictLogging
import fs2.Stream
import org.http4s.HttpApp
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import cats.effect._
import cats.implicits._

object Main extends IOApp with StrictLogging {
  override def run(args: List[String]): IO[ExitCode] = {
    pureconfig.loadConfig[ConfigReader] match {
      case Right(config) =>
        Blocker[IO].use { implicit blocker =>
          DbTransactor[IO](config.jdbc).transactor.use { implicit xa =>
            logger.debug(s"config is loaded correctly $config")
            val databases: Databases[IO] = Databases.initialize[IO]()
            val services: Services[IO]   = Services.initialize[IO](databases)
            val transitionsController    = new TransitionsController[IO](services)
            val entityController         = new EntityController[IO](services)
            val stateController          = new StateController[IO](services)
            val httpApp =
              (transitionsController.pureRoutes <+> entityController.pureRoutes <+> stateController.pureRoutes).orNotFound
            val httpIO = stream(httpApp, config.service.host, config.service.port).compile.drain
            httpIO.flatMap(_ => IO(ExitCode.Success))
          }
        }
      case Left(err) =>
        logger.error("Wrong config for a service", err)
        IO(ExitCode.Error)
    }
  }

  def stream[F[_]](httpApp: HttpApp[F], host: String, port: Int)(
      implicit F: ConcurrentEffect[F],
      timer: Timer[F]
  ): Stream[F, ExitCode] =
    BlazeServerBuilder[F]
      .bindHttp(port, host)
      .withHttpApp(httpApp)
      .serve

}
