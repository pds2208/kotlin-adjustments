package com.souletech.lw.adjustments

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object Statistics {
    private val totalUpdated: AtomicInteger = AtomicInteger(0)
    private val totalFailures: AtomicInteger = AtomicInteger(0)
    private val totalWithRetries: AtomicInteger = AtomicInteger(0)
    private val toUpdate: AtomicInteger = AtomicInteger(0)
    private val paused: AtomicBoolean = AtomicBoolean(false)

    fun getTotalUpdated(): Int {
        return totalUpdated.get()
    }

    fun setTotalUpdated(totalUpdated: Int) {
        this.totalUpdated.set(totalUpdated)
    }

    fun incrementUpdated() {
        totalUpdated.incrementAndGet()
    }

    fun getTotalFailures(): Int {
        return totalFailures.get()
    }

    fun setTotalFailures(totalFailures: Int) {
        this.totalFailures.set(totalFailures)
    }

    fun incrementFailures() {
        totalFailures.incrementAndGet()
    }

    fun decrementFailures(): Int {
        return totalFailures.decrementAndGet()
    }

    fun getTotalWithRetries(): Int {
        return totalWithRetries.get()
    }

    fun setTotaWithRetries(totalFailures: Int) {
        totalWithRetries.set(totalFailures)
    }

    fun incrementTotalWithRetries(): Int {
        return totalWithRetries.incrementAndGet()
    }

    fun getToUpdate(): Int {
        return toUpdate.get()
    }

    fun setToUpdate(totalFailures: Int) {
        toUpdate.set(totalFailures)
    }

    fun incrementToUpdate() {
        toUpdate.incrementAndGet()
    }

    fun decrementToUpdate(): Int {
        return toUpdate.decrementAndGet()
    }

    fun isPaused(): Boolean {
        return paused.get()
    }

    fun setPaused(paused: Boolean) {
        this.paused.set(paused)
    }
}