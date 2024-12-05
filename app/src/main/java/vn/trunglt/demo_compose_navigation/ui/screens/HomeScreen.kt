package vn.trunglt.demo_compose_navigation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import vn.trunglt.democustomviewcompose.CameraPreviewScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSeeProfileClick: () -> Unit
) {
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
    Column(modifier = modifier.fillMaxSize()) {
        Box {
            CameraPreviewScreen(modifier)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onSeeProfileClick.invoke()
            }) {
            Text(text = "See my profile")
        }
    }
}