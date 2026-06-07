package com.osornogourmet.domain.usecase.foodplace

import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para eliminar un local de comida.
 */
class DeleteFoodPlaceUseCase(private val repository: FoodPlaceRepository) {
    suspend operator fun invoke(foodPlace: FoodPlace) {
        repository.deleteFoodPlace(foodPlace)
    }
}

