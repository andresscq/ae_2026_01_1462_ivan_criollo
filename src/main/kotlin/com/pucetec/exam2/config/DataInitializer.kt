package com.pucetec.exam2.config

import com.pucetec.exam2.entities.ParkingSpot
import com.pucetec.exam2.repositories.ParkingSpotRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(private val parkingSpotRepository: ParkingSpotRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        // Solo inserta si la tabla está vacía
        if (parkingSpotRepository.count() == 0L) {
            val spots = listOf(
                ParkingSpot(code = "A-01"),
                ParkingSpot(code = "A-02"),
                ParkingSpot(code = "A-03"),
                ParkingSpot(code = "B-01"),
                ParkingSpot(code = "B-02")
            )
            parkingSpotRepository.saveAll(spots)
            println("➔ ¡Espacios iniciales de estacionamiento creados con éxito!")
        }
    }
}