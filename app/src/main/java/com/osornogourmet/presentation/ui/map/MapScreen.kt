package com.osornogourmet.presentation.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.presentation.theme.*

/**
 * Pantalla del Mapa con Google Maps.
 * Muestra marcadores de todos los locales de comida en Osorno.
 * Integra la API de Google Maps via maps-compose.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    foodPlaces: List<FoodPlace>,
    initialFocusPlace: FoodPlace? = null,
    routePlaces: List<FoodPlace>? = null,
    onBack: () -> Unit
) {
    // Centro de Osorno, Chile
    val osornoCenter = LatLng(-40.5726, -73.1350)

    // Si hay un local focalizado, centrar en él
    val initialPosition = if (initialFocusPlace != null) {
        LatLng(initialFocusPlace.latitude, initialFocusPlace.longitude)
    } else {
        osornoCenter
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f)
    }

    var selectedPlace by remember { mutableStateOf<FoodPlace?>(null) }

    // Determinar qué locales mostrar
    val placesToShow = routePlaces ?: foodPlaces

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            if (routePlaces != null) "Ruta en Mapa" else "Mapa de Osorno",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${placesToShow.size} locales",
                            style = MaterialTheme.typography.bodySmall,
                            color = SubtleText
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CreamBackground
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = false
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = false,
                    mapToolbarEnabled = true
                )
            ) {
                // Marcadores de cada local
                placesToShow.forEach { place ->
                    val position = LatLng(place.latitude, place.longitude)
                    val markerColor = when (place.category) {
                        "Restaurante" -> BitmapDescriptorFactory.HUE_RED
                        "Café" -> BitmapDescriptorFactory.HUE_ORANGE
                        "Pastelería" -> BitmapDescriptorFactory.HUE_ROSE
                        "Comida Rápida" -> BitmapDescriptorFactory.HUE_YELLOW
                        "Mercado" -> BitmapDescriptorFactory.HUE_GREEN
                        "Bar" -> BitmapDescriptorFactory.HUE_VIOLET
                        "Cocina Casera" -> BitmapDescriptorFactory.HUE_CYAN
                        "Emporio" -> BitmapDescriptorFactory.HUE_AZURE
                        else -> BitmapDescriptorFactory.HUE_RED
                    }

                    Marker(
                        state = MarkerState(position = position),
                        title = place.name,
                        snippet = "${place.category} · ⭐ ${place.rating}",
                        icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                        onClick = {
                            selectedPlace = place
                            false // false permite mostrar la ventana de info
                        }
                    )
                }

                // Dibujar línea de ruta si es una ruta
                if (routePlaces != null && routePlaces.size >= 2) {
                    Polyline(
                        points = routePlaces.map { LatLng(it.latitude, it.longitude) },
                        color = CrimsonPrimary,
                        width = 8f
                    )
                }
            }

            // Tarjeta del local seleccionado
            selectedPlace?.let { place ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = place.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = CrimsonPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "${place.rating}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CrimsonPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = GoldAccent.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            place.category,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GoldAccent,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            IconButton(onClick = { selectedPlace = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = place.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = SubtleText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = SubtleText, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(place.address, style = MaterialTheme.typography.bodySmall, color = SubtleText)
                        }
                        if (place.phone.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = SubtleText, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(place.phone, style = MaterialTheme.typography.bodySmall, color = SubtleText)
                            }
                        }
                        if (place.openingHours.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = SubtleText, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(place.openingHours, style = MaterialTheme.typography.bodySmall, color = SubtleText)
                            }
                        }
                    }
                }
            }

            // Leyenda de colores
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        "Categorías",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LegendItem("🍖 Restaurante")
                    LegendItem("☕ Café")
                    LegendItem("🍰 Pastelería")
                    LegendItem("🍔 Comida Rápida")
                    LegendItem("🏪 Mercado")
                }
            }
        }
    }
}

@Composable
private fun LegendItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(vertical = 1.dp)
    )
}


