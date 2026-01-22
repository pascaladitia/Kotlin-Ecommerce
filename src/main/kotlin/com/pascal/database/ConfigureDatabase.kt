package com.pascal.database

import com.pascal.config.DotEnvConfig
import com.pascal.database.entities.UserProfileTable
import com.pascal.database.entities.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.Slf4jSqlDebugLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun configureDatabase() {
    initDB()
    transaction {
        TransactionManager.current().addLogger(Slf4jSqlDebugLogger)
        SchemaUtils.create(
            UserTable,
            UserProfileTable,
        )
    }
}

private fun initDB() {
    // Create HikariConfig with values from DotEnv
    // we can also define from hikari.properties like HikariDataSource(HikariConfig("/hikari.properties"))
    val config = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = "jdbc:postgresql://${DotEnvConfig.dbHost}:${DotEnvConfig.dbPort}/${DotEnvConfig.dbName}"
        username = DotEnvConfig.dbUser
        password = DotEnvConfig.dbPassword
    }

    HikariDataSource(config).also { dataSource ->
        runFlyway(dataSource)
        Database.connect(dataSource)
    }
}

private fun runFlyway(dataSource: HikariDataSource) {
    val flyway = Flyway.configure().dataSource(dataSource).load()
    try {
        flyway.info()
        flyway.migrate()
    } catch (e: Exception) {
        throw e
    }
}

