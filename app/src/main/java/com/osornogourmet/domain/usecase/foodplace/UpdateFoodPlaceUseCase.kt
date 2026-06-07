package com.osornogourmet.domain.usecase.foodplace

import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para actualizar un local de comida.
 */
class UpdateFoodPlaceUseCase(private val repository: FoodPlaceRepository) {
    suspend operator fun invoke(foodPlace: FoodPlace) {
        repository.updateFoodPlace(foodPlace)
    }
}

