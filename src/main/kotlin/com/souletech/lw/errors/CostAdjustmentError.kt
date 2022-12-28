package com.souletech.lw.errors

sealed class CostAdjustmentError: AdjustmentError() {
    data class NoCostPrice(val errorMessage: String) : CostAdjustmentError()
    data class GeneralError(val errorMessage: String) : CostAdjustmentError()
}