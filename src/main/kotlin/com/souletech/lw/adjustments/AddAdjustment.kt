package com.souletech.lw.adjustments

import arrow.core.Either
import com.souletech.lw.errors.AdjustmentError
import com.souletech.lw.stock.UpdateStock

interface AddAdjustment {
    suspend fun addAdjustmentIn(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ): Either<AdjustmentError, UpdateStock.UpdateStockResponse>

    suspend fun addAdjustmentOut(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ): Either<AdjustmentError, UpdateStock.UpdateStockResponse>
}