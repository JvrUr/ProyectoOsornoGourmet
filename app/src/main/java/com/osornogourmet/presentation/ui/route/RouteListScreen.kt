package com.osornogourmet.presentation.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.model.Route
import com.osornogourmet.presentation.theme.*
import com.osornogourmet.presentation.viewmodel.RouteUiState

/**
 * Pantalla de lista de rutas del usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteListScreen(
    uiState: RouteUiState,
    allFoodPlaces: List<FoodPlace>,
    onCreateRoute: () -> Unit,
    onDeleteRoute: (Route) -> Unit,
    onViewRouteOnMap: (Route) -> Unit,
    onBack: () -> Unit
) {
    var routeToDelete by remember { mutableStateOf<Route?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mis Rutas", fontWeight = FontWeight.Bold, color = TextPrimary)
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateRoute,
                containerColor = GoldAccent,
                contentColor = DarkBackground,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear ruta")
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GoldAccent)
            }
        } else if (uiState.routes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🗺️", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "No tienes rutas creadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Crea tu primera ruta gastronómica",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onCreateRoute,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = DarkBackground),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = DarkBackground)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Crear Ruta", color = DarkBackground)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.routes) { route ->
                    RouteCard(
                        route = route,
                        foodPlaces = allFoodPlaces.filter { it.id in route.foodPlaceIds },
                        onViewOnMap = { onViewRouteOnMap(route) },
                        onDelete = { routeToDelete = route }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // Diálogo de confirmación
    routeToDelete?.let { route ->
        AlertDialog(
            onDismissRequest = { routeToDelete = null },
            title = { Text("Eliminar Ruta") },
            text = { Text("¿Eliminar la ruta \"${route.name}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteRoute(route)
                    routeToDelete = null
                }) {
                    Text("Eliminar", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { routeToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun RouteCard(
    route: Route,
    foodPlaces: List<FoodPlace>,
    onViewOnMap: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldAccent.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Route,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = route.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${route.foodPlaceIds.size} locales · ${route.estimatedTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            if (route.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = route.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Locales en la ruta
            if (foodPlaces.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                foodPlaces.take(3).forEach { place ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📍", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = place.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (foodPlaces.size > 3) {
                    Text(
                        text = "+${foodPlaces.size - 3} más",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldAccent,
                        modifier = Modifier.padding(start = 22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onViewOnMap,
                    colors = ButtonDefaults.textButtonColors(contentColor = GoldAccent)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver en Mapa")
                }
                TextButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = ErrorRed)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar", color = ErrorRed)
                }
            }
        }
    }
}


