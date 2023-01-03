package com.souletech.lw.adjustments

interface Adjustments {
    enum class AdjustmentType {
        IN,
        OUT,
    }

     suspend fun addAdjustmentIn(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    )

    suspend fun addAdjustmentOut(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    )

}