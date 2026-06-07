package com.osornogourmet.data.local

import com.osornogourmet.data.local.entity.FoodPlaceEntity

/**
 * Datos precargados de locales de comida gourmet en Osorno.
 * Coordenadas reales o aproximadas de la ciudad de Osorno, Región de Los Lagos, Chile.
 */
object FoodPlaceSeeder {

    fun getInitialFoodPlaces(): List<FoodPlaceEntity> = listOf(
        FoodPlaceEntity(
            name = "Bistró del Lago",
            description = "Cocina de autor con ingredientes locales, carnes premium y una excelente carta de vinos de la región.",
            category = "Restaurante",
            address = "Av. Zenteno 120, Osorno",
            latitude = -40.5750,
            longitude = -73.1300,
            rating = 4.8f,
            phone = "+56 64 250 1111",
            openingHours = "Mar-Dom 19:00-23:30"
        ),
        FoodPlaceEntity(
            name = "La Cava Gourmet",
            description = "Especialistas en maridaje, tablas de quesos importados y embutidos artesanales.",
            category = "Bar",
            address = "O'Higgins 500, Osorno",
            latitude = -40.5735,
            longitude = -73.1345,
            rating = 4.7f,
            phone = "+56 64 250 2222",
            openingHours = "Jue-Sáb 18:00-02:00"
        ),
        FoodPlaceEntity(
            name = "Patagonia Sweet",
            description = "Pastelería francesa y chocolatería fina con cacao ecuatoriano y frutos del bosque.",
            category = "Pastelería",
            address = "Manuel Rodríguez 800, Osorno",
            latitude = -40.5715,
            longitude = -73.1285,
            rating = 4.9f,
            phone = "+56 64 250 3333",
            openingHours = "Lun-Sáb 10:00-19:30"
        ),
        FoodPlaceEntity(
            name = "El Asador Premium",
            description = "Cortes de carne Wagyu y Angus a la parrilla, con guarniciones sofisticadas.",
            category = "Restaurante",
            address = "Av. República 300, Osorno",
            latitude = -40.5770,
            longitude = -73.1380,
            rating = 4.6f,
            phone = "+56 64 250 4444",
            openingHours = "Mié-Dom 13:00-16:00, 20:00-23:00"
        ),
        FoodPlaceEntity(
            name = "Osorno Coffee Roasters",
            description = "Café de especialidad tostado en el local, con métodos de filtrado manual y pastelería vegana.",
            category = "Café",
            address = "Cochrane 450, Osorno",
            latitude = -40.5705,
            longitude = -73.1330,
            rating = 4.8f,
            phone = "+56 64 250 5555",
            openingHours = "Lun-Vie 08:00-20:00, Sáb 09:00-14:00"
        )
    )
}
