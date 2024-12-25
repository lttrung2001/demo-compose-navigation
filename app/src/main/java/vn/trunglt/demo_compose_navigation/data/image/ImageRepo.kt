package vn.trunglt.demo_compose_navigation.data.image

import vn.trunglt.demo_compose_navigation.utils.io
import java.net.URL
import java.net.URLConnection

typealias FilePath = String

interface ImageRepo {
    suspend fun fetchImage(url: String): FilePath
    suspend fun deleteLocalImage()
}

class ImageRepoImpl(
    val imageFileDataSource: ImageFileDataSource
) : ImageRepo {
    private val cacheFileName = "external-image.jpg"

    override suspend fun fetchImage(url: String): FilePath {
        val cacheFile = imageFileDataSource.getFile(cacheFileName)
        val cacheFilePath = cacheFile.absolutePath
        if (imageFileDataSource.isFileExisted(cacheFile)) {
            return cacheFilePath
        }
        val remoteInputStream = io {
            val remoteUrl = URL(url)
            val conn: URLConnection? = remoteUrl.openConnection()
            conn?.connect()
            conn?.getInputStream()
        }
        remoteInputStream?.let { imageFileDataSource.writeFile(it, cacheFileName) }
        return cacheFilePath
    }

    override suspend fun deleteLocalImage() {
        io {
            imageFileDataSource.deleteFile(cacheFileName)
        }
    }
}