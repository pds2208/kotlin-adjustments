package com.souletech.lw.stock

import kotlinx.serialization.Serializable

interface UpdateStock {
    fun updateStock(stockInfo: StockInfo, costPrice: Double)

    @Serializable
    data class UpdateStockRequest(
        val stockCode: String,
        val quantity: Double,
        val type: Int,
        val date: String,
        val reference: String,
        val details: String,
        val costPrice: Double,
    )

    @Serializable
    data class UpdateStockResponse(
        val success: Boolean,
        val code: Int,
        val response: Boolean,
        val message: String?,
        val reference: String,
        val details: String,
        val costPrice: Double,
    )
}