package com.souletech.lw.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ConfigTest {

    @Test
    @DisplayName("Test database username is set")
    fun testUserName() {
        val userName = config.database.userName
        val expected = "lw"
        assertEquals(expected, userName)
    }

    @Test
    @DisplayName("Test the sender email is set to info@lewisandwood.co.uk")
    fun testFromEmail() {
        val email = config.email.senderEmail
        val expected = "info@lewisandwood.co.uk"
        assertEquals(expected, email)
    }
}