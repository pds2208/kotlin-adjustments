package com.souletech.lw.stock

import arrow.core.Either
import com.souletech.lw.errors.StockAdjustmentError
import kotlinx.serialization.Serializable

interface UpdateStock {
    fun updateStock(stockInfo: StockInfo, costPrice: Double): Either<StockAdjustmentError, UpdateStockResponse>

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