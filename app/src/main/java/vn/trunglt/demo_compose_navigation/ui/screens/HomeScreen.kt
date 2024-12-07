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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import vn.trunglt.democustomviewcompose.CameraPreviewScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    currentMillis: Long,
    onSeeProfileClick: () -> Unit
) {
    println("recomposition HomeScreen")
    val lifecycleOwner = LocalLifecycleOwner.current
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
            })
        TestOnly()
        LaunchedEffect(key1 = currentText) {
            delay(10000)
            println("done LaunchedEffect $currentText")
        }
    }
}

@Composable
fun TestOnly(modifier: Modifier = Modifier) {
    println("recomposition Box Test")
    Box {
        Text(text = "Test")
    }
}