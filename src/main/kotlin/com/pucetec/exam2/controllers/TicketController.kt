package com.pucetec.exam2.controllers

import com.pucetec.exam2.dto.EntryRequestDto
import com.pucetec.exam2.dto.TicketResponseDto
import com.pucetec.exam2.mappers.toResponse
import com.pucetec.exam2.services.ParkingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tickets")
class TicketController(
    private val parkingService: ParkingService
) {

    // endpoint PRIVADO: Registrar entrada
    @PostMapping("/entry")
    fun registerEntry(@RequestBody request: EntryRequestDto): ResponseEntity<TicketResponseDto> {
        val ticket = parkingService.registerEntry(request.parkingSpotId, request.licensePlate)
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket.toResponse())
    }

    // endpoint PRIVADO: Registrar salida
    @PutMapping("/{id}/exit")
    fun registerExit(@PathVariable id: Long): ResponseEntity<TicketResponseDto> {
        val ticket = parkingService.registerExit(id)
        return ResponseEntity.ok(ticket.toResponse())
    }
}