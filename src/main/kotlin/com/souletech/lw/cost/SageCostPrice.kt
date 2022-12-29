package com.souletech.lw.cost

import com.souletech.lw.util.Configuration
import kotlinx.coroutines.future.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.*

class SageCostPrice(val configuration: Configuration) : CostPrice {

    private val basicAuth = { username: String, password: String ->
        "Basic " + Base64.getEncoder().encodeToString("$username:$password".toByteArray())
    }

    override suspend fun getCostPrice(stockCode: String): Result<SageResponse> {
        return runCatching {
            val parameters = "?select=cost&format=json&where=reference%20eq%20" +
                    URLEncoder.encode("'$stockCode'", StandardCharsets.UTF_8)
            val client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build()
            val request = HttpRequest.newBuilder()
                .uri(URI(configuration.stock.uri + parameters))
                .header("Authorization", basicAuth.invoke(configuration.cost.user, configuration.cost.password))
                .build()
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { x ->
                    x.body()?.let { Json.decodeFromString<SageResponse>(it.replace("\uFEFF", "")) }
                }.await()
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