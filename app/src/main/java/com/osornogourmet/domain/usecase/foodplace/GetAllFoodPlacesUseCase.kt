package com.osornogourmet.domain.usecase.foodplace

import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todos los locales de comida.
 * Principio SRP: Solo recupera la lista completa.
 */
class GetAllFoodPlacesUseCase(private val repository: FoodPlaceRepository) {
    operator fun invoke(): Flow<List<FoodPlace>> = repository.getAllFoodPlaces()
}

