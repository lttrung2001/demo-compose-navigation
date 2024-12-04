package vn.trunglt.demo_compose_navigation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vn.trunglt.democustomviewcompose.CameraPreviewScreen
import vn.trunglt.democustomviewcompose.Oval

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSeeProfileClick: () -> Unit
) {
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