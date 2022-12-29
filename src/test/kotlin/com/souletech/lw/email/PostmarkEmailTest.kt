package com.souletech.lw.email

import com.souletech.lw.util.config
import org.junit.jupiter.api.Test

class PostmarkEmailTest {

    @Test
    fun sendEmail() {
        val email = PostmarkEmail(config)
        val result = email.sendEmail("1234", "Hello from email test", 2)
        assert(result.isSuccess).also { result.onSuccess { s -> assert( s.status == 0) } }
    }
}