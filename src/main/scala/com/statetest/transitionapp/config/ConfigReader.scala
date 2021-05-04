package com.statetest.transitionapp.config

case class ConfigReader(service: ServiceConfig, jdbc: JdbcConfig)

case class ServiceConfig(host: String, port: Int)

case class JdbcConfig(driver: String, url: String, user: String, pass: String, fixedThreadPoolSize: Int)