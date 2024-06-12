package com.softwareplace.fileserver.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.fileserver.properties.AppProperties
import com.softwareplace.fileserver.rest.model.RunnerConfigRest
import com.softwareplace.jsonlogger.log.kLogger
import com.softwareplace.springsecurity.util.ReadFilesUtils
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service

@Service
class StressRunnerService(
    private val properties: AppProperties,
    private val objectMapper: ObjectMapper,
    private val systemLogger: SystemLogger,
    private val resourceLoader: ResourceLoader,
) {

    private val stackValues = mutableListOf<BigObject>()

    @PostConstruct
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(2000)
            runnerStarter()
        }
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(15000)
            cleanup()
        }
    }


    private fun cleanup() {
        val runnerConfig = gerRunnerConfigRest()
        if (runnerConfig.cleanObjects) {
            stackValues.clear()
            kLogger.info("Stack objects cleaned.")
            Thread.sleep(15000)
        } else {
            Thread.sleep(1000)
        }

        cleanup()
    }

    @Throws(Exception::class)
    private suspend fun runnerStarter() {
        try {
            val runnerConfig = gerRunnerConfigRest()
            run(runnerConfig)
        } catch (error: Exception) {
            kLogger.error("Failed to load runner configuration", error)
        }
    }

    private fun gerRunnerConfigRest(): RunnerConfigRest {
        val resource =
            ReadFilesUtils.readFileBytes(resourceLoader, "${properties.storagePath}config/runner.json")
        return try {
            objectMapper.readValue(resource, object : TypeReference<RunnerConfigRest>() {})
        } catch (error: Exception) {
            val asMap = objectMapper.readValue(resource, object : TypeReference<Map<String, Any>>() {})
            RunnerConfigRest(
                asMap["fibonacci"] as Int,
                asMap["objectSizeGenerator"] as Int,
                asMap["runNext"] as Boolean,
                asMap["cleanObjects"] as Boolean
            )
        }
    }

    private suspend fun run(runnerConfig: RunnerConfigRest) {
        if (runnerConfig.runNext) {
            fibonacciRunner(runnerConfig)
        }
        withContext(Dispatchers.IO) {
            Thread.sleep(1000)
        }
        runnerStarter()
    }

    private fun fibonacciRunner(config: RunnerConfigRest) {
        kLogger.info("Starting fibonacci calculation...")
        val startTime = System.currentTimeMillis()
        kLogger.info("Fibonacci value {}", fibonacci(config.fibonacci.toLong()))
        val endTime = System.currentTimeMillis()
        kLogger.info("Total execution time: " + (endTime - startTime) + "ms")
        kLogger.info("Starting generating objects")
        val elements = createObjects(config.objectSizeGenerator)
        stackValues.addAll(elements)
        kLogger.info("Objects created: {}. Total allocated: {}", elements.size, stackValues.size)
    }

    fun fibonacci(n: Long): Long {
        return if (n <= 1) n
        else fibonacci(n - 1) + fibonacci(n - 2)
    }

    data class BigObject(val array: IntArray = IntArray(100))

    fun createObjects(n: Int): List<BigObject> {
        val list = mutableListOf<BigObject>()
        for (i in 0 until n) {
            list.add(BigObject())
        }
        systemLogger.systemPrint()
        return list
    }
}
