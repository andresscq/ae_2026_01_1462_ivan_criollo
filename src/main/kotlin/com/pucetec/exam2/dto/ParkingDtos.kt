package com.pucetec.exam2.dto

import java.time.LocalDateTime

// Lo que envía el cliente para registrar una entrada
data class EntryRequestDto(
    val parkingSpotId: Long,
    val licensePlate: String
)

// Lo que devolvemos al cliente cuando se genera o consulta un ticket
data class TicketResponseDto(
    val id: Long?,
    val licensePlate: String,
    val entryTime: LocalDateTime,
    val exitTime: LocalDateTime?,
    val spotCode: String
)

// Lo que devolvemos al consultar espacios disponibles
data class ParkingSpotResponseDto(
    val id: Long?,
    val code: String,
    val isOccupied: Boolean
)