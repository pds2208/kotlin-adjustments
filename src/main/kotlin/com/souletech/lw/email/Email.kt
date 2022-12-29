package com.souletech.lw.email

interface Email {
    fun sendEmail(stockCode: String, errorMessage: String, maxErrors: Int): Result<EmailRecord>

    data class EmailRecord(val successful: Boolean, val status: Int, val errorMessage: String?)
}