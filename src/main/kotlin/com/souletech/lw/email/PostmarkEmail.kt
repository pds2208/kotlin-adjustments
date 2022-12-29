package com.souletech.lw.email

import com.postmarkapp.postmark.Postmark
import com.postmarkapp.postmark.client.data.model.message.Message
import com.souletech.lw.email.Email.EmailRecord
import com.souletech.lw.util.Configuration
import io.pebbletemplates.pebble.PebbleEngine
import mu.KotlinLogging
import java.io.StringWriter
import java.io.Writer

class PostmarkEmail(val config: Configuration) : Email {

    private val logger = KotlinLogging.logger {}

    override fun sendEmail(stockCode: String, errorMessage: String, maxErrors: Int): Result<EmailRecord> {
        val client = Postmark.getApiClient(config.email.apiKey)
        val engine = PebbleEngine.Builder().build()
        val template = engine.getTemplate("templates/email.peb")

        val writer: Writer = StringWriter()

        val context = mapOf("stockCode" to stockCode, "maxErrors" to maxErrors, "errorMessage" to errorMessage)

        return runCatching {
            template.evaluate(writer, context)
            val output = writer.toString()

            val message = Message(
                config.email.senderEmail,
                config.email.receiverEmail,
                config.email.subject,
                output
            )

            val response = client.deliverMessage(message)
            logger.info { "email sent" }
            EmailRecord(true, response.errorCode, null)

        }
    }

}