package vn.trunglt.demo_compose_navigation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import vn.trunglt.demo_compose_navigation.MainApplication
import vn.trunglt.demo_compose_navigation.Profile
import vn.trunglt.demo_compose_navigation.data.image.ImageFileDataSourceImpl
import vn.trunglt.demo_compose_navigation.data.image.ImageRepoImpl

@Composable
fun RunOneLaunchedEffect(
    block: suspend CoroutineScope.() -> Unit
): Unit = LaunchedEffect(key1 = null) {
    block.invoke(this)
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profile: Profile,
    currentMillis: Long,
    onProfileClick: (Profile) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = profile.shortFullName + " " + currentMillis,
        )
        Text(
            text = profile.company,
            modifier = Modifier.clickable {
                onProfileClick.invoke(profile)
            }
        )
        val bitmap = loadExternalImage(
            imageRepo = ImageRepoImpl(ImageFileDataSourceImpl(MainApplication.instance)),
            url = "https://eoimages.gsfc.nasa.gov/images/imagerecords/73000/73751/world.topo.bathy.200407.3x5400x2700.png"
        )
        bitmap.value?.let { Image(bitmap = it, contentDescription = null) }
    }
}