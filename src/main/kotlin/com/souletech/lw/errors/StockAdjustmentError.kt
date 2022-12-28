package com.souletech.lw.errors

sealed class StockAdjustmentError : AdjustmentError() {
    data class Interrupted(val errorMessage: String) : StockAdjustmentError()
    data class Timeout(val errorMessage: String) : StockAdjustmentError()
    data class ExecutionAdjustmentError(val errorMessage: String) : StockAdjustmentError()
}