package com.keelim.cnubus.ui.screen.map.screen.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8
import com.keelim.core.data.model.Location

@Composable
fun MapRoute(
    viewModel: MapViewModel = hiltViewModel(),
) = trace("MapRoute") {
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    MapScreen(
        locations = locations,
    )
}

@Composable
fun MapScreen(
    locations: List<MapState>,
) = trace("MapScreen") {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(Location.defaultLocation().latLng, 25f)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = space24,
                horizontal = space24,
            ),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(space8),
                ) {
                    Text(
                        text = "CNUBUS",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            locations.fastForEach { marker ->
                CustomMarker(marker = marker)
            }
        }
    }
}

@Composable
private fun CustomMarker(
    marker: MapState,
    markerState: MarkerState = rememberUpdatedMarkerState(position = marker.position),
) {
    MarkerInfoWindowContent(
        state = markerState,
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

@Preview(showBackground = true)
@Composable
private fun PreviewMapScreen() {
    MapScreen(
        locations = listOf(
            MapState(
                name = "",
                latlng = Location.defaultLocation().latLng,
                itemSnippet = "",
                imageUrl = "",
            ),
        ),
    )
}
