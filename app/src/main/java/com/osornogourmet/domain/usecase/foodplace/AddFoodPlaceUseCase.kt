package com.osornogourmet.domain.usecase.foodplace

import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para agregar un local de comida.
 * Principio SRP: Solo se encarga de la lógica de inserción.
 */
class AddFoodPlaceUseCase(private val repository: FoodPlaceRepository) {

    sealed class Result {
        data class Success(val id: Long) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(foodPlace: FoodPlace): Result {
        if (foodPlace.name.isBlank()) {
            return Result.Error("El nombre del local es obligatorio")
        }
        if (foodPlace.address.isBlank()) {
            return Result.Error("La dirección es obligatoria")
        }
        val id = repository.insertFoodPlace(foodPlace)
        return Result.Success(id)
    }
}

