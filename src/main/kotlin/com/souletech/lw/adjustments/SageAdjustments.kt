package com.souletech.lw.adjustments

import com.souletech.lw.cost.CostPrice
import com.souletech.lw.stock.StockInfo
import com.souletech.lw.stock.UpdateStock
import mu.KotlinLogging

class SageAdjustments(private val costPrice: CostPrice, private val updateStock: UpdateStock) : Adjustments {

    override suspend fun addAdjustmentIn(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ) {
        return addAdjustment(StockInfo(date, Adjustments.AdjustmentType.IN, quantity, stockCode, reference, batch))
    }

    override suspend fun addAdjustmentOut(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ) {
        return addAdjustment(StockInfo(date, Adjustments.AdjustmentType.OUT, quantity, stockCode, reference, batch))
    }

    private suspend fun addAdjustment(info: StockInfo) {
        val logger = KotlinLogging.logger {}

        costPrice.getCostPrice(info.stockCode)
            .onFailure { e -> logger.warn { "Error retrieving a cost price for ${info.stockCode}: ${e.message}" } }
            .onSuccess { result ->
                if (result.resources.isEmpty())
                    logger.warn { "Sage did not return a cost price for ${info.stockCode} - does this item exist?" }
                else
                    updateStock.updateStock(info, result.resources[0].cost)
            }
    }

}

