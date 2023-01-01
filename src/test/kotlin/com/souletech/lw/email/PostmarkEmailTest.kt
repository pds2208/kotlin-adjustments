package com.souletech.lw.email

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class PostmarkEmailTest {

    @Test
    fun sendEmail() {
        postMarkEmail.sendEmail(
            Email.StockCode("1234"), Email.Message("Hello from email test"), Email.MaximumErrors(2)
        )
            .onFailure { error -> fail(error.message) }
            .onSuccess { result -> assert(result.successful).also { assert(result.status == 0) } }
    }
}