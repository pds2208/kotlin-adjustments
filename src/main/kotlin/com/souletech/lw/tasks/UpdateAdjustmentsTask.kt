package com.souletech.lw.tasks

import com.souletech.lw.adjustments.Adjustments
import com.souletech.lw.adjustments.Statistics
import com.souletech.lw.email.postMarkEmail
import com.souletech.lw.util.Utils.Companion.formatDate
import com.souletech.lw.util.config
import mu.KotlinLogging
import java.sql.Date
import java.util.*
import com.souletech.lw.email.Email.*

class UpdateAdjustmentsTask(
    private val stats: Statistics,
    private val adjustments: Adjustments,
    addAdjustment: Adjustments
) {
    val logger = KotlinLogging.logger {}

    fun updateAdjustments() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (stats.isPaused()) {
                    logger.info("AdjustmentTask: Adjustments are paused, skipping")
                    return
                }
                val adjustment = adjustments.getNextAdjustment()
                if (adjustment.isEmpty()) {
                    logger.debug { "No adjustments to process" }
                    return
                }
                val adj: Unit = adjustment.get()
                logger.info(
                    "Processing adjustment, stockCode: " + adj.stockCode() + " Type: " + adj.adjustmentType().name()
                )
                val adjustmentDate: Unit = adj.adjustmentDate()
                    .map(formatDate)
                    .orElse(formatDate.apply(Date(System.currentTimeMillis())))
                val result: Unit = when (adj.adjustmentType) {
                    IN -> addAdjustment.addAdjustmentIn(
                        adjustmentDate, adj.stockCode(), adj.referenceText().orElse(""),
                        adj.batch().orElse(""), adj.amount()
                    )

                    adj_out -> addAdjustment.addAdjustmentOut(
                        adjustmentDate, adj.stockCode(), adj.referenceText().orElse(""),
                        adj.batch().orElse(""), adj.amount()
                    )
                }
                if (result.isSuccessful() && result.response().isPresent()) {
                    val status: Unit = result.response().get()
                    if (status.success() && status.code() === 200) {
                        setUpdated(adj)
                        return
                    }
                }
                setFailed(adj, result)
            }
        }, config.adjustmentUpdate.initialDelay, config.adjustmentUpdate.fixedDelay)
    }

    private fun setUpdated(adj: Adjustments.Adjustment) {
        adjustments.setUpdated(adj.id())
        stats.incrementUpdated()
        val i = stats.decrementToUpdate()
        if (i < 0) stats.setToUpdate(0)
        logger.info("Updated Sage record for product: " + adj.stockCode())
    }

    private fun setFailed(adj: Adjustments.Adjustment, status: AddAdjustment.Status) {
        val numRetries: Int = adjustments.setFailed(adj)
        stats.incrementFailures()
        logger.warn { "Update FAILED for: " + adj.stockCode() + ", " + status.errorMessage().orElse("Unknown") }
        if (numRetries >= config.adjustments.maximumErrors) {
            logger.warn { "Error threshold of " + config.adjustments.maximumErrors + " has been exceeded, Sending email alert and pausing adjustment" }
            postMarkEmail.sendEmail(
                adj.stockCode(),
                status.errorMessage().orElse("Unknown error"),
                MaximumErrors(config.adjustments.maximumErrors)
            )
            // mark the adjustment as paused so we can move on to another one
            adjustments.pauseAdjustment(adj)
        }
    }
}