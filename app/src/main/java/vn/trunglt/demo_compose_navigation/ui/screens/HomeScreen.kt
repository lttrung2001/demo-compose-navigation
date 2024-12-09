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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.trunglt.democustomviewcompose.CameraPreviewScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    currentMillis: Long,
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
        TestOnly()
        // Test LaunchedEffect
        LaunchedEffect(key1 = currentText) {
            delay(10000)
            println("done LaunchedEffect $currentText")
        }
        // If keys = null LaunchedEffect wont be cancelled and restart when composition
        // Test rememberUpdatedState
        // rememberUpdatedState + LaunchedEffect with null keys combination
        // -> LaunchedEffect will not be cancelled when recomposition
        // but also can use the latest value of rememberUpdatedState
        val updatedState by rememberUpdatedState(newValue = currentText)
        LaunchedEffect(key1 = null) {
            println("start launchedEffect with key1 = null ${System.currentTimeMillis()}")
            delay(10000)
            println("test rememberUpdatedState currentText: $updatedState with millis: ${System.currentTimeMillis()}")
        }
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
fun TestOnly(modifier: Modifier = Modifier) {
    println("recomposition Box Test")
    Box {
        Text(text = "Test")
    }
}