package com.pucetec.exam2.mappers

import com.pucetec.exam2.dto.ParkingSpotResponseDto
import com.pucetec.exam2.dto.TicketResponseDto
import com.pucetec.exam2.entities.ParkingSpot
import com.pucetec.exam2.entities.Ticket

fun ParkingSpot.toResponse() = ParkingSpotResponseDto(
    id = this.id,
    code = this.code,
    isOccupied = this.isOccupied
)

fun Ticket.toResponse() = TicketResponseDto(
    id = this.id,
    licensePlate = this.licensePlate,
    entryTime = this.entryTime,
    exitTime = this.exitTime,
    spotCode = this.parkingSpot.code
)