package vn.trunglt.demo_compose_navigation.data.image

import android.content.Context
import vn.trunglt.demo_compose_navigation.utils.io
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream

interface ImageFileDataSource {
    @Throws(FileNotFoundException::class)
    suspend fun isFileExisted(file: File): Boolean
    suspend fun getFile(fileName: String): File
    suspend fun writeFile(inputStream: InputStream, fileName: String): Boolean
    suspend fun deleteFile(fileName: String)
}

class ImageFileDataSourceImpl(private val context: Context) : ImageFileDataSource {
    override suspend fun isFileExisted(file: File): Boolean {
        return io { file.exists() }
    }

    override suspend fun getFile(fileName: String): File {
        return io { File(context.cacheDir, fileName) }
    }

    override suspend fun writeFile(inputStream: InputStream, fileName: String): Boolean {
        return io {
            try {
                val cacheFile = File(context.cacheDir, fileName)
                val localFileOutputStream = FileOutputStream(cacheFile)
                inputStream.copyTo(
                    out = localFileOutputStream,
                    bufferSize = 8 * 1024
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun deleteFile(fileName: String) {
        return io {
            val cacheFile = getFile(fileName)
            if (cacheFile.exists()) {
                cacheFile.delete()
            }
        }
    }
}