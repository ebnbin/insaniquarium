package dev.ebnbin.insaniquarium.tool.util

import java.io.File
import java.nio.file.attribute.FileTime
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zip(inputDirectory: File, outputFile: File) {
    outputFile.delete()
    ZipOutputStream(outputFile.outputStream()).use { zipOutputStream ->
        inputDirectory.walk()
            .filter { it.isFile && it.name != ".DS_Store" }
            .sorted()
            .forEach { file ->
                val byteArray = file.readBytes()
                val zipEntry = ZipEntry(file.relativeTo(inputDirectory).path).also { zipEntry ->
                    zipEntry.time = 0L
                    val fileTime = FileTime.fromMillis(0L)
                    zipEntry.lastModifiedTime = fileTime
                    zipEntry.lastAccessTime = fileTime
                    zipEntry.creationTime = fileTime
                    val size = file.length()
                    zipEntry.size = size
                    zipEntry.compressedSize = size
                    zipEntry.crc = CRC32().also { it.update(byteArray) }.value
                    zipEntry.method = ZipEntry.STORED
                }
                zipOutputStream.putNextEntry(zipEntry)
                zipOutputStream.write(byteArray)
                zipOutputStream.closeEntry()
            }
    }
}
