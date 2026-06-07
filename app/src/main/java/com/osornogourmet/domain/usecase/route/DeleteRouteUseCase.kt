package com.osornogourmet.domain.usecase.route

import com.osornogourmet.domain.model.Route
import com.osornogourmet.domain.repository.RouteRepository

/**
 * Caso de uso para eliminar una ruta.
 */
class DeleteRouteUseCase(private val repository: RouteRepository) {
    suspend operator fun invoke(route: Route) {
        repository.deleteRoute(route)
    }
}

