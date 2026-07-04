package com.pucetec.exam2.repositories

import com.pucetec.exam2.entities.ParkingSpot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpotRepository : JpaRepository<ParkingSpot, Long> {
    // Esto lo usa el servicio para validar si el parqueadero se llenó (capacidad >= 20)
    fun countByIsOccupiedTrue(): Int

    // Esto te servirá para el endpoint público de consultar disponibles
    fun findByIsOccupiedFalse(): List<ParkingSpot>
}