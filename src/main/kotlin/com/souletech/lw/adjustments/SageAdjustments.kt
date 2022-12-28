package com.souletech.lw.adjustments

import com.souletech.lw.stock.StockInfo
import com.souletech.lw.stock.UpdateStockResponse
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

data class Status(
    val isSuccessful: Boolean,
    val response: UpdateStockResponse?,
    val errorMessage: String?
)

class AddSageAdjustment() {

    fun addAdjustmentIn(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ): Status {
        return addAdjustment(StockInfo(date, Adjustments.AdjustmentType.IN, quantity, stockCode, reference, batch))
    }

    fun addAdjustmentOut(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ): Status {
        return addAdjustment(StockInfo(date, Adjustments.AdjustmentType.OUT, quantity, stockCode, reference, batch))
    }

    private fun addAdjustment(info: StockInfo): Status {
        return try {
            val r: Unit = costPrice
                .getCostPrice(info.stockCode())
                .thenCompose { x -> updateStock.updateStock(info, x.orElse(0.0)) }
                .get(config.adjustmentTimeout(), TimeUnit.SECONDS)
            Status(true, r, Optional.empty<Any>())
        } catch (e: InterruptedException) {
            val error = if (e.message == null) e.toString() else e.message!!
            Status(false, Optional.empty<Any>(), Optional.of(error))
        } catch (e: TimeoutException) {
            val error = if (e.message == null) e.toString() else e.message!!
            Status(false, Optional.empty<Any>(), Optional.of(error))
        } catch (e: ExecutionException) {
            val error = if (e.message == null) e.toString() else e.message!!
            Status(false, Optional.empty<Any>(), Optional.of(error))
        }
    }
}