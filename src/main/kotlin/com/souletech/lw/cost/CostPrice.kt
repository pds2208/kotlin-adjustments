package com.souletech.lw.cost

interface CostPrice {
    suspend fun getCostPrice(stockCode: String): Result<SageCostPrice.SageResponse>
}