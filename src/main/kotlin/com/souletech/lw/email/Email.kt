package com.souletech.lw.email


fun interface Email {

    @JvmInline
    value class StockCode(val value: String)

    @JvmInline
    value class Message(val value: String)

    @JvmInline
    value class MaximumErrors(val value: Int)

    fun sendEmail(stockCode: StockCode, message: Message, maxErrors: MaximumErrors): Result<EmailRecord>

    data class EmailRecord(val successful: Boolean, val status: Int, val errorMessage: String)

}