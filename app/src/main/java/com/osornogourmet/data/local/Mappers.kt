package com.osornogourmet.data.local

import com.osornogourmet.data.local.entity.FoodPlaceEntity
import com.osornogourmet.data.local.entity.RouteEntity
import com.osornogourmet.data.local.entity.UserEntity
import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.model.Route
import com.osornogourmet.domain.model.User

/**
 * Funciones de mapeo entre entidades Room y modelos de dominio.
 * Permite mantener separadas las capas de data y domain.
 */

// === USER MAPPERS ===
fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    password = password
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    password = password
)

// === FOOD PLACE MAPPERS ===
fun FoodPlaceEntity.toDomain(): FoodPlace = FoodPlace(
    id = id,
    name = name,
    description = description,
    category = category,
    address = address,
    latitude = latitude,
    longitude = longitude,
    rating = rating,
    imageUrl = imageUrl,
    phone = phone,
    openingHours = openingHours
)

fun FoodPlace.toEntity(): FoodPlaceEntity = FoodPlaceEntity(
    id = id,
    name = name,
    description = description,
    category = category,
    address = address,
    latitude = latitude,
    longitude = longitude,
    rating = rating,
    imageUrl = imageUrl,
    phone = phone,
    openingHours = openingHours
)

// === ROUTE MAPPERS ===
fun RouteEntity.toDomain(): Route = Route(
    id = id,
    name = name,
    description = description,
    userId = userId,
    foodPlaceIds = if (foodPlaceIds.isBlank()) emptyList()
                   else foodPlaceIds.split(",").mapNotNull { it.trim().toLongOrNull() },
    estimatedTime = estimatedTime
)

fun Route.toEntity(): RouteEntity = RouteEntity(
    id = id,
    name = name,
    description = description,
    userId = userId,
    foodPlaceIds = foodPlaceIds.joinToString(","),
    estimatedTime = estimatedTime
)

