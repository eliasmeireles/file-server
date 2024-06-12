package com.softwareplace.fileserver.service

import com.softwareplace.jsonlogger.log.kLogger
import org.springframework.stereotype.Service
import java.lang.management.ManagementFactory

@Service
class SystemLogger {

    fun systemPrint() {
        val threadMXBean = ManagementFactory.getThreadMXBean()
        val runtime = Runtime.getRuntime()

        val maxMemory = runtime.maxMemory()
        val usedMemoryBytes = runtime.totalMemory() - runtime.freeMemory()

        val freeMemory = runtime.maxMemory() - usedMemoryBytes

        val threadCount = threadMXBean.threadCount

        kLogger.info(
            "Free memory: {}, Max memory: {}, Used memory: {}, Thread count: {}, Running threads: {}",
            toMB(freeMemory),
            toMB(maxMemory),
            toMB(usedMemoryBytes),
            threadCount,
            threadMXBean.totalStartedThreadCount
        )
    }

    private fun toMB(value: Long): String {
        return when {
            value < 1024 -> "${value}B"
            value < 1024 * 1024 -> "${value / 1024}KB"
            value < 1024 * 1024 * 1024 -> "${value / 1024 / 1024}MB"
            else -> "${value / 1024 / 1024 / 1024}GB"
        }
    }
}
