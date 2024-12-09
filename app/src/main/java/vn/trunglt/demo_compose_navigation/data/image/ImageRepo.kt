package vn.trunglt.demo_compose_navigation.data.image

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLConnection

typealias FilePath = String

interface ImageRepo {
    suspend fun fetchImage(url: String): FilePath
}

class ImageRepoImpl(
    val imageFileDataSource: ImageFileDataSource
) : ImageRepo {
    override suspend fun fetchImage(url: String): FilePath {
        return withContext(Dispatchers.IO) {
            val cacheFileName = "external-image.jpg"
            val cacheFile = imageFileDataSource.getFile(cacheFileName)
            val cacheFilePath = cacheFile.absolutePath
            if (imageFileDataSource.isFileExisted(cacheFile)) {
                return@withContext cacheFilePath
            }
            val remoteUrl = URL(url)
            val conn: URLConnection? = remoteUrl.openConnection()
            conn?.connect()
            conn?.getInputStream()?.use { remoteInputStream ->
                imageFileDataSource.writeFile(remoteInputStream, cacheFileName)
            }
            return@withContext cacheFilePath
        }
    }
}