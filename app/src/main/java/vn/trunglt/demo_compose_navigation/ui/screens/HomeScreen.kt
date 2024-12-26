package vn.trunglt.demo_compose_navigation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.trunglt.demo_compose_navigation.data.image.ImageRepo
import vn.trunglt.demo_compose_navigation.data.image.decodeSampledBitmapFromFile
import vn.trunglt.democustomviewcompose.CameraPreviewScreen
import java.io.File

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSeeProfileClick: () -> Unit
) {
//    println("recomposition HomeScreen")
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    // Test DisposableEffect
    DisposableEffect(key1 = lifecycleOwner) {
        println("run DisposableEffect")
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    println("ON_CREATE")
                }

                Lifecycle.Event.ON_START -> {
                    println("ON_START")
                }

                Lifecycle.Event.ON_RESUME -> {
                    println("ON_RESUME")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    println("ON_PAUSE")
                }

                Lifecycle.Event.ON_STOP -> {
                    println("ON_STOP")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    println("ON_DESTROY")
                }

                else -> {

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            println("onDispose")
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            CameraPreviewScreen(Modifier)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onSeeProfileClick.invoke()
            }) {
            Text(text = "See my profile")
        }
        var currentText by rememberSaveable {
            mutableStateOf("")
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = currentText,
            onValueChange = {
                currentText = it
                // Test rememberCoroutineScope
                scope.launch {
                    delay(2000)
                    println("done launch $currentText without cancelling when recomposition")
                }
            })
        // Test checking skippable composable
        TestComposable()
        // Side effect will execute after successful recomposition
        // This effect use to send newest data/state of composable to others (non-composable)
        // Why we don't use LaunchedEffect? -> LaunchedEffect not ensure that it can send data to others
        // because of coroutine can be cancelled when composable recomposition
//        val analytics: FirebaseAnalytics = remember {
//            FirebaseAnalytics()
//        }
//        SideEffect {
//            analytics.setUserProperty("userType", user.userType)
//        }
    }
}

@Composable
fun TestComposable(modifier: Modifier = Modifier) {
    println("recomposition TestOnly")
    Box {
        Text(text = "Test")
    }
}

@Composable
fun loadExternalImage(
    imageRepo: ImageRepo,
    url: String,
): State<ImageBitmap?> {
    return produceState<ImageBitmap?>(initialValue = null) {
        val bitmap = withContext(Dispatchers.IO) {
            println("Fetch image")
            decodeSampledBitmapFromFile(
                file = File(imageRepo.fetchImage(url)),
                reqWidth = 500,
                reqHeight = 500,
            )
        }
        val imageBitmap = bitmap.asImageBitmap()
        value = imageBitmap
        awaitDispose {
            println("Dispose image")
//            bitmap.recycle()
        }
    }
}