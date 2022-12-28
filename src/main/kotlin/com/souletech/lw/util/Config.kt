package com.souletech.lw.util

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

data class Database(
    val url: String,
    val userName: String,
    val password: String,
    val driverClassName: String,
)

data class ResumeAdjustment(
    val initialDelay: Int,
    val fixedDelay: Int,
)

data class AdjustmentUpdate(
    val initialDelay: Int,
    val fixedDelay: Int,
)

data class Cost(
    val stockURI: String,
    val stockLevels: String,
    val password: String,
    val user: String,
    val getCostPrice: Boolean,
)

data class Email(
    val senderEmail: String,
    val receiverEmail: String,
    val subject: String,
    val apiKey: String,
)

data class Stock(
    val uri: String,
    val apiKey: String,
)

data class Adjustments(
    val adjustmentTimeout: Long,
    val maximumErrors: Int,
    val pausePeriod: Int,
)

data class Configuration(
    val database: Database,
    val resumeAdjustment: ResumeAdjustment,
    val adjustmentUpdate: AdjustmentUpdate,
    val cost: Cost,
    val email: Email,
    val stock: Stock,
    val adjustments: Adjustments,
)


val config =
    ConfigLoaderBuilder.default().addResourceSource("/application-dev.yml").build().loadConfigOrThrow<Configuration>()