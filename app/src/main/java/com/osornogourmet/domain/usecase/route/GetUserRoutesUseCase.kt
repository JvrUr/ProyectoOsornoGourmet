package com.osornogourmet.domain.usecase.route

import com.osornogourmet.domain.model.Route
import com.osornogourmet.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todas las rutas de un usuario.
 */
class GetUserRoutesUseCase(private val repository: RouteRepository) {
    operator fun invoke(userId: Long): Flow<List<Route>> = repository.getRoutesByUserId(userId)
}

