package com.keelim.cnubus.ui.screen.map.screen.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.keelim.core.data.model.Location

@Composable
fun MapRoute() {
    MapScreen()
}

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) = trace("MapScreen") {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(Location.defaultLocation().latLng, 25f)
    }
    GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
        val locations by viewModel.locations.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current,
        )
        locations.fastForEach { marker ->
            MarkerInfoWindowContent(
                state = MarkerState(position = marker.position),
                title = marker.title,
                snippet = marker.itemSnippet,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = Modifier.padding(top = 6.dp),
                        text = marker.title,
                        fontWeight = FontWeight.Bold,
                    )
                    AsyncImage(
                        model = marker.imageUrl,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .border(
                                BorderStroke(3.dp, color = Color.Gray),
                                shape = RectangleShape,
                            ),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMapScreen() {
    MapScreen()
}
