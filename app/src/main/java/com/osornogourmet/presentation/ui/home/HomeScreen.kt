package com.osornogourmet.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.model.User
import com.osornogourmet.presentation.theme.*

/**
 * Pantalla principal - Diseño Dark Premium Grid
 */
@Composable
fun HomeScreen(
    user: User,
    foodPlaces: List<FoodPlace>,
    routeCount: Int,
    onNavigateToPlaces: () -> Unit,
    onNavigateToRoutes: () -> Unit,
    onNavigateToMap: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Minimalista
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Bienvenido,",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        user.name.split(" ").first().uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
                IconButton(onClick = onLogout) {
                    Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión", tint = GoldAccent)
                }
            }

            // Banner minimalista
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceDarkElevated, RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Icon(Icons.Outlined.Diamond, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "RUTAS DE AUTOR",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Explora las selecciones más exclusivas de Osorno.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = onNavigateToMap,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("VER MAPA", letterSpacing = 1.sp, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Grid de Accesos
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    "MI EXPERIENCIA",
                    style = MaterialTheme.typography.bodySmall,
                    color = GoldDark,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PremiumActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Restaurant,
                        title = "Catálogo",
                        value = "${foodPlaces.size} Locales",
                        onClick = onNavigateToPlaces
                    )
                    PremiumActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Map,
                        title = "Mis Rutas",
                        value = "$routeCount Creadas",
                        onClick = onNavigateToRoutes
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Locales Recomendados
            if (foodPlaces.isNotEmpty()) {
                PaddingValues(horizontal = 24.dp).let {
                    Text(
                        "SELECCIÓN DEL CHEF",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldDark,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val topPlaces = foodPlaces.sortedByDescending { it.rating }.take(5)
                    items(topPlaces) { place ->
                        PremiumPlaceCard(place)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun PremiumActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceDark)
            .clickable(onClick = onClick)
            .border(1.dp, SurfaceDarkElevated, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Icon(
                icon,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun PremiumPlaceCard(place: FoodPlace) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceDarkElevated, RoundedCornerShape(8.dp))
    ) {
        Column {
            // Header Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF0A0A0A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.RestaurantMenu,
                    contentDescription = null,
                    tint = SurfaceDarkElevated,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = place.name.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${place.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = place.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
