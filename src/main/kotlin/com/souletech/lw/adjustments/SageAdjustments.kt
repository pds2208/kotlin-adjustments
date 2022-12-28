package com.souletech.lw.adjustments

import arrow.core.Either
import arrow.core.computations.either
import com.souletech.lw.cost.CostPrice
import com.souletech.lw.errors.AdjustmentError
import com.souletech.lw.stock.StockInfo
import com.souletech.lw.stock.UpdateStock
import com.souletech.lw.util.Configuration

class AddSageAdjustment(val costPrice: CostPrice, val updateStock: UpdateStock, val config: Configuration) : AddAdjustment  {

    override suspend fun addAdjustmentIn(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ): Either<AdjustmentError, UpdateStock.UpdateStockResponse> {
        return addAdjustment(StockInfo(date, Adjustments.AdjustmentType.IN, quantity, stockCode, reference, batch))
    }

    override suspend fun addAdjustmentOut(
        date: String,
        stockCode: String,
        reference: String,
        batch: String,
        quantity: Double
    ): Either<AdjustmentError, UpdateStock.UpdateStockResponse> {
        return addAdjustment(StockInfo(date, Adjustments.AdjustmentType.OUT, quantity, stockCode, reference, batch))
    }

    private suspend fun addAdjustment(info: StockInfo): Either<AdjustmentError, UpdateStock.UpdateStockResponse> {
        return  either {
            val costPrice = costPrice.getCostPrice(info.stockCode).bind()
            updateStock.updateStock(info, costPrice).bind()
        }
    }

}

