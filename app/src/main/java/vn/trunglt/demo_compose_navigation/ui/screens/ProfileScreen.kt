package vn.trunglt.demo_compose_navigation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import vn.trunglt.demo_compose_navigation.Profile


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profile: Profile,
    onProfileClick: (Profile) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = profile.shortFullName,
        )
        Text(
            text = profile.company,
            modifier = Modifier.clickable {
                onProfileClick.invoke(profile)
            }
        )
        val bitmap by loadExternalImage(url = "https://eoimages.gsfc.nasa.gov/images/imagerecords/73000/73751/world.topo.bathy.200407.3x5400x2700.png")
        bitmap?.let { Image(bitmap = it, contentDescription = null) }
    }
}