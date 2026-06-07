package com.osornogourmet.presentation.ui.foodplace

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.presentation.theme.*
import com.osornogourmet.presentation.viewmodel.FoodPlaceUiState

/**
 * Pantalla de lista de locales de comida - Diseño Dark Premium (Estilo Menú)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodPlaceListScreen(
    uiState: FoodPlaceUiState,
    filteredPlaces: List<FoodPlace>,
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    onAddPlace: () -> Unit,
    onEditPlace: (FoodPlace) -> Unit,
    onDeletePlace: (FoodPlace) -> Unit,
    onViewOnMap: (FoodPlace) -> Unit,
    onBack: () -> Unit
) {
    var placeToDelete by remember { mutableStateOf<FoodPlace?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CATÁLOGO GOURMET",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = GoldAccent,
                        style = MaterialTheme.typography.titleMedium
                    )
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
                onClick = onAddPlace,
                containerColor = SurfaceDark,
                contentColor = GoldAccent,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.border(1.dp, GoldAccent, RoundedCornerShape(16.dp))
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Agregar local")
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filtro por categorías minimalista
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    val isSelected = uiState.selectedCategory == category
                    Surface(
                        color = if (isSelected) GoldAccent else Color.Transparent,
                        shape = RoundedCornerShape(4.dp),
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, SurfaceDarkElevated),
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                        onClick = { onCategorySelected(category) }
                    ) {
                        Text(
                            text = category.uppercase(),
                            color = if (isSelected) DarkBackground else TextSecondary,
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Lista de locales (Estilo menú de restaurante)
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GoldAccent)
                }
            } else if (filteredPlaces.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.RestaurantMenu, contentDescription = null, tint = SurfaceDarkElevated, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No hay elementos en esta selección.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(filteredPlaces) { place ->
                        FoodPlaceMenuItem(
                            place = place,
                            onEdit = { onEditPlace(place) },
                            onDelete = { placeToDelete = place },
                            onViewOnMap = { onViewOnMap(place) }
                        )
                        Divider(color = SurfaceDarkElevated, thickness = 1.dp, modifier = Modifier.padding(top = 24.dp))
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    placeToDelete?.let { place ->
        AlertDialog(
            onDismissRequest = { placeToDelete = null },
            containerColor = SurfaceDark,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary,
            title = { Text("Eliminar Registro") },
            text = { Text("¿Desea remover permanentemente \"${place.name}\" del catálogo?") },
            confirmButton = {
                TextButton(onClick = { onDeletePlace(place); placeToDelete = null }) {
                    Text("Eliminar", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { placeToDelete = null }) {
                    Text("Cancelar", color = GoldAccent)
                }
            }
        )
    }
}

@Composable
fun FoodPlaceMenuItem(
    place: FoodPlace,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewOnMap: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = place.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldAccent,
                    letterSpacing = 1.sp
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${place.rating}",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = place.description,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = SurfaceDarkElevated, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = place.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(150.dp)
                    )
                }
                if (place.openingHours.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = SurfaceDarkElevated, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = place.openingHours,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
            
            Row {
                IconButton(onClick = onViewOnMap, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Map, contentDescription = "Mapa", tint = GoldAccent, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = ErrorRed, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
