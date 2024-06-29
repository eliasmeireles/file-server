package com.softwareplace.fileserver.service

import com.softwareplace.fileserver.file.exception.FileNotFoundException
import com.softwareplace.fileserver.file.exception.FileStorageException
import com.softwareplace.fileserver.rest.model.DataRest
import com.softwareplace.jsonlogger.log.kLogger
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService {

    private final val baseFilePath = "${System.getProperty("user.home")}/file-server"
    private val fileStorageLocation: Path = Paths.get(baseFilePath)
        .toAbsolutePath()
        .normalize()

    init {
        try {
            kLogger.info("Target files path: ${fileStorageLocation.toUri()}")
            Files.createDirectories(this.fileStorageLocation)
        } catch (ex: Exception) {
            throw FileStorageException("Could not create the directory where the uploaded files will be stored.", ex)
        }
    }

    fun storeFile(file: Resource, fileName: String): String {
        try {
            val targetLocation = this.fileStorageLocation.resolve(fileName)
            val directory = File(targetLocation.toUri())

            if (!directory.exists()) {
                directory.mkdirs()
            }
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

            return fileName
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        throw FileStorageException("Could not store the file, Please try again!")
    }

    fun loadFileAsResource(filePath: String): Resource {
        try {
            val path = fileStorageLocation.resolve("$baseFilePath/$filePath")
                .toAbsolutePath()
                .normalize()

            val resource = UrlResource(path.toUri())
            return if (resource.exists()) {
                kLogger.info("Loading resource {}, path: {}", filePath, resource.file.absolutePath)
                resource
            } else {
                throw FileNotFoundException("File not found $$filePath")
            }
        } catch (ex: MalformedURLException) {
            throw FileNotFoundException("File not found $$filePath", ex)
        }

    }

    suspend fun list(resource: String?): DataRest {
        val list = mutableListOf<String>()

        val path = fileStorageLocation.resolve("$baseFilePath/${resource ?: ""}")
            .toAbsolutePath()
            .normalize()

        val files = path.toFile().listFiles()

        fun getAllFilesPath(resource: Array<File>?) {
            resource?.forEach {
                if (it.isDirectory) {
                    getAllFilesPath(it.listFiles())
                } else {
                    val basPath = "${fileStorageLocation}/"
                    list.add(it.path.replace(basPath, ""))
                }
            }
        }

        getAllFilesPath(files)

        kLogger.info("Files listed: resource {}, files: {}", resource, files)

        return DataRest(list)
    }
}
