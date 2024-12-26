package vn.trunglt.demo_compose_navigation.data.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import vn.trunglt.demo_compose_navigation.utils.io
import java.io.File
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

fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

fun decodeSampledBitmapFromFile(
    file: File,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        BitmapFactory.decodeFile(file.absolutePath, this)
    }
}