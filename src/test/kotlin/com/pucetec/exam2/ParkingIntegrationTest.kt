package com.pucetec.exam2.services

import com.pucetec.exam2.entities.ParkingSpot
import com.pucetec.exam2.entities.Ticket
import com.pucetec.exam2.exceptions.*
import com.pucetec.exam2.repositories.ParkingSpotRepository
import com.pucetec.exam2.repositories.TicketRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.Optional

class ParkingServiceTest {

    private val parkingSpotRepository: ParkingSpotRepository = mock(ParkingSpotRepository::class.java)
    private val ticketRepository: TicketRepository = mock(TicketRepository::class.java)
    private val parkingService = ParkingService(parkingSpotRepository, ticketRepository)

    // ==========================================
    // PRUEBAS PARA REGISTER ENTRY (ENTRADA)
    // ==========================================

    @Test
    fun `registerEntry - Deberia registrar entrada con exito`() {
        val spot = ParkingSpot(id = 1L, code = "A-01", isOccupied = false)
        val ticket = Ticket(id = 10L, licensePlate = "PCW-1234", parkingSpot = spot)

        `when`(parkingSpotRepository.countByIsOccupiedTrue()).thenReturn(5)
        `when`(parkingSpotRepository.findById(1L)).thenReturn(Optional.of(spot))
        `when`(parkingSpotRepository.save(spot)).thenReturn(spot)
        `when`(ticketRepository.save(any(Ticket::class.java))).thenReturn(ticket)

        val result = parkingService.registerEntry(1L, "PCW-1234")

        assertNotNull(result)
        assertTrue(spot.isOccupied)
        assertEquals("PCW-1234", result.licensePlate)
    }

    @Test
    fun `registerEntry - Deberia lanzar ParkingFullException cuando supera capacidad`() {
        `when`(parkingSpotRepository.countByIsOccupiedTrue()).thenReturn(20) // Capacidad máxima

        assertThrows<ParkingFullException> {
            parkingService.registerEntry(1L, "PCW-1234")
        }
    }

    @Test
    fun `registerEntry - Deberia lanzar ParkingSpotNotFoundException si espacio no existe`() {
        `when`(parkingSpotRepository.countByIsOccupiedTrue()).thenReturn(5)
        `when`(parkingSpotRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<ParkingSpotNotFoundException> {
            parkingService.registerEntry(1L, "PCW-1234")
        }
    }

    @Test
    fun `registerEntry - Deberia lanzar SpotAlreadyOccupiedException si espacio ya esta ocupado`() {
        val spotOcupado = ParkingSpot(id = 1L, code = "A-01", isOccupied = true)

        `when`(parkingSpotRepository.countByIsOccupiedTrue()).thenReturn(5)
        `when`(parkingSpotRepository.findById(1L)).thenReturn(Optional.of(spotOcupado))

        assertThrows<SpotAlreadyOccupiedException> {
            parkingService.registerEntry(1L, "PCW-1234")
        }
    }

    // ==========================================
    // PRUEBAS PARA REGISTER EXIT (SALIDA)
    // ==========================================

    @Test
    fun `registerExit - Deberia registrar salida con exito`() {
        val spot = ParkingSpot(id = 1L, code = "A-01", isOccupied = true)
        val ticketActivo = Ticket(id = 10L, licensePlate = "PCW-1234", parkingSpot = spot)

        `when`(ticketRepository.findById(10L)).thenReturn(Optional.of(ticketActivo))
        `when`(parkingSpotRepository.save(spot)).thenReturn(spot)
        `when`(ticketRepository.save(ticketActivo)).thenReturn(ticketActivo)

        val result = parkingService.registerExit(10L)

        assertNotNull(result)
        assertNotNull(result.exitTime)
        assertFalse(spot.isOccupied)
    }

    @Test
    fun `registerExit - Deberia lanzar TicketNotFoundException si ticket no existe`() {
        `when`(ticketRepository.findById(10L)).thenReturn(Optional.empty())

        assertThrows<TicketNotFoundException> {
            parkingService.registerExit(10L)
        }
    }

    @Test
    fun `registerExit - Deberia lanzar TicketAlreadyClosedException si ticket ya tiene salida`() {
        val spot = ParkingSpot(id = 1L, code = "A-01", isOccupied = false)
        val ticketCerrado = Ticket(id = 10L, licensePlate = "PCW-1234", parkingSpot = spot)
        ticketCerrado.exitTime = LocalDateTime.now() // Forzamos que ya esté cerrado

        `when`(ticketRepository.findById(10L)).thenReturn(Optional.of(ticketCerrado))

        assertThrows<TicketAlreadyClosedException> {
            parkingService.registerExit(10L)
        }
    }
}