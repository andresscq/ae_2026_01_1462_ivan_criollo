package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.ParkingSpotResponseDto
import com.pucetec.exam2.mappers.toResponse
import com.pucetec.exam2.repositories.ParkingSpotRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/parking-spots")
class ParkingSpotController(
    private val parkingSpotRepository: ParkingSpotRepository
) {

    // endpoint PÚBLICO: consulta los espacios libres
    @GetMapping("/available")
    fun getAvailableSpots(): ResponseEntity<List<ParkingSpotResponseDto>> {
        val availableSpots = parkingSpotRepository.findByIsOccupiedFalse()
            .map { it.toResponse() }
        return ResponseEntity.ok(availableSpots)
    }
}