package com.osornogourmet.presentation.ui.map

import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.presentation.theme.*
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.PolylineOptions
import android.graphics.Color as AndroidColor

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapLibre.getInstance(context)
        MapView(context)
    }

    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            mapView.onDestroy()
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> { }
                else -> { }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    foodPlaces: List<FoodPlace>,
    initialFocusPlace: FoodPlace? = null,
    routePlaces: List<FoodPlace>? = null,
    onBack: () -> Unit
) {
    val osornoCenter = LatLng(-40.5726, -73.1350)

    val initialPosition = if (initialFocusPlace != null) {
        LatLng(initialFocusPlace.latitude, initialFocusPlace.longitude)
    } else {
        osornoCenter
    }

    var selectedPlace by remember { mutableStateOf<FoodPlace?>(null) }
    val placesToShow = routePlaces ?: foodPlaces

    // Jawg Dark Style URL
    val styleUrl = "https://api.jawg.io/styles/jawg-dark.json?access-token=eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6IjU3YjM5NzYwOTM1MTRkZjM4MzZlY2Q0OGY0ZjY3MDRlIiwiaCI6Im11cm11cjY0In0="

    val mapView = rememberMapViewWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            if (routePlaces != null) "Ruta en Mapa" else "Mapa de Osorno",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            "${placesToShow.size} locales",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = GoldAccent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // MapLibre AndroidView
            AndroidView(
                factory = { 
                    mapView.apply {
                        getMapAsync { mapLibreMap ->
                            mapLibreMap.setStyle(styleUrl) { style ->
                                // Map is ready
                                mapLibreMap.cameraPosition = CameraPosition.Builder()
                                    .target(initialPosition)
                                    .zoom(14.0)
                                    .build()
                                    
                                // Add markers
                                placesToShow.forEach { place ->
                                    val marker = MarkerOptions()
                                        .position(LatLng(place.latitude, place.longitude))
                                        .title(place.name)
                                        .snippet("${place.category} · ⭐ ${place.rating}")
                                    
                                    mapLibreMap.addMarker(marker)
                                }
                                
                                // Polyline
                                if (routePlaces != null && routePlaces.size >= 2) {
                                    val polylineOptions = PolylineOptions()
                                        .addAll(routePlaces.map { LatLng(it.latitude, it.longitude) })
                                        .color(AndroidColor.parseColor("#D4AF37")) // GoldAccent
                                        .width(5f)
                                    mapLibreMap.addPolyline(polylineOptions)
                                }

                                mapLibreMap.setOnMarkerClickListener { marker ->
                                    selectedPlace = placesToShow.find { it.name == marker.title }
                                    false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Selected Place Card
            selectedPlace?.let { place ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
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
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "${place.rating}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = GoldAccent,
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
                                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = place.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(place.address, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        if (place.phone.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(place.phone, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                        if (place.openingHours.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(place.openingHours, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            // Legend
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        "Categorías",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
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
        color = TextSecondary,
        modifier = Modifier.padding(vertical = 1.dp)
    )
}
