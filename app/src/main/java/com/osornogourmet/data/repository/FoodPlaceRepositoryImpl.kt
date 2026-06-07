package com.osornogourmet.data.repository

import com.osornogourmet.data.local.dao.FoodPlaceDao
import com.osornogourmet.data.local.toDomain
import com.osornogourmet.data.local.toEntity
import com.osornogourmet.domain.model.FoodPlace
import com.osornogourmet.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de locales de comida.
 * Patrón Repository: Abstrae el acceso a la base de datos Room.
 */
class FoodPlaceRepositoryImpl(
    private val foodPlaceDao: FoodPlaceDao
) : FoodPlaceRepository {

    override fun getAllFoodPlaces(): Flow<List<FoodPlace>> {
        return foodPlaceDao.getAllFoodPlaces().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getFoodPlaceById(id: Long): FoodPlace? {
        return foodPlaceDao.getFoodPlaceById(id)?.toDomain()
    }

    override suspend fun getFoodPlacesByCategory(category: String): List<FoodPlace> {
        return foodPlaceDao.getFoodPlacesByCategory(category).map { it.toDomain() }
    }

    override suspend fun insertFoodPlace(foodPlace: FoodPlace): Long {
        return foodPlaceDao.insert(foodPlace.toEntity())
    }

    override suspend fun updateFoodPlace(foodPlace: FoodPlace) {
        foodPlaceDao.update(foodPlace.toEntity())
    }

    override suspend fun deleteFoodPlace(foodPlace: FoodPlace) {
        foodPlaceDao.delete(foodPlace.toEntity())
    }

    override suspend fun searchFoodPlaces(query: String): List<FoodPlace> {
        return foodPlaceDao.searchFoodPlaces(query).map { it.toDomain() }
    }
}

