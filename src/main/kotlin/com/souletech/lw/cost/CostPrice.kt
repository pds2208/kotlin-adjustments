package com.souletech.lw.cost

import arrow.core.Either
import com.souletech.lw.errors.CostAdjustmentError

interface CostPrice {
    suspend fun getCostPrice(stockCode: String): Result<SageCostPrice.SageResponse>
}