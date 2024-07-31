package dev.ebnbin.kgdx.util

import com.badlogic.gdx.files.FileHandle
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

fun FileHandle.readByteArrayFromZip(fileName: String): ByteArray {
    ZipInputStream(read()).use { zipInputStream ->
        var zipEntry = zipInputStream.nextEntry
        while (zipEntry != null) {
            if (zipEntry.name == fileName) {
                return ByteArrayOutputStream().use { byteArrayOutputStream ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var len: Int
                    while (zipInputStream.read(buffer, 0, buffer.size).also { len = it } != -1) {
                        byteArrayOutputStream.write(buffer, 0, len)
                    }
                    zipInputStream.closeEntry()
                    byteArrayOutputStream.toByteArray()
                }
            }
            zipInputStream.closeEntry()
            zipEntry = zipInputStream.nextEntry
        }
    }
    error(Unit)
}
