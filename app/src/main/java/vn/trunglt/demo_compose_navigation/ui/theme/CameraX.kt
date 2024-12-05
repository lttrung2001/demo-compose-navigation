package vn.trunglt.democustomviewcompose

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CameraPreviewScreen(modifier: Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val lensFacing = remember {
        CameraSelector.LENS_FACING_FRONT
    }
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = remember {
        CameraSelector.Builder().requireLensFacing(lensFacing).build()
    }
    val scope = rememberCoroutineScope { Dispatchers.IO }
    LaunchedEffect(lensFacing) {
        val preview = Preview.Builder().build()
        val imageAnalysisUseCase = ImageAnalysis.Builder().build()
        imageAnalysisUseCase.setAnalyzer({ runnable ->
            scope.launch { runnable.run() }
        }, { imageProxy ->
//            println("width: ${imageProxy.width}, height: ${imageProxy.height}")
            imageProxy.close()
        })
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraxSelector,
            preview,
            imageAnalysisUseCase
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    Box(modifier = modifier) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Oval()
    }
}