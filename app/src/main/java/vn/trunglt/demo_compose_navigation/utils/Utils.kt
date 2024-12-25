package vn.trunglt.demo_compose_navigation.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> io(block: suspend () -> T) = withContext(Dispatchers.IO) {
    return@withContext block.invoke()
}

suspend fun <T> default(block: suspend () -> T) = withContext(Dispatchers.Default) {
    return@withContext block.invoke()
}