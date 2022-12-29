package com.souletech.lw.util

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

interface Utils {
    companion object {
        val formatTimestamp = { date: Timestamp? -> SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date) }
        val formatDate = { date: Date? -> SimpleDateFormat("dd/MM/yyyy").format(date) }
    }
}