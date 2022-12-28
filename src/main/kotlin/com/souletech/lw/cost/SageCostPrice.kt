package com.souletech.lw.cost

import arrow.core.Either
import com.souletech.lw.errors.CostAdjustmentError
import com.souletech.lw.util.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.*

class SageCostPrice(val configuration: Configuration) : CostPrice {
    private val logger = KotlinLogging.logger {}

    private val basicAuth = { username: String, password: String ->
        "Basic " + Base64.getEncoder().encodeToString(
            "$username:$password".toByteArray()
        )
    }

    override suspend fun getCostPrice(stockCode: String): Either<CostAdjustmentError, Double> {
        return withContext(Dispatchers.IO) {
            try {
                val parameters = "?select=cost&format=json&where=reference%20eq%20" +
                        URLEncoder.encode("'$stockCode'", StandardCharsets.UTF_8)
                val client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build()
                val request = HttpRequest.newBuilder()
                    .uri(URI(configuration.stock.uri + parameters))
                    .header("Authorization", basicAuth.invoke(configuration.cost.user, configuration.cost.password))
                    .build()

                val data = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
                val sageResponse = Json.decodeFromString<SageResponse>(data.replace("\uFEFF", ""))
                if (sageResponse.resources.isEmpty()) {
                    val msg = "Sage did not return a cost price for $stockCode - does this item exist?"
                    logger.warn { msg }
                    Either.Left(CostAdjustmentError.NoCostPrice(msg))
                } else Either.Right(sageResponse.resources[0].cost)
            } catch (e: Exception) {
                val msg = "Unexpected error: $e.message"
                logger.warn { }
                Either.Left(CostAdjustmentError.GeneralError(msg))
            }
        }
    }

    @Serializable
    data class SageResponse(
        val descriptor: String,
        val url: String,
        val totalResults: Int,
        val startIndex: Int,
        val itemsPerPage: Int,
        val resources: List<SageResources>
    )

    @Serializable
    data class SageResources(
        val url: String,
        val uuid: String,
        val httpStatus: String,
        val descriptor: String,
        val cost: Double
    )
}