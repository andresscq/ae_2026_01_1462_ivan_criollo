package com.pucetec.exam2.services

import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.*
import com.pucetec.exam2.repositories.ParkingSpotRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ParkingService(
    private val parkingSpotRepository: ParkingSpotRepository,
    private val ticketRepository: TicketRepository
) {
    private val log = LoggerFactory.getLogger(ParkingService::class.java)
    private val capacity = 20 // Capacidad máxima parametrizable en un solo lugar

    @Transactional
    fun registerEntry(spotId: Long, licensePlate: String): Ticket {
        log.info("Intentando registrar entrada para vehículo: $licensePlate en espacio ID: $spotId")

        // 1. Validación de Estacionamiento Lleno
        val occupiedCount = parkingSpotRepository.countByIsOccupiedTrue()
        if (occupiedCount >= capacity) {
            log.warn("Registro rechazado: El estacionamiento está lleno ($occupiedCount/$capacity)")
            throw ParkingFullException("El estacionamiento ha alcanzado su capacidad máxima de $capacity espacios.")
        }

        // 2. Validación: Espacio inexistente
        val spot = parkingSpotRepository.findById(spotId)
            .orElseThrow {
                log.error("Espacio con ID $spotId no encontrado")
                ParkingSpotNotFoundException("El espacio de estacionamiento no existe.")
            }

        // Validación Adicional Opcional: ¿Ya está ocupado este espacio específico?
        if (spot.isOccupied) {
            throw SpotAlreadyOccupiedException("El espacio ${spot.code} ya se encuentra ocupado.")
        }

        // Lógica de negocio: Ocupar espacio y crear ticket
        spot.isOccupied = true
        parkingSpotRepository.save(spot)

        val ticket = Ticket(licensePlate = licensePlate, parkingSpot = spot)
        return ticketRepository.save(ticket)
    }

    @Transactional
    fun registerExit(ticketId: Long): Ticket {
        log.info("Intentando registrar salida para el ticket ID: $ticketId")

        // 3. Validación: Ticket inexistente
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow {
                log.error("Ticket con ID $ticketId no encontrado")
                TicketNotFoundException("El ticket de estacionamiento no existe.")
            }

        // 4. VALIDACIÓN ADICIONAL PROPIA: Ticket ya cerrado
        if (ticket.exitTime != null) {
            log.warn("El ticket ID $ticketId ya fue cerrado previamente")
            throw TicketAlreadyClosedException("Este ticket ya registra una salida previa.")
        }

        // Lógica de negocio: Cerrar ticket y liberar espacio
        ticket.exitTime = LocalDateTime.now()
        val spot = ticket.parkingSpot
        spot.isOccupied = false

        parkingSpotRepository.save(spot)
        return ticketRepository.save(ticket)
    }
}