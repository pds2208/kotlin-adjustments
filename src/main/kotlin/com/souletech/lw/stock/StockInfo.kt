package com.souletech.lw.stock

import com.souletech.lw.adjustments.Adjustments

data class StockInfo(
    val date: String,
    val adjustmentType: Adjustments.AdjustmentType,
    val quantity: Double,
    val stockCode: String,
    val reference: String,
    val batch: String,
)